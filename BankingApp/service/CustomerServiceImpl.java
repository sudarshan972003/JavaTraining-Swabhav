package com.aurionpro.BankingApp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aurionpro.BankingApp.dto.AddressDTO;
import com.aurionpro.BankingApp.dto.ApiResponse;
import com.aurionpro.BankingApp.dto.CustomerDTO;
import com.aurionpro.BankingApp.entity.Address;
import com.aurionpro.BankingApp.entity.Customer;
import com.aurionpro.BankingApp.exception.ResourceNotFoundException;
import com.aurionpro.BankingApp.repository.CustomerRepository;

import jakarta.transaction.Transactional;

@Service
public class CustomerServiceImpl implements com.aurionpro.BankingApp.service.CustomerService {

    @Autowired
    private CustomerRepository customerRepo;

    private CustomerDTO toDto(Customer c, boolean showAddressId) {
        CustomerDTO dto = new CustomerDTO();
        dto.setCustomerId(c.getCustomerId());
        dto.setContactNo(c.getContactNo());
        dto.setEmail(c.getEmail());
        dto.setDob(c.getDob());
        
        if (c.getAddress() != null) {
            AddressDTO addrDto = new AddressDTO();
            addrDto.setAddressId(c.getAddress().getAddressId());
            addrDto.setCity(c.getAddress().getCity());
            addrDto.setState(c.getAddress().getState());
            addrDto.setPincode(c.getAddress().getPincode());
            dto.setAddress(addrDto);
            
            if (!showAddressId) {
                addrDto.hideId();
            }
            
            dto.setAddress(addrDto);
        }
        
        return dto;
    }

    @Override
    public CustomerDTO getCustomerById(Long id, boolean showAddressId) {
        Customer c = customerRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        return toDto(c, showAddressId);
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepo.findAll().stream(). map(c -> toDto(c, true)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CustomerDTO updateCustomer(Long id, CustomerDTO dto, boolean showAddressId) {
        Customer c = customerRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        if (dto.getEmail() != null) c.setEmail(dto.getEmail());
        if (dto.getContactNo() != null) c.setContactNo(dto.getContactNo());
        if (dto.getDob() != null) c.setDob(dto.getDob());
        if (dto.getAddress() != null) {
            AddressDTO addrDto = dto.getAddress();
            Address addr = c.getAddress();
            if (addrDto.getCity() != null) addr.setCity(addrDto.getCity());
            if (addrDto.getState() != null) addr.setState(addrDto.getState());
            if (addrDto.getPincode() != null) addr.setPincode(addrDto.getPincode());
        }
        customerRepo.save(c);
        return toDto(c, showAddressId);
    }

    @Override
    @Transactional
    public ApiResponse deleteCustomer(Long id) {
        Customer c = customerRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        customerRepo.delete(c);
        return new ApiResponse("Customer Deleted successfully");
    }
    
//    @Override
//    public Long getCustomerIdByUsername(String username) {
//        return customerRepo.findByUser_Username(username)
//                .map(Customer::getCustomerId)
//                .orElseThrow(() -> new ResourceNotFoundException(
//                        "Customer not found for username: " + username));
//    }
}
