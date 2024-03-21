package com.payme.buddy.service.impl;

import com.payme.buddy.dto.ConnexionDto;
import com.payme.buddy.entity.Connexion;
import com.payme.buddy.entity.User;
import com.payme.buddy.repository.ConnexionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConnexionServiceImplTest {
    @Mock
    private ConnexionRepository connexionRepository;
    @InjectMocks
    private ConnexionServiceImpl connexionService;

    @Test
    void testFindConnectedUsersFrom() {
        User user1 = new User();
        user1.setEmail("user1@test.com");

        User user2 = new User();
        user2.setEmail("user2@test.com");

        Connexion connexion = new Connexion();
        connexion.setUser1(user1);
        connexion.setUser2(user2);

        when(connexionRepository.findConnexionsByUser_Email("user1@test.com")).thenReturn(List.of(connexion));

        List<ConnexionDto> connectedUsers = connexionService.findConnectedUsersFrom("user1@test.com");
        assertFalse(connectedUsers.isEmpty());
        assertEquals("user2@test.com", connectedUsers.get(0).getEmail());

        verify(connexionRepository, times(1)).findConnexionsByUser_Email("user1@test.com");
    }

    @Test
    void testFindConnectedUsersFromWithError() {
        when(connexionRepository.findConnexionsByUser_Email("invalid@test.com")).thenThrow(new RuntimeException("Database error"));

        List<ConnexionDto> connectedUsers = connexionService.findConnectedUsersFrom("invalid@test.com");
        assertTrue(connectedUsers.isEmpty());
        verify(connexionRepository, times(1)).findConnexionsByUser_Email("invalid@test.com");
    }

    @Test
    void testCreateConnexion() {
        User user1 = new User();
        User user2 = new User();

        connexionService.createConnexion(user1, user2);
        verify(connexionRepository, times(1)).save(any(Connexion.class));
    }
    @Test
    void testExistConnexion() {
        when(connexionRepository.existsConnexionByUser1_EmailAndUser2_Email("user1@test.com", "user2@test.com")).thenReturn(true);

        assertTrue(connexionService.existConnexion("user1@test.com", "user2@test.com"));
        verify(connexionRepository, times(1)).existsConnexionByUser1_EmailAndUser2_Email("user1@test.com", "user2@test.com");
    }
    @Test
    void testRemoveConnexion() {
        Connexion connexion = new Connexion();
        connexion.setId(1L);

        when(connexionRepository.findConnexionByUser1_EmailAndUser2_Email("user1@test.com", "user2@test.com")).thenReturn(Optional.of(connexion));

        connexionService.removeConnexion("user1@test.com", "user2@test.com");

        verify(connexionRepository, times(1)).removeConnexionById(connexion.getId());
    }
}
