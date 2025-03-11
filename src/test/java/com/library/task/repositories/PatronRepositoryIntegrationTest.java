package com.library.task.repositories;

import com.library.task.models.Patron;
import com.library.task.infrastructure.repositories.PatronRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PatronRepositoryIntegrationTest {

    @Autowired
    private PatronRepository patronRepository;

    private Patron patron;

    @BeforeEach
    public void setUp() {
        patron = new Patron();
        patron.setName("Patron One");
        patron.setContactInformation("patron1@example.com");
    }

    @Test
    public void testCreatePatron() {
        Patron savedPatron = patronRepository.save(patron);
        assertNotNull(savedPatron);
        assertNotNull(savedPatron.getId());
        assertEquals("Patron One", savedPatron.getName());
    }

    @Test
    public void testFindPatronById() {
        Patron savedPatron = patronRepository.save(patron);
        Optional<Patron> foundPatron = patronRepository.findById(savedPatron.getId());

        assertTrue(foundPatron.isPresent());
        assertEquals("Patron One", foundPatron.get().getName());
    }

    @Test
    public void testUpdatePatron() {
        Patron savedPatron = patronRepository.save(patron);
        savedPatron.setName("Updated Patron");
        Patron updatedPatron = patronRepository.save(savedPatron);

        assertEquals("Updated Patron", updatedPatron.getName());
    }

    @Test
    public void testDeletePatron() {
        Patron savedPatron = patronRepository.save(patron);
        Long id = savedPatron.getId();
        patronRepository.deleteById(id);
        Optional<Patron> foundPatron = patronRepository.findById(id);

        assertFalse(foundPatron.isPresent());
    }
}