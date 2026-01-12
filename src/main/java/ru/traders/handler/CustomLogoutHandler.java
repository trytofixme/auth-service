package ru.traders.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.traders.entity.Token;
import ru.traders.repository.TokenRepository;
import ru.traders.service.UserService;

@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final UserService userService;
    private final TokenRepository tokenRepository;

    @Override
    @Transactional
    public void logout(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @Nullable Authentication authentication) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        String token = authHeader.substring(7);
        tokenRepository.findByAccessToken(token)
                .ifPresent(tokenEntity -> {
                    tokenEntity.setLoggedOut(true);
                    tokenRepository.save(tokenEntity);
                });
    }
}
