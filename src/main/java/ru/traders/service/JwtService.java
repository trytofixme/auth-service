package ru.traders.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.traders.entity.User;
import ru.traders.repository.TokenRepository;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${security.jwt.secret_key}")
    private String secretKey;

    @Value("${security.jwt.access_token_expiration}")
    private long accessTokenExpiration;

    @Value("${security.jwt.refresh_token_expiration}")
    private long refreshTokenExpiration;

    private final TokenRepository tokenRepository;

    public String generateAccessToken(User user) {
        return generateToken(user, accessTokenExpiration);
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, refreshTokenExpiration);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Transactional(readOnly = true)
    public boolean isValidAccess(String token, User user) {
        String username = extractUsername(token);

        boolean isValidToken = tokenRepository.findByAccessToken(token)
                .map(tkn -> !tkn.isLoggedOut())
                .orElse(false);

        return username.equals(user.getUsername())
                && isAccessTokenAlive(token)
                && isValidToken;
    }

    @Transactional(readOnly = true)
    public boolean isValidRefresh(String token, User user) {
        String username = extractUsername(token);

        boolean isValidToken = tokenRepository.findByRefreshToken(token)
                .map(tkn -> !tkn.isLoggedOut())
                .orElse(false);

        return username.equals(user.getUsername())
                && isAccessTokenAlive(token)
                && isValidToken;
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String generateToken(User user, long expiryTime) {
        JwtBuilder jwtBuilder = Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiryTime))
                .signWith(getSigningKey());

        return jwtBuilder.compact();
    }

    private boolean isAccessTokenAlive(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);
        return !expiration.before(new Date(System.currentTimeMillis()));
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        JwtParserBuilder jwtParserBuilder = Jwts.parser();
        jwtParserBuilder.verifyWith(getSigningKey());

        Claims claims = jwtParserBuilder.build()
                .parseSignedClaims(token)
                .getPayload();

        return resolver.apply(claims);
    }
}
