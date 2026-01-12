package ru.traders.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.traders.dto.AuthenticationResponseDto;
import ru.traders.dto.LoginRequestDto;
import ru.traders.dto.RegistrationRequestDto;
import ru.traders.service.AuthenticationService;
import ru.traders.service.UserService;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/registration")
    public ResponseEntity<String> register(@RequestBody RegistrationRequestDto registrationDto) {
        if (userService.existsByUsername(registrationDto.getUsername())) {
            return ResponseEntity.badRequest().body("Имя пользователя уже занято");
        }

        if (userService.existsByEmail(registrationDto.getEmail())) {
            return ResponseEntity.badRequest().body("Email уже занят");
        }

        authenticationService.register(registrationDto);
        return ResponseEntity.ok("Регистрация прошла успешно");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<AuthenticationResponseDto> refreshToken(HttpServletRequest request) {
        return authenticationService.refreshToken(request);
    }
}