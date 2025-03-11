package com.library.task.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Patron {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 50, message = "Name must be less than 50 characters")
    private String name;

    @NotBlank(message = "Contact information is required")
    @Size(max = 100, message = "Contact information must be less than 100 characters")
    private String contactInformation;

    @Column(nullable = false)
    private boolean isDeleted = false; //  Soft delete flag

    @OneToMany(mappedBy = "patron", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<BorrowingRecord> borrowingRecords;




}