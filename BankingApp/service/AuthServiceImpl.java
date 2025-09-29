package com.aurionpro.BankingApp.service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aurionpro.BankingApp.config.JwtUtil;
import com.aurionpro.BankingApp.dto.AddressDTO;
import com.aurionpro.BankingApp.dto.AdminRegisterRequest;
import com.aurionpro.BankingApp.dto.ApiResponse;
import com.aurionpro.BankingApp.dto.AuthRequest;
import com.aurionpro.BankingApp.dto.AuthResponse;
import com.aurionpro.BankingApp.dto.RegisterRequest;
import com.aurionpro.BankingApp.entity.Address;
import com.aurionpro.BankingApp.entity.Customer;
import com.aurionpro.BankingApp.entity.Role;
import com.aurionpro.BankingApp.repository.AddressRepository;
import com.aurionpro.BankingApp.repository.CustomerRepository;
import com.aurionpro.BankingApp.repository.RoleRepository;
import com.aurionpro.BankingApp.repository.UserRepository;
import com.aurionpro.BankingApp.util.EmailService;

import jakarta.transaction.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationConfiguration authConfig;
    private final JwtUtil jwtUtil;
    private final CustomerRepository customerRepo;
    private final AddressRepository addressRepo;
    private final EmailService emailService;

    public AuthServiceImpl(UserRepository userRepo,
                           RoleRepository roleRepo,
                           PasswordEncoder passwordEncoder,
                           AuthenticationConfiguration authConfig,
                           JwtUtil jwtUtil,
                           CustomerRepository customerRepo,
                           AddressRepository addressRepo,
                           EmailService emailService) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
        this.authConfig = authConfig;
        this.jwtUtil = jwtUtil;
        this.customerRepo = customerRepo;
        this.addressRepo = addressRepo;
        this.emailService = emailService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var optional = userRepo.findByUsername(username);
        if (optional.isEmpty()) throw new UsernameNotFoundException("User not found");
        var user = optional.get();
        var authorities = user.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getName().toUpperCase()))
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    @Override
    @Transactional
    public AuthResponse login(AuthRequest request) {
        try {
            var authManager = authConfig.getAuthenticationManager();
            authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (Exception e) {
            throw new RuntimeException("Invalid username/password", e);
        }

        var user = userRepo.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid user"));

        Map<String,Object> extras = new HashMap<>();
        extras.put("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
        String token = jwtUtil.generateToken(user.getUsername(), extras);
        return new AuthResponse(token);
    }

    @Override
    @Transactional
    public ApiResponse register(RegisterRequest request) {
        if (userRepo.existsByUsername(request.getUsername())) {
            throw new RuntimeException("username already exists");
        }

        var appUser = new com.aurionpro.BankingApp.entity.User();
        appUser.setUsername(request.getUsername());
        appUser.setPassword(passwordEncoder.encode(request.getPassword()));

        Role role = roleRepo.findByName("customer").orElseGet(() -> {
            Role r = new Role();
            r.setName("customer");
            return roleRepo.save(r);
        });
        appUser.getRoles().add(role);
        userRepo.save(appUser);

        AddressDTO addrDto = request.getAddress();
        Address addr = new Address();
        addr.setCity(addrDto.getCity());
        addr.setState(addrDto.getState());
        addr.setPincode(addrDto.getPincode());
        addressRepo.save(addr);

        Customer cust = new Customer();
        cust.setEmail(request.getEmail());
        cust.setContactNo(request.getContactNo());
        cust.setDob(request.getDob());
        cust.setUser(appUser);
        cust.setAddress(addr);
//        customerRepo.save(cust);
        cust = customerRepo.save(cust);

//        emailService.sendSimpleMessage(request.getEmail(), "Welcome to BankingApp", "Your account created successfully.");

        String body = String.format(
        	    "Dear Customer,\n\nWelcome to BankingApp!\n\nCustomer has been created successfully.\nYour Customer ID is: %d\n\nPlease keep it safe.\n\nThanks,\nBankingApp Team",
        	    cust.getCustomerId()
        	);
        emailService.sendSimpleMessage(request.getEmail(), "Welcome to BankingApp", body);
        
        return new ApiResponse("Registration successful");
    }
    
    @Override
    @Transactional
    public ApiResponse registerAdmin(AdminRegisterRequest request) {
        if (userRepo.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        var adminUser = new com.aurionpro.BankingApp.entity.User();
        adminUser.setUsername(request.getUsername());
        adminUser.setPassword(passwordEncoder.encode(request.getPassword()));

        Role role = roleRepo.findByName("admin")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("admin");
                    return roleRepo.save(r);
                });

        adminUser.getRoles().add(role);
        userRepo.save(adminUser);

        return new ApiResponse("Admin registered successfully");
    }
}
