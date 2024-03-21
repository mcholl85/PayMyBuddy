package com.payme.buddy.service.impl;

import com.payme.buddy.dto.TransactionDto;
import com.payme.buddy.entity.Transaction;
import com.payme.buddy.repository.TransactionRepository;
import com.payme.buddy.service.TransactionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Log4j2
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public Page<TransactionDto> getTransactionsByRecipient(String recipientEmail, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());

        try {
            Page<Transaction> transactions = transactionRepository.findByRecipient_Email(recipientEmail, pageable);
            return transactions.map(TransactionDto::fromEntity);
        } catch(Exception e) {
            log.error("Error fetching transactions by recipient : {}", e.getMessage());
            return Page.empty(pageable);
        }
    }

    @Override
    public Page<TransactionDto> getTransactionsBySender(String senderEmail, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());

        try {
            Page<Transaction> transactions = transactionRepository.findBySender_Email(senderEmail, pageable);
            return transactions.map(TransactionDto::fromEntity);
        } catch(Exception e) {
            log.error("Error fetching transactions by sender : {}", e.getMessage());
            return Page.empty(pageable);
        }
    }

    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }
}
