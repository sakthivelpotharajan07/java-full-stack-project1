package com.zidio.zidio_connect.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationDto {
    @NotBlank
    @Size(min = 2, message = "Name must be at least 2 characters long")
    private String name;

    @NotBlank
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @NotBlank(message = "Role must be selected")
    private String role;
}