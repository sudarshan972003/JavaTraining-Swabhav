package com.aurionpro.BankingApp.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aurionpro.BankingApp.dto.TransactionDTO;
import com.aurionpro.BankingApp.entity.Account;
import com.aurionpro.BankingApp.entity.Customer;
import com.aurionpro.BankingApp.entity.Transaction;
import com.aurionpro.BankingApp.exception.BadRequestException;
import com.aurionpro.BankingApp.exception.ResourceNotFoundException;
import com.aurionpro.BankingApp.repository.AccountRepository;
import com.aurionpro.BankingApp.repository.CustomerRepository;
import com.aurionpro.BankingApp.repository.TransactionRepository;
import com.aurionpro.BankingApp.util.EmailService;

import jakarta.transaction.Transactional;

@Service
public class TransactionServiceImpl implements com.aurionpro.BankingApp.service.TransactionService {

	@Autowired
	private TransactionRepository txRepo;
	@Autowired
	private AccountRepository accountRepo;
	@Autowired
	private CustomerRepository customerRepo;
	@Autowired
	private EmailService emailService;

	private TransactionDTO toDto(Transaction t) {
		TransactionDTO d = new TransactionDTO();
		d.setTransId(t.getTransId());
		d.setTransType(t.getTransType());
		d.setAmount(t.getAmount());
		d.setDate(t.getDate());
		d.setAccountId(t.getAccount().getAccountId());
		d.setCustomerId(t.getCustomer().getCustomerId());
		return d;
	}

	@Override
	@Transactional
	public TransactionDTO createTransaction(TransactionDTO dto) {
		Account acc = accountRepo.findById(dto.getAccountId())
				.orElseThrow(() -> new ResourceNotFoundException("Account not found"));
		Customer cust = customerRepo.findById(dto.getCustomerId())
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

		if (!acc.getCustomer().getCustomerId().equals(cust.getCustomerId())) {
			throw new BadRequestException("Account does not belong to customer");
		}

		String type = dto.getTransType().toLowerCase();
		double amount = dto.getAmount();
		if (amount <= 0)
			throw new BadRequestException("Amount must be positive");

		if ("debit".equals(type)) {
			if (acc.getBalance() < amount)
				throw new BadRequestException("Insufficient funds");
			acc.setBalance(acc.getBalance() - amount);
		} else if ("credit".equals(type)) {
			acc.setBalance(acc.getBalance() + amount);
		} else if ("transfer".equals(type)) {
			throw new BadRequestException("Use dedicated transfer endpoint or include target account id");
		} else {
			throw new BadRequestException("Unknown transaction type");
		}

		accountRepo.save(acc);

		Transaction tx = new Transaction();
		tx.setTransType(dto.getTransType());
		tx.setAmount(amount);
		tx.setDate(LocalDateTime.now());
		tx.setAccount(acc);
		tx.setCustomer(cust);
		txRepo.save(tx);

		String subject = "Transaction Alert - " + dto.getTransType().toUpperCase();
		String body = String.format(
				"Dear %s,\n\nA %s transaction of ₹%.2f has been %s your account %s on %s.\n\nAvailable Balance: ₹%.2f\n\nRegards,\nBankingApp",
				cust.getUser().getUsername(), dto.getTransType().toUpperCase(), amount,
				("debit".equals(type) ? "debited from" : "credited to"), acc.getAccountNumber(), tx.getDate(),
				acc.getBalance());
		emailService.sendSimpleMessage(cust.getEmail(), subject, body);

		return toDto(tx);
	}

	@Override
	public List<TransactionDTO> getTransactionsByAccountId(Long accountId) {
		  return txRepo.findByAccount_AccountId(accountId).stream().map(this::toDto).collect(Collectors.toList());
//		List<Transaction> txs = txRepo.findByAccount_AccountId(accountId).stream()
//				.sorted((a, b) -> a.getDate().compareTo(b.getDate())).collect(Collectors.toList());
//
//		double runningBalance = txs.isEmpty() ? 0 : txs.get(0).getAccount().getBalance();
//		List<TransactionDTO> dtos = new ArrayList<>();
//
//		for (Transaction t : txs) {
//			TransactionDTO dto = toDto(t);
//
//			// compute running balance
//			if ("debit".equalsIgnoreCase(t.getTransType()))
//				runningBalance -= t.getAmount();
//			else if ("credit".equalsIgnoreCase(t.getTransType()))
//				runningBalance += t.getAmount();
//			dto.setBalanceAfterTransaction(runningBalance);
//
//			dtos.add(dto);
//		}
//
//		return dtos;
	}

	@Override
	public List<TransactionDTO> getTransactionsByCustomerId(Long customerId) {
		return txRepo.findByCustomer_CustomerId(customerId).stream().map(this::toDto).collect(Collectors.toList());
	}
}
