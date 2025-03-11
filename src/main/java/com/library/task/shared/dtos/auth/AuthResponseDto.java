package com.library.task.shared.dtos.auth;

import lombok.*;

@Data
@Builder
public class AuthResponseDto {
    private final String jwt;

    public AuthResponseDto(String jwt) {
        this.jwt = jwt;
    }

}
