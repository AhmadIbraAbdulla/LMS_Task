package com.library.task.shared.dtos.shared;

import lombok.*;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageRequestDto {
    @Builder.Default
    private int page = 0;
    @Builder.Default
    private int size = 10;
    @Builder.Default
    private String sortBy = "id";
    @Builder.Default
    private String sortDirection = "asc";


}