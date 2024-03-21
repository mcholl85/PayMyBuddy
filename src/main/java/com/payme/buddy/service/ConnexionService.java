package com.payme.buddy.service;

import com.payme.buddy.dto.ConnexionDto;
import com.payme.buddy.entity.User;

import java.util.List;

public interface ConnexionService {
    List<ConnexionDto> findConnectedUsersFrom(String email);
    void createConnexion(User user1, User user2);
    boolean existConnexion(String user1Email, String user2Email);
    void removeConnexion(String user1Email, String user2Email);
}
