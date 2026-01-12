package ru.traders.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginRequestDto {

    private String username;
    private String password;
}
