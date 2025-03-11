package com.library.task.shared.dtos.auth;

import com.library.task.shared.validator.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password should be at least 6 characters")
    @ValidPassword
    private String password;
    @NotBlank(message = "Role is required")
    private String roleName;


    public String getRole() {
        return roleName;
    }


}
