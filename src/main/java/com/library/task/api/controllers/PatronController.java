package com.library.task.api.controllers;

import com.library.task.shared.dtos.patrons.PatronCreateDto;
import com.library.task.shared.dtos.patrons.PatronDto;
import com.library.task.shared.dtos.patrons.PatronUpdateDto;
import com.library.task.shared.dtos.shared.PageRequestDto;
import com.library.task.shared.dtos.shared.PageResponse;
import com.library.task.service.patron.IPatronService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patrons")
public class PatronController {

    private IPatronService patronService;

    public PatronController(IPatronService patronService) {
        this.patronService = patronService;
    }

    @GetMapping()
    public ResponseEntity<PageResponse<PatronDto>> getPatronsByPage(@ModelAttribute PageRequestDto pageRequestDto) {
        Sort sort = Sort.by(Sort.Direction.fromString(pageRequestDto.getSortDirection()), pageRequestDto.getSortBy());
        Pageable pageable = PageRequest.of(pageRequestDto.getPage(), pageRequestDto.getSize(), sort);
        PageResponse<PatronDto> response = patronService.getPatronsByPage(pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<PatronDto> getPatronById(@PathVariable Long id) {
        return new ResponseEntity<>(patronService.getPatronById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PatronDto> addPatron(@RequestBody PatronCreateDto patronCreateDto) {
        return new ResponseEntity<>(patronService.addPatron(patronCreateDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatronDto> updatePatron(@PathVariable Long id, @RequestBody PatronUpdateDto patronDto) {
        return new ResponseEntity<>(patronService.updatePatron(id, patronDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatron(@PathVariable Long id) {
        patronService.deletePatron(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}