package com.example.nic_validation_service.service;



import com.example.nic_validation_service.exception.InvalidNicException;
import com.example.nic_validation_service.model.Nic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NicServiceTest {

    private NicService nicService;

    @BeforeEach
    void setUp() {
        nicService = new NicService();
    }

    @Test
    void testParseNicsFromCsv_ValidOldFormat() {
        List<String> nicNumbers = Arrays.asList("820149894V");
        List<Nic> nics = nicService.parseNicsFromCsv(nicNumbers);

        assertEquals(1, nics.size());
        Nic nic = nics.get(0);
        assertEquals("820149894V", nic.getNic_no());
        assertEquals("Male", nic.getGender());
        assertNotNull(nic.getBirthDate());
        assertTrue(nic.getAge() > 0);
    }

    @Test
    void testParseNicsFromCsv_ValidNewFormat() {
        List<String> nicNumbers = Arrays.asList("198201409894");
        List<Nic> nics = nicService.parseNicsFromCsv(nicNumbers);

        assertEquals(1, nics.size());
        Nic nic = nics.get(0);
        assertEquals("198201409894", nic.getNic_no());
        assertEquals("Male", nic.getGender());
        assertNotNull(nic.getBirthDate());
        assertTrue(nic.getAge() > 0);
    }

    @Test
    void testParseNicsFromCsv_InvalidFormat() {
        List<String> nicNumbers = Arrays.asList("12345");
        assertThrows(InvalidNicException.class, () -> nicService.parseNicsFromCsv(nicNumbers));
    }
}
