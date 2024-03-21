package com.payme.buddy.service.impl;

import com.payme.buddy.dto.TransactionFormDto;
import com.payme.buddy.entity.Transaction;
import com.payme.buddy.entity.User;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PayMeServiceImplTest {
    @Mock
    private UserServiceImpl userService;
    @Mock
    private ConnexionServiceImpl connexionService;
    @Mock
    private TransactionServiceImpl transactionService;

    @InjectMocks
    private PayMeServiceImpl payMeService;

    private User sender;
    private User recipient;
    private TransactionFormDto transactionFormDto;

    @BeforeEach
    void setUp() {
        sender = new User();
        sender.setEmail("sender@example.com");
        sender.setBalance(new BigDecimal("1000"));

        recipient = new User();
        recipient.setEmail("recipient@example.com");
        recipient.setBalance(new BigDecimal("500"));

        transactionFormDto = new TransactionFormDto();
        transactionFormDto.setAmount(new BigDecimal("100"));
        transactionFormDto.setDescription("Test transfer");
        transactionFormDto.setConnection("recipient@example.com");
    }

    @Test
    void testSendMoneySuccess() {
        when(userService.getUserByEmail("sender@example.com")).thenReturn(Optional.of(sender));
        when(userService.getUserByEmail("recipient@example.com")).thenReturn(Optional.of(recipient));

        payMeService.sendMoney("sender@example.com", transactionFormDto);

        assertEquals(new BigDecimal("895.00"), sender.getBalance());
        assertEquals(new BigDecimal("600"), recipient.getBalance());

        verify(transactionService, times(1)).saveTransaction(any(Transaction.class));
        verify(userService, times(1)).updateUser(sender);
        verify(userService, times(1)).updateUser(recipient);
    }
    @Test
    void testSendMoneyUnknownSender() {
        when(userService.getUserByEmail("sender@example.com")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> payMeService.sendMoney("sender@example.com", transactionFormDto));
    }
    @Test
    void testSendMoneyUnknownRecipient() {
        when(userService.getUserByEmail("sender@example.com")).thenReturn(Optional.of(sender));
        when(userService.getUserByEmail("recipient@example.com")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> payMeService.sendMoney("sender@example.com", transactionFormDto));
    }
    @Test
    void testAddConnexionSuccess() {
        when(userService.getUserByEmail("user1@example.com")).thenReturn(Optional.of(sender));
        when(userService.getUserByEmail("user2@example.com")).thenReturn(Optional.of(recipient));

        payMeService.addConnexion("user1@example.com", "user2@example.com");

        verify(connexionService, times(1)).createConnexion(sender, recipient);
    }
    @Test
    void testAddConnexionUnknownUser1() {
        when(userService.getUserByEmail("user1@example.com")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> payMeService.addConnexion("user1@example.com", "user2@example.com"));
    }

    @Test
    void testAddConnexionUnknownUser2() {
        when(userService.getUserByEmail("user1@example.com")).thenReturn(Optional.of(sender));
        when(userService.getUserByEmail("user2@example.com")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> payMeService.addConnexion("user1@example.com", "user2@example.com"));
    }

}
