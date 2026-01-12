package ru.traders.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RegistrationRequestDto {

    private String username;
    private String email;
    private String password;
}
