package com.library.task.services;


import com.library.task.shared.dtos.patrons.PatronCreateDto;
import com.library.task.shared.dtos.patrons.PatronDto;
import com.library.task.shared.dtos.patrons.PatronUpdateDto;
import com.library.task.shared.exception.BadRequestException;
import com.library.task.shared.exception.ResourceNotFoundException;
import com.library.task.models.Patron;
import com.library.task.infrastructure.repositories.PatronRepository;
import com.library.task.service.patron.PatronServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PatronServiceImplTest {

    @InjectMocks
    private PatronServiceImpl patronService;

    @Mock
    private PatronRepository patronRepository;

    @Mock
    private ModelMapper modelMapper;

    private Patron patron;
    private PatronDto patronDto;
    private PatronCreateDto patronCreateDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        patron = new Patron(1L, "Patron One", "patron1@example.com",false, null);
        patronDto = new PatronDto(1L, "Patron One", "patron1@example.com");
        patronCreateDto = new PatronCreateDto("Patron One", "patron1@example.com");
    }


    @Test
    public void testGetPatronById_Success() {
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));
        when(modelMapper.map(patron, PatronDto.class)).thenReturn(patronDto);

        PatronDto foundPatron = patronService.getPatronById(1L);

        assertNotNull(foundPatron);
        assertEquals(patronDto, foundPatron);
        verify(patronRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetPatronById_NotFound() {
        when(patronRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> patronService.getPatronById(1L));
        assertEquals("Patron not found with id: 1", exception.getMessage());
    }

    @Test
    public void testAddPatron() {
        when(modelMapper.map(patronCreateDto, Patron.class)).thenReturn(patron);
        when(patronRepository.save(patron)).thenReturn(patron);
        when(modelMapper.map(patron, PatronDto.class)).thenReturn(patronDto);

        PatronDto createdPatron = patronService.addPatron(patronCreateDto);

        assertNotNull(createdPatron);
        assertEquals(patronDto, createdPatron);
        verify(patronRepository, times(1)).save(patron);
    }

    @Test
    public void testUpdatePatron_Success() {
        PatronUpdateDto patronUpdateDto = new PatronUpdateDto(1L, "Updated Patron", "updated@example.com");

        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));
        when(modelMapper.map(patronUpdateDto, Patron.class)).thenReturn(patron);
        when(patronRepository.save(patron)).thenReturn(patron);
        when(modelMapper.map(patron, PatronDto.class)).thenReturn(patronDto);

        PatronDto updatedPatron = patronService.updatePatron(1L, patronUpdateDto);

        assertNotNull(updatedPatron);
        assertEquals(patronDto, updatedPatron);
        verify(patronRepository, times(1)).save(patron);
    }

    @Test
    public void testUpdatePatron_IdMismatch() {
        PatronUpdateDto patronUpdateDto = new PatronUpdateDto(2L, "Updated Patron", "updated@example.com");
        BadRequestException exception = assertThrows(BadRequestException.class, () -> patronService.updatePatron(1L, patronUpdateDto));
        assertEquals("ID in path and request body do not match", exception.getMessage());
    }

    @Test
    public void testDeletePatron_Success() {
        when(patronRepository.existsById(1L)).thenReturn(true);

        patronService.deletePatron(1L);

        verify(patronRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeletePatron_NotFound() {
        when(patronRepository.existsById(1L)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> patronService.deletePatron(1L));
        assertEquals("Patron not found with id: 1", exception.getMessage());
    }
}