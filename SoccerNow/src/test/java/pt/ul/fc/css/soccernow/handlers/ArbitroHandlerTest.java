package pt.ul.fc.css.soccernow.handlers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pt.ul.fc.css.soccernow.dominio.Arbitro;
import pt.ul.fc.css.soccernow.dominio.Jogo;
import pt.ul.fc.css.soccernow.dominio.Utilizador;
import pt.ul.fc.css.soccernow.dto.ArbitroDTO;
import pt.ul.fc.css.soccernow.repositorio.ArbitroRepository;

@ExtendWith(MockitoExtension.class)
public class ArbitroHandlerTest {
    @Mock
    private ArbitroRepository arbitroRepository;

    @InjectMocks
    private ArbitroHandler arbitroHandler;

    private ArbitroDTO arbitroDTO;
    private Arbitro savedArbitro;

    @BeforeEach
    void setUp() {
        // Arrange
        arbitroDTO = new ArbitroDTO();
        arbitroDTO.setNome("João Silva");
        arbitroDTO.setUsername("joaosilva");
        arbitroDTO.setPassword("password123");
        arbitroDTO.setIsCertificated(true);

        savedArbitro = createArbitroComId(1L, "João Silva", true, "joaosilva", "password123");

    }


// TEST CREATE
    @Test
    void testCreateArbitro() {
        
        // Simula o comportamento do repositório
        when(arbitroRepository.save(any(Arbitro.class))).thenReturn(savedArbitro);

        // Act
        ArbitroDTO result = arbitroHandler.create(arbitroDTO);
        
        
        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("João Silva", result.getNome());
        assertEquals("joaosilva", result.getUsername());
        assertEquals("password123", result.getPassword());
        assertEquals(true, result.getIsCertificated());
        verify(arbitroRepository, times(1)).save(any(Arbitro.class));
        
    }

    @Test
    void testCreateArbitro_NomeNulo_ThrowsIllegalArgumentException() {
        arbitroDTO.setNome(null);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> arbitroHandler.create(arbitroDTO));

        assertEquals("Nome é obrigatório", exception.getMessage());
        verify(arbitroRepository, times(0)).save(any(Arbitro.class)); // Verifica se o save não foi chamado
    }

    @Test
    void testCreateArbitro_NomeVazio_ThrowsIllegalArgumentException() {
        arbitroDTO.setNome("");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> arbitroHandler.create(arbitroDTO));

        assertEquals("Nome é obrigatório", exception.getMessage());
        verify(arbitroRepository, times(0)).save(any(Arbitro.class));
    }

    @Test
    void testCreateArbitro_UsernameNulo_ThrowsIllegalArgumentException() {
        arbitroDTO.setUsername(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> arbitroHandler.create(arbitroDTO));

        assertEquals("Username é obrigatório", exception.getMessage());
        verify(arbitroRepository, times(0)).save(any(Arbitro.class));

    }

    @Test
    void testCreateArbitro_UsernameVazio_ThrowsIllegalArgumentException() {
        arbitroDTO.setUsername("");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> arbitroHandler.create(arbitroDTO));

        assertEquals("Username é obrigatório", exception.getMessage());
        verify(arbitroRepository, times(0)).save(any(Arbitro.class));

    }

    @Test
    void testCreateArbitro_PasswordNula_ThrowsIllegalArgumentException() {
        arbitroDTO.setPassword(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> arbitroHandler.create(arbitroDTO));

        assertEquals("Password é obrigatória", exception.getMessage());
        verify(arbitroRepository, times(0)).save(any(Arbitro.class)); 
    }

    @Test
    void testCreateArbitro_PasswordVazio_ThrowsIllegalArgumentException() {
        arbitroDTO.setPassword("");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> arbitroHandler.create(arbitroDTO));

        assertEquals("Password é obrigatória", exception.getMessage());
        verify(arbitroRepository, times(0)).save(any(Arbitro.class)); 
    }

    @Test
    void testCreateArbitro_UsernameExistente_ThrowsIllegalArgumentException() {
        when(arbitroRepository.findByUsername(arbitroDTO.getUsername())).thenReturn(savedArbitro);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> arbitroHandler.create(arbitroDTO));

        assertEquals("Username já existe", exception.getMessage());
        verify(arbitroRepository, times(1)).findByUsername(arbitroDTO.getUsername());
    }


// TEST UPDATE
    @Test
    void testUpdateArbitro() {
        Long id = 1L;

        ArbitroDTO arbitroDTOatualizado = new ArbitroDTO();
        arbitroDTOatualizado.setNome("João Silva Atualizado");
        arbitroDTOatualizado.setUsername("joaosilva");
        arbitroDTOatualizado.setPassword("newpassword123");
        arbitroDTOatualizado.setIsCertificated(false);

        Arbitro arbitroAtualizado = createArbitroComId(id, "João Silva Atualizado", false, "joaosilva", "newpassword123");

        when(arbitroRepository.findById(id)).thenReturn(Optional.of(savedArbitro));
        when(arbitroRepository.save(any(Arbitro.class))).thenReturn(arbitroAtualizado);

        ArbitroDTO result = arbitroHandler.update(id, arbitroDTOatualizado);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("João Silva Atualizado", result.getNome());
        assertEquals("joaosilva", result.getUsername());
        assertEquals("newpassword123", result.getPassword());
        assertEquals(false, result.getIsCertificated());
        verify(arbitroRepository, times(1)).findById(id);
        verify(arbitroRepository, times(1)).save(any(Arbitro.class));
    }
    
    @Test
    void testUpdateArbitro_NomeNulo_ThrowsIllegalArgumentException() {
        Long id = 1L;
        arbitroDTO.setNome(null);
        assertThrows(IllegalArgumentException.class, () -> arbitroHandler.update(id, arbitroDTO));
    }
    
    @Test
    void testUpdateArbitro_NomeVazio_ThrowsIllegalArgumentException() {
        Long id = 1L;
        arbitroDTO.setNome("");

        assertThrows(IllegalArgumentException.class, () -> arbitroHandler.update(id, arbitroDTO));
    }

    @Test
    void testUpdateArbitro_UsernameNulo_ThrowsIllegalArgumentException() {
        Long id = 1L;
        arbitroDTO.setUsername(null);

        assertThrows(IllegalArgumentException.class, () -> arbitroHandler.update(id, arbitroDTO));
    }

    @Test
    void testUpdateArbitro_UsernameVazio_ThrowsIllegalArgumentException() {
        Long id = 1L;
        arbitroDTO.setUsername("");

        assertThrows(IllegalArgumentException.class, () -> arbitroHandler.update(id, arbitroDTO));
    }

    @Test
    void testUpdateArbitro_PasswordNula_ThrowsIllegalArgumentException() {
        Long id = 1L;
        arbitroDTO.setPassword(null);

        assertThrows(IllegalArgumentException.class, () -> arbitroHandler.update(id, arbitroDTO));
    }

    @Test
    void testUpdateArbitro_PasswordVazia_ThrowsIllegalArgumentException() {
        Long id = 1L;
        arbitroDTO.setPassword("");

        assertThrows(IllegalArgumentException.class, () -> arbitroHandler.update(id, arbitroDTO));
    }

    @Test
    void testUpdateArbitro_UsernameExistente_ThrowsIllegalArgumentException() {
        Long id = 1L;
        Arbitro outroArbitro = createArbitroComId(2L, "Outro Nome", true, "outroUsername", "outraPassword");
        arbitroDTO.setUsername("outroUsername"); // Tenta usar o username de outro árbitro
        when(arbitroRepository.findByUsername("outroUsername")).thenReturn(outroArbitro);

        assertThrows(IllegalArgumentException.class, () -> arbitroHandler.update(id, arbitroDTO));
    }

    @Test
    void testUpdateArbitro_NonExistingId_ThrowsIllegalArgumentException() {
        Long id = 100L;
        when(arbitroRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            arbitroHandler.update(id, arbitroDTO);
        });

        assertEquals("Árbitro não encontrado", exception.getMessage());
        verify(arbitroRepository, times(1)).findById(id);
        verify(arbitroRepository, times(0)).save(any(Arbitro.class));
    }

    

// TEST DELETE
    @Test
    void testDeleteArbitro_ExistingId_ReturnsArbitroDTO() {
        Long id = 1L;
        when(arbitroRepository.findById(id)).thenReturn(Optional.of(savedArbitro));

        ArbitroDTO result = arbitroHandler.delete(id);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("João Silva", result.getNome());
        assertEquals("joaosilva", result.getUsername());
        assertEquals("password123", result.getPassword());
        assertEquals(true, result.getIsCertificated());
        
        verify(arbitroRepository, times(1)).findById(id);
        verify(arbitroRepository, times(1)).deleteById(id);
    }

     @Test
    void testDeleteArbitro_NonExistingId_ThrowsIllegalArgumentException() {
        Long id = 100L;
        when(arbitroRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            arbitroHandler.delete(id);
        });
        
        assertEquals("Árbitro não encontrado", exception.getMessage());
        verify(arbitroRepository, times(1)).findById(id);
        verify(arbitroRepository, times(0)).deleteById(anyLong()); // Certifica-se de que deleteById não é chamado
    }

     @Test
    void testDeleteArbitro_ComJogosFuturos_ThrowsIllegalArgumentException() {
        Long id = 1L;
        when(arbitroRepository.findById(id)).thenReturn(Optional.of(savedArbitro));

        // Simular um jogo futuro
        Jogo jogoFuturo = new Jogo();
        jogoFuturo.setData(LocalDate.now().plusDays(1)); // Jogo amanhã
        savedArbitro.setJogosOficiados(Arrays.asList(jogoFuturo));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> arbitroHandler.delete(id));

        assertEquals("Árbitro não pode ser eliminado, pois está escalado para jogos futuros.", exception.getMessage());
        verify(arbitroRepository, times(1)).findById(id);
        verify(arbitroRepository, times(0)).deleteById(id); 
    }


// TEST FIND BY ID
    @Test
    void findById_ExistingId_ReturnsArbitroDTO() {
        when(arbitroRepository.findById(1L)).thenReturn(Optional.of(savedArbitro));

        ArbitroDTO result = arbitroHandler.findById(1L);

        assertNotNull(result);
        assertEquals(savedArbitro.getId(), result.getId());
        verify(arbitroRepository, times(1)).findById(1L);
    }

    @Test
    void findById_NonExistingId_ReturnsNull() {
        when(arbitroRepository.findById(100L)).thenReturn(Optional.empty());

        ArbitroDTO result = arbitroHandler.findById(100L);

        assertNull(result);
        verify(arbitroRepository, times(1)).findById(100L);
    }


// TEST FIND ALL
    @Test
    void findAll_ExistingArbitros_ReturnsListOfArbitroDTOs() {
        Arbitro savedArbitro2 = createArbitroComId(2L, "João", true, "joao", "password123");


        List<Arbitro> arbitros = Arrays.asList(savedArbitro , savedArbitro2);
        when(arbitroRepository.findAll()).thenReturn(arbitros);


        List<ArbitroDTO> results = arbitroHandler.findAll();

        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals(savedArbitro.getId(), results.get(0).getId());
        assertEquals(savedArbitro2.getId(), results.get(1).getId());
        verify(arbitroRepository, times(1)).findAll();
    }

    @Test
    void findAll_NoArbitros_ReturnsEmptyList() {
        when(arbitroRepository.findAll()).thenReturn(Arrays.asList());

        List<ArbitroDTO> results = arbitroHandler.findAll();

        assertNotNull(results);
        assertTrue(results.isEmpty());
        verify(arbitroRepository, times(1)).findAll();
    }



//auxiliar 
    private Arbitro createArbitroComId(Long id, String nome, boolean certificated, String username, String password) {
        Arbitro arbitro = new Arbitro(nome, certificated, username, password);
        try {
            Field idField = Utilizador.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(arbitro, id);
        } catch (Exception e) {
            fail("Falha ao definir ID");
        }
        return arbitro;
    }
}
