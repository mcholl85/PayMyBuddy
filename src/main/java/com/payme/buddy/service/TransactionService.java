package com.payme.buddy.service;

import com.payme.buddy.dto.TransactionDto;
import com.payme.buddy.entity.Transaction;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TransactionService {
    Page<TransactionDto> getTransactionsByRecipient(String email, int page, int size);
    Page<TransactionDto> getTransactionsBySender(String senderEmail, int page, int size);
    void saveTransaction(Transaction transaction);
}
