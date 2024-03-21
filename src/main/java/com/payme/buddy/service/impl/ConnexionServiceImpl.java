package com.payme.buddy.service.impl;

import com.payme.buddy.dto.ConnexionDto;
import com.payme.buddy.entity.Connexion;
import com.payme.buddy.entity.User;
import com.payme.buddy.repository.ConnexionRepository;
import com.payme.buddy.service.ConnexionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class ConnexionServiceImpl implements ConnexionService {
    @Autowired
    private ConnexionRepository connexionRepository;

    @Override
    public List<ConnexionDto> findConnectedUsersFrom(String email) {
        try {
            List<Connexion> connexions = connexionRepository.findConnexionsByUser_Email(email);
            List<User> connectedUsers = connexions.stream().map(connexion -> connexion.getUser2().getEmail().equals(email) ? connexion.getUser1() : connexion.getUser2()).toList();
            return connectedUsers.stream().map(ConnexionDto::fromEntity).toList();
        } catch(Exception e) {
            log.error("Error to find Connected Users with {}", email);
            return Collections.emptyList();
        }
    }

    @Override
    public void createConnexion(User user1, User user2) {
        Connexion connexion = Connexion.builder().user1(user1).user2(user2).build();
        connexionRepository.save(connexion);
    }
    @Override
    public boolean existConnexion(String user1Email, String user2Email) {
        return connexionRepository.existsConnexionByUser1_EmailAndUser2_Email(user1Email, user2Email) || connexionRepository.existsConnexionByUser1_EmailAndUser2_Email(user2Email, user1Email);
    }

    @Transactional
    @Override
    public void removeConnexion(String user1Email, String user2Email) {
        Optional<Connexion> connexionUser1User2 = connexionRepository.findConnexionByUser1_EmailAndUser2_Email(user1Email, user2Email);
        Optional<Connexion> connexionUser2User1 = connexionRepository.findConnexionByUser1_EmailAndUser2_Email(user2Email, user1Email);

        if(connexionUser1User2.isPresent()) {
            Connexion connexionToDelete = connexionUser1User2.get();
            connexionRepository.removeConnexionById(connexionToDelete.getId());
        }

        if(connexionUser2User1.isPresent()) {
            Connexion connexionToDelete = connexionUser2User1.get();
            connexionRepository.removeConnexionById(connexionToDelete.getId());
        }
    }
}
