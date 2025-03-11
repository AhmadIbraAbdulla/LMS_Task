package com.library.task.service.autentication;

import com.library.task.shared.dtos.auth.AuthResponseDto;
import com.library.task.shared.dtos.auth.LogInRequestDto;
import com.library.task.shared.dtos.auth.SignUpRequestDto;
import com.library.task.shared.dtos.auth.UserDto;


public interface IAuthenticationService {
    AuthResponseDto loginService(LogInRequestDto logInRequest);
    UserDto signUpService(SignUpRequestDto signUpRequest);
}
