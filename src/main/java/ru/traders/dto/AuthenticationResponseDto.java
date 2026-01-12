package ru.traders.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AuthenticationResponseDto {

    private final String accessToken;
    private final String refreshToken;
}
