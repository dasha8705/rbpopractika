package ru.mtuci.antiviruslicensesystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.mtuci.antiviruslicensesystem.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
    Optional<User> findByEmail(String email);
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);
}
