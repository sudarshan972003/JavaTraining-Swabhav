package com.aurionpro.BankingApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aurionpro.BankingApp.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
	
}
