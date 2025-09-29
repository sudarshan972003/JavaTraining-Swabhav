package com.aurionpro.BankingApp.service;

import java.util.List;

import com.aurionpro.BankingApp.dto.ApiResponse;
import com.aurionpro.BankingApp.dto.CustomerDTO;

public interface CustomerService {
    CustomerDTO getCustomerById(Long id, boolean showAddressId);
    List<CustomerDTO> getAllCustomers();
    CustomerDTO updateCustomer(Long id, CustomerDTO dto, boolean showAddressId);
    ApiResponse deleteCustomer(Long id);
//    Long getCustomerIdByUsername(String username);
}
