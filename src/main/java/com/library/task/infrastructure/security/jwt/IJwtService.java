package com.library.task.infrastructure.security.jwt;

import com.library.task.shared.dtos.auth.LogInRequestDto;

public interface IJwtService {
    String createJwtToken(LogInRequestDto authenticationRequest);
}
