package com.library.task.shared.dtos.patrons;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatronCreateDto {
    @NotBlank(message = "Name is required")
    @Size(max = 50, message = "Name must be less than 50 characters")
    private String name;

    @NotBlank(message = "Contact information is required")
    @Size(max = 100, message = "Contact information must be less than 100 characters")
    private String contactInformation;


}