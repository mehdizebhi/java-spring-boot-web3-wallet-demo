package dev.mehdizebhi.web3.repositories;

import dev.mehdizebhi.web3.entities.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSessionRepository extends JpaRepository<UserSession, Integer> {
}