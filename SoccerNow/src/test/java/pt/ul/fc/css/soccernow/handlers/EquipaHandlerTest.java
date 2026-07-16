package pt.ul.fc.css.soccernow.handlers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pt.ul.fc.css.soccernow.dominio.Equipa;
import pt.ul.fc.css.soccernow.dto.EquipaDTO;
import pt.ul.fc.css.soccernow.repositorio.EquipaRepository;
import pt.ul.fc.css.soccernow.repositorio.JogadorRepository;

@ExtendWith(MockitoExtension.class)
public class EquipaHandlerTest {

    @Mock
    private EquipaRepository equipaRepository;

    @Mock
    private JogadorRepository jogadorRepository;

    @InjectMocks
    private EquipaHandler equipaHandler;

    private EquipaDTO equipaDTO;
    private Equipa savedEquipa;

    @BeforeEach
    void setUp() {
        equipaDTO = new EquipaDTO();
        equipaDTO.setNome("Benfica");

        savedEquipa = createEquipaComId(1L, "Benfica");
    }

    // TEST CREATE
    @Test
    void testCreateEquipa() {
        when(equipaRepository.save(any(Equipa.class))).thenReturn(savedEquipa);

        EquipaDTO result = equipaHandler.create(equipaDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Benfica", result.getNome());
        verify(equipaRepository, times(2)).save(any(Equipa.class));
    }

    @Test
    void testCreateEquipa_NomeNulo_ThrowsIllegalArgumentException() {
        equipaDTO.setNome(null);

        Exception exception = assertThrows(IllegalArgumentException.class, 
            () -> equipaHandler.create(equipaDTO));

        assertEquals("Nome da equipa é obrigatório", exception.getMessage());
        verify(equipaRepository, times(0)).save(any(Equipa.class));
    }

    @Test
    void testCreateEquipa_NomeVazio_ThrowsIllegalArgumentException() {
        equipaDTO.setNome("");

        Exception exception = assertThrows(IllegalArgumentException.class, 
            () -> equipaHandler.create(equipaDTO));

        assertEquals("Nome da equipa é obrigatório", exception.getMessage());
        verify(equipaRepository, times(0)).save(any(Equipa.class));
    }

    @Test
    void testCreateEquipa_NomeExistente_ThrowsIllegalArgumentException() {
        when(equipaRepository.findByNomeIgnoreCase(equipaDTO.getNome())).thenReturn(savedEquipa);

        Exception exception = assertThrows(IllegalArgumentException.class, 
            () -> equipaHandler.create(equipaDTO));

        assertEquals("Já existe uma equipa com este nome", exception.getMessage());
        verify(equipaRepository, times(0)).save(any(Equipa.class));
    }

    // TEST UPDATE
    @Test
    void testUpdateEquipa() {
        Long id = 1L;
        EquipaDTO equipaDTOAtualizada = new EquipaDTO();
        equipaDTOAtualizada.setNome("Benfica Atualizado");
        
        Equipa equipaAtualizada = createEquipaComId(id, "Benfica Atualizado");

        when(equipaRepository.findById(id)).thenReturn(Optional.of(savedEquipa));
        when(equipaRepository.save(any(Equipa.class))).thenReturn(equipaAtualizada);

        EquipaDTO result = equipaHandler.update(id, equipaDTOAtualizada);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Benfica Atualizado", result.getNome());
        verify(equipaRepository, times(1)).findById(id);
        verify(equipaRepository, times(1)).save(any(Equipa.class));
    }

    @Test
    void testUpdateEquipa_NonExistingId_ThrowsIllegalArgumentException() {
        Long id = 100L;
        when(equipaRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, 
            () -> equipaHandler.update(id, equipaDTO));

        assertEquals("Equipa não encontrada", exception.getMessage());
        verify(equipaRepository, times(1)).findById(id);
        verify(equipaRepository, times(0)).save(any(Equipa.class));
    }

    // TEST DELETE
    @Test
    void testDeleteEquipa_ExistingId_ReturnsEquipaDTO() {
        Long id = 1L;
        when(equipaRepository.findById(id)).thenReturn(Optional.of(savedEquipa));

        EquipaDTO result = equipaHandler.delete(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Benfica", result.getNome());
        verify(equipaRepository, times(1)).findById(id);
        verify(equipaRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteEquipa_NonExistingId_ThrowsIllegalArgumentException() {
        Long id = 100L;
        when(equipaRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, 
            () -> equipaHandler.delete(id));

        assertEquals("Equipa não encontrada", exception.getMessage());
        verify(equipaRepository, times(1)).findById(id);
        verify(equipaRepository, times(0)).deleteById(anyLong());
    }

    // TEST FIND ALL
    @Test
    void testFindAll_ExistingEquipas_ReturnsListOfEquipaDTOs() {
        Equipa savedEquipa2 = createEquipaComId(2L, "Porto");

        List<Equipa> equipas = Arrays.asList(savedEquipa, savedEquipa2);
        when(equipaRepository.findAll()).thenReturn(equipas);

        List<EquipaDTO> results = equipaHandler.findAll();

        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals(savedEquipa.getId(), results.get(0).getId());
        assertEquals(savedEquipa2.getId(), results.get(1).getId());
        verify(equipaRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_NoEquipas_ReturnsEmptyList() {
        when(equipaRepository.findAll()).thenReturn(Arrays.asList());

        List<EquipaDTO> results = equipaHandler.findAll();

        assertNotNull(results);
        assertTrue(results.isEmpty());
        verify(equipaRepository, times(1)).findAll();
    }

    // Método auxiliar
    private Equipa createEquipaComId(Long id, String nome) {
        Equipa equipa = new Equipa(nome);
        try {
            Field idField = Equipa.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(equipa, id);
        } catch (Exception e) {
            fail("Falha ao definir ID");
        }
        return equipa;
    }
}