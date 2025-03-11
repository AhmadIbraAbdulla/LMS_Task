package com.library.task.shared.dtos.patrons;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatronUpdateDto {
    @NotNull(message = "Patron id is required")
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 50, message = "Name must be less than 50 characters")
    private String name;

    @NotBlank(message = "Contact information is required")
    @Size(max = 100, message = "Contact information must be less than 100 characters")
    private String contactInformation;

}
