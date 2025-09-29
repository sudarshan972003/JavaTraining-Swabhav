package com.aurionpro.BankingApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aurionpro.BankingApp.dto.ApiResponse;
import com.aurionpro.BankingApp.dto.CustomerDTO;
import com.aurionpro.BankingApp.service.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired private CustomerService customerService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<CustomerDTO> all() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN') or @customerSecurity.isOwner(#id, authentication.name)")
    public CustomerDTO get(@PathVariable Long id, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return customerService.getCustomerById(id, isAdmin);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN') or @customerSecurity.isOwner(#id, authentication.name)")
    public CustomerDTO update(@PathVariable Long id, @Valid @RequestBody CustomerDTO dto, Authentication authentication) {
    	boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    	return customerService.updateCustomer(id, dto, isAdmin);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        ApiResponse response = customerService.deleteCustomer(id);
        return ResponseEntity.ok(response);
    }
}
