package com.payme.buddy.service.impl;

import com.payme.buddy.dto.TransactionFormDto;
import com.payme.buddy.entity.Transaction;
import com.payme.buddy.entity.User;
import com.payme.buddy.service.ConnexionService;
import com.payme.buddy.service.PayMeService;
import com.payme.buddy.service.TransactionService;
import com.payme.buddy.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class PayMeServiceImpl implements PayMeService {
    private static final BigDecimal FEE = new BigDecimal("0.05");

    @Autowired
    private UserService userService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ConnexionService connexionService;

    @Transactional
    @Override
    public void sendMoney(String emailSender, TransactionFormDto transactionFormDto) {
        BigDecimal amount = transactionFormDto.getAmount();
        BigDecimal fee = calculateFee(amount);

        User senderUser = userService.getUserByEmail(emailSender).orElseThrow(() -> new EntityNotFoundException("Sender not found"));
        User recipientUser = userService.getUserByEmail(transactionFormDto.getConnection()).orElseThrow(() -> new EntityNotFoundException("Recipient not found"));

        Transaction transaction = Transaction.builder().amount(amount).fee(fee).description(transactionFormDto.getDescription()).sender(senderUser).recipient(recipientUser).build();

        transactionService.saveTransaction(transaction);
        senderUser.removeAmountToBalance(amount.add(fee));
        recipientUser.addAmountToBalance(amount);

        userService.updateUser(senderUser);
        userService.updateUser(recipientUser);
    }

    @Override
    public void addConnexion(String emailUser1, String emailUser2) {
        User user1 = userService.getUserByEmail(emailUser1).orElseThrow(() -> new EntityNotFoundException("User1 not found"));
        User user2 = userService.getUserByEmail(emailUser2).orElseThrow(() -> new EntityNotFoundException("User2 not found"));

        connexionService.createConnexion(user1, user2);
    }

    private BigDecimal calculateFee(BigDecimal amount) {
        return amount.multiply(FEE);
    }
}
