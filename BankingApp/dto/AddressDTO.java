package com.aurionpro.BankingApp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressDTO {
	private Long addressId;
	
    @NotBlank
    private String city;

    @NotBlank
    private String state;

    @NotBlank
    private String pincode;
    
    public void hideId() {
        this.addressId = null;
    }
}
