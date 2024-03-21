package com.payme.buddy.repository;

import com.payme.buddy.entity.Connexion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConnexionRepository extends JpaRepository<Connexion, Long> {
    @Query("SELECT c FROM Connexion c WHERE c.user1.email = :email OR c.user2.email = :email")
    List<Connexion> findConnexionsByUser_Email(@Param("email") String email);
    boolean existsConnexionByUser1_EmailAndUser2_Email(String user1Email, String user2Email);
    Optional<Connexion> findConnexionByUser1_EmailAndUser2_Email(String user1Email, String user2Email);
    Optional<Connexion> removeConnexionById(Long id);
}
