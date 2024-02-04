package dev.sarvesh.userservice.repositories;

import dev.sarvesh.userservice.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;
import java.util.UUID;

public interface SessionRepository extends JpaRepository<Session, UUID> {

    void deleteByUser_Id(UUID userId);

    Optional<Session> findByTokenAndUser_Email(String token, String userEmail);

}
