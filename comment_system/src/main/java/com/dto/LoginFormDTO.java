package com.dto;

import lombok.Data;

@Data
public class LoginFormDTO {
    private String email;
    private String code;
    private String password;
}
