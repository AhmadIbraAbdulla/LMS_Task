package com.library.task.service.patron;

import com.library.task.shared.dtos.patrons.PatronCreateDto;
import com.library.task.shared.dtos.patrons.PatronDto;
import com.library.task.shared.dtos.patrons.PatronUpdateDto;
import com.library.task.shared.dtos.shared.PageResponse;
import org.springframework.data.domain.Pageable;

public interface IPatronService {
    PageResponse<PatronDto> getPatronsByPage(Pageable pageable);

    PatronDto getPatronById(Long id);
    PatronDto addPatron(PatronCreateDto patronCreateDto);
    PatronDto updatePatron(Long id, PatronUpdateDto patronDto);
    void deletePatron(Long id);

}