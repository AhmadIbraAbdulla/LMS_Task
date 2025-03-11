package com.library.task.controllers;

import com.library.task.api.controllers.PatronController;
import com.library.task.shared.dtos.patrons.PatronCreateDto;
import com.library.task.shared.dtos.patrons.PatronDto;
import com.library.task.shared.dtos.patrons.PatronUpdateDto;
import com.library.task.service.patron.IPatronService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class PatronControllerTest {

    @InjectMocks
    private PatronController patronController;

    @Mock
    private IPatronService patronService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    public void testGetPatronById() {
        Long patronId = 1L;
        PatronDto patron = new PatronDto(patronId, "Patron One", "patron1@example.com");

        when(patronService.getPatronById(patronId)).thenReturn(patron);

        ResponseEntity<PatronDto> response = patronController.getPatronById(patronId);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(patronId, response.getBody().getId());
        assertEquals("Patron One", response.getBody().getName());
        assertEquals("patron1@example.com", response.getBody().getContactInformation());
        verify(patronService, times(1)).getPatronById(patronId);
    }

    @Test
    public void testAddPatron() {
        PatronCreateDto patronCreateDto = new PatronCreateDto("Patron One", "patron1@example.com");
        PatronDto patronDto = new PatronDto(1L, "Patron One", "patron1@example.com");

        when(patronService.addPatron(patronCreateDto)).thenReturn(patronDto);

        ResponseEntity<PatronDto> response = patronController.addPatron(patronCreateDto);

        // Assertions
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(patronDto, response.getBody());
        assertEquals("Patron One", response.getBody().getName());
        assertEquals("patron1@example.com", response.getBody().getContactInformation());
        verify(patronService, times(1)).addPatron(patronCreateDto);
    }

    @Test
    public void testUpdatePatron() {
        Long patronId = 1L;
        PatronUpdateDto patronUpdateDto = new PatronUpdateDto(patronId, "Updated Patron", "updated@example.com");
        PatronDto patronDto = new PatronDto(patronId, "Updated Patron", "updated@example.com");

        when(patronService.updatePatron(patronId, patronUpdateDto)).thenReturn(patronDto);

        ResponseEntity<PatronDto> response = patronController.updatePatron(patronId, patronUpdateDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(patronDto, response.getBody());
        assertEquals(patronId, response.getBody().getId());
        assertEquals("Updated Patron", response.getBody().getName());
        assertEquals("updated@example.com", response.getBody().getContactInformation());
        verify(patronService, times(1)).updatePatron(patronId, patronUpdateDto);
    }

    @Test
    public void testDeletePatron() {
        Long patronId = 1L;

        ResponseEntity<Void> response = patronController.deletePatron(patronId);

        // Assertions
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(patronService, times(1)).deletePatron(patronId);
    }
}