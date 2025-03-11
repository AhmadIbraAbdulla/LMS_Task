package com.library.task.service.autentication;


import com.library.task.shared.exception.ConflictException;
import com.library.task.shared.exception.ResourceNotFoundException;
import com.library.task.models.Role;
import com.library.task.models.User;
import com.library.task.shared.dtos.auth.AuthResponseDto;
import com.library.task.shared.dtos.auth.LogInRequestDto;
import com.library.task.shared.dtos.auth.SignUpRequestDto;
import com.library.task.shared.dtos.auth.UserDto;
import com.library.task.infrastructure.security.jwt.IJwtService;
import com.library.task.service.user.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationService implements IAuthenticationService {

    private final IJwtService jwtService;
    private final IUserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthenticationService(IJwtService jwtService, IUserService userService,
                                 ModelMapper modelMapper) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    public AuthResponseDto loginService(LogInRequestDto logInRequestDto)  {
        final String jwt = jwtService.createJwtToken(logInRequestDto);
        AuthResponseDto authenticationResponse = new AuthResponseDto(jwt);
        return authenticationResponse;
    }

    public UserDto signUpService(SignUpRequestDto signUpRequestDto) {
        if (userService.findByEmail(signUpRequestDto.getEmail()).isPresent()) {
            throw new ConflictException("User already exists with email: " + signUpRequestDto.getEmail());
        }
        User user = modelMapper.map(signUpRequestDto, User.class);
        Role role = userService.findRoleByName(signUpRequestDto.getRole())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + signUpRequestDto.getRole()));

        user.setRole(role);
        User savedUser = userService.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }
}

