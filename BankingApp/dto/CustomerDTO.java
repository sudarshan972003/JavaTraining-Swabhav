package com.aurionpro.BankingApp.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CustomerDTO {
    private Long customerId;
    private String email;
    private String contactNo;
    private LocalDate dob;
    
    private AddressDTO address;
}
