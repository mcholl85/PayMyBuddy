package com.payme.buddy.service.impl;

import com.payme.buddy.dto.TransactionDto;
import com.payme.buddy.entity.Transaction;
import com.payme.buddy.entity.User;
import com.payme.buddy.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Transaction transaction;
    private TransactionDto transactionDto;

    @BeforeEach
    void setUp() {
        User sender = new User();
        sender.setFirstname("John");

        transaction = new Transaction();
        transaction.setDescription("A test transaction");
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setFee(new BigDecimal("1.00"));
        transaction.setSender(sender);

        transactionDto = new TransactionDto("Connection test", "A test transaction", new BigDecimal("1.00"), new BigDecimal("100.00"));
    }

    @Test
    void testGetTransactionsByRecipient() {
        Pageable pageable = PageRequest.of(0, 1, Sort.by("createdAt").descending());
        Page<Transaction> transactionPage = new PageImpl<>(List.of(transaction), pageable, 1);

        when(transactionRepository.findByRecipient_Email("recipient@test.com", pageable)).thenReturn(transactionPage);

        Page<TransactionDto> result = transactionService.getTransactionsByRecipient("recipient@test.com", 1, 1);

        assertFalse(result.isEmpty());
        assertEquals(transactionDto.getDescription(), result.getContent().get(0).getDescription());
    }

    @Test
    void testGetTransactionsByRecipientWithError() {
        Pageable pageable = PageRequest.of(0, 1, Sort.by("createdAt").descending());
        when(transactionRepository.findByRecipient_Email("recipient@test.com", pageable)).thenThrow(new RuntimeException("Database error"));

        Page<TransactionDto> result = transactionService.getTransactionsByRecipient("recipient@test.com", 1, 1);

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetTransactionsBySender() {
        Pageable pageable = PageRequest.of(0, 1, Sort.by("createdAt").descending());
        Page<Transaction> transactionPage = new PageImpl<>(List.of(transaction), pageable, 1);

        when(transactionRepository.findBySender_Email("sender@test.com", pageable)).thenReturn(transactionPage);

        Page<TransactionDto> result = transactionService.getTransactionsBySender("sender@test.com", 1, 1);

        assertFalse(result.isEmpty());
        assertEquals(transactionDto.getDescription(), result.getContent().get(0).getDescription());
    }

    @Test
    void testGetTransactionsBySenderWithError() {
        Pageable pageable = PageRequest.of(0, 1, Sort.by("createdAt").descending());
        when(transactionRepository.findBySender_Email("sender@test.com", pageable)).thenThrow(new RuntimeException("Database error"));

        Page<TransactionDto> result = transactionService.getTransactionsBySender("sender@test.com", 1, 1);

        assertTrue(result.isEmpty());
    }
    @Test
    void testSaveTransaction() {
        transactionService.saveTransaction(transaction);
        verify(transactionRepository, times(1)).save(transaction);
    }

}
