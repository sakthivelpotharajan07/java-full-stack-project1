package com.zidio.zidio_connect.dto;

import lombok.Data;

@Data // Using Lombok for boilerplate code
public class AuthRequest {
    private String email;
    private String password;
}