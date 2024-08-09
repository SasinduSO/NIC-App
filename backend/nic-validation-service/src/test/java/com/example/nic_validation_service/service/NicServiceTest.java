package com.example.nic_validation_service.service;

import com.example.nic_validation_service.exception.InvalidNicException;
import com.example.nic_validation_service.model.Nic;
import com.example.nic_validation_service.repository.NicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
//import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest // or @ExtendWith(SpringExtension.class) for older versions
class NicServiceTest {

    @Mock
    private NicRepository nicRepository;

    @InjectMocks
    private NicService nicService;
    String filename = "nic1.csv";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testParseNicsFromCsv_ValidOldFormat() {
        List<String> nicNumbers = Arrays.asList("820149894V");

        // Mock the repository save method
        when(nicRepository.save(any(Nic.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<Nic> nics = nicService.parseNicsFromCsv(nicNumbers, filename);

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

        // Mock the repository save method
        when(nicRepository.save(any(Nic.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<Nic> nics = nicService.parseNicsFromCsv(nicNumbers, filename);

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

        assertThrows(InvalidNicException.class, () -> nicService.parseNicsFromCsv(nicNumbers, filename));
    }
}
