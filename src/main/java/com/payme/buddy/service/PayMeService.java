package com.payme.buddy.service;

import com.payme.buddy.dto.TransactionFormDto;
import com.payme.buddy.entity.User;

public interface PayMeService {
    void sendMoney(String emailSender, TransactionFormDto transactionFormDto);
    void addConnexion(String emailUser1, String emailUser2);
}
