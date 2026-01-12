package ru.traders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.traders.entity.Token;
import ru.traders.entity.User;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("""
            SELECT t
            FROM Token t
            JOIN User u ON t.user.id = u.id
            WHERE t.user.id = :userId AND t.loggedOut = false
            """)
    List<Token> findAllAccessTokenByUser(Long userId);

    Optional<Token> findByAccessToken(String accessToken);
    Optional<Token> findByRefreshToken(String refreshToken);
}
