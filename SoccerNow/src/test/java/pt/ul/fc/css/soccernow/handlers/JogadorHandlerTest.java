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

import pt.ul.fc.css.soccernow.dominio.Jogador;
import pt.ul.fc.css.soccernow.dominio.Posicao;
import pt.ul.fc.css.soccernow.dominio.Utilizador;
import pt.ul.fc.css.soccernow.dto.JogadorDTO;
import pt.ul.fc.css.soccernow.repositorio.JogadorRepository;

@ExtendWith(MockitoExtension.class)
public class JogadorHandlerTest {

    @Mock
    private JogadorRepository jogadorRepository;

    @InjectMocks
    private JogadorHandler jogadorHandler;

    private JogadorDTO jogadorDTO;
    private Jogador savedJogador;

    @BeforeEach
    void setUp() {
        jogadorDTO = new JogadorDTO();
        jogadorDTO.setNome("Carlos Silva");
        jogadorDTO.setUsername("carlossilva");
        jogadorDTO.setPassword("password123");
        jogadorDTO.setPosicao("ALA");
        Posicao posicao = Posicao.valueOf("ALA");

        savedJogador = createJogadorComId(1L, "Carlos Silva", "carlossilva", "password123", posicao);
    }

    // TEST CREATE
    @Test
    void testCreateJogador() {
        when(jogadorRepository.save(any(Jogador.class))).thenReturn(savedJogador);

        JogadorDTO result = jogadorHandler.create(jogadorDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Carlos Silva", result.getNome());
        assertEquals("carlossilva", result.getUsername());
        assertEquals("password123", result.getPassword());
        assertEquals("ALA", result.getPosicao());
        verify(jogadorRepository, times(1)).save(any(Jogador.class));
    }

    @Test
    void testCreateJogador_NomeNulo_ThrowsIllegalArgumentException() {
        jogadorDTO.setNome(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> jogadorHandler.create(jogadorDTO));

        assertEquals("Nome é obrigatório", exception.getMessage());
        verify(jogadorRepository, times(0)).save(any(Jogador.class));
    }

    @Test
    void testCreateJogador_NomeVazio_ThrowsIllegalArgumentException() {
        jogadorDTO.setNome("");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> jogadorHandler.create(jogadorDTO));

        assertEquals("Nome é obrigatório", exception.getMessage());
        verify(jogadorRepository, times(0)).save(any(Jogador.class));
    }

    @Test
    void testCreateJogador_UsernameNulo_ThrowsIllegalArgumentException() {
        // Arrange
        jogadorDTO.setUsername(null);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> jogadorHandler.create(jogadorDTO));
        assertEquals("Username é obrigatório", exception.getMessage());
        verify(jogadorRepository, times(0)).save(any(Jogador.class));
    }


    @Test
    void testCreateJogador_UsernameVazio_ThrowsIllegalArgumentException() {
        // Arrange
        jogadorDTO.setUsername("");

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> jogadorHandler.create(jogadorDTO));
        assertEquals("Username é obrigatório", exception.getMessage());
        verify(jogadorRepository, times(0)).save(any(Jogador.class));
    }

    @Test
    void testCreateJogador_PasswordNulo_ThrowsIllegalArgumentException() {
        // Arrange
        jogadorDTO.setPassword(null);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> jogadorHandler.create(jogadorDTO));
        assertEquals("Password é obrigatória", exception.getMessage());
        verify(jogadorRepository, times(0)).save(any(Jogador.class));
    }

    @Test
    void testCreateJogador_PasswordVazio_ThrowsIllegalArgumentException() {
        // Arrange
        jogadorDTO.setPassword("");

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> jogadorHandler.create(jogadorDTO));
        assertEquals("Password é obrigatória", exception.getMessage());
        verify(jogadorRepository, times(0)).save(any(Jogador.class));
    }
    
    @Test
    void testCreateJogador_PosicaoNula_ThrowsIllegalArgumentException() {
        // Arrange
        jogadorDTO.setPosicao(null);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> jogadorHandler.create(jogadorDTO));
        assertEquals("Posição é obrigatória", exception.getMessage());
        verify(jogadorRepository, times(0)).save(any(Jogador.class));
    }

    @Test
    void testCreateJogador_PosicaoVazia_ThrowsIllegalArgumentException() {
        // Arrange
        jogadorDTO.setPosicao("");

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> jogadorHandler.create(jogadorDTO));
        assertEquals("Posição é obrigatória", exception.getMessage());
        verify(jogadorRepository, times(0)).save(any(Jogador.class));
    }

    @Test
    void testCreateJogador_UsernameExistente_ThrowsIllegalArgumentException() {
        when(jogadorRepository.findByUsername(jogadorDTO.getUsername())).thenReturn(savedJogador);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> jogadorHandler.create(jogadorDTO));

        assertEquals("Username já existe", exception.getMessage());
        verify(jogadorRepository, times(1)).findByUsername(jogadorDTO.getUsername());
    }

    // TEST UPDATE
    @Test
    void testUpdateJogador() {
        Long id = 1L;

        JogadorDTO jogadorDTOAtualizado = new JogadorDTO();
        jogadorDTOAtualizado.setNome("Carlos Silva Atualizado");
        jogadorDTOAtualizado.setUsername("carlossilva");
        jogadorDTOAtualizado.setPassword("newpassword123");
        jogadorDTOAtualizado.setPosicao("FIXO");
        
        Posicao posicao = Posicao.valueOf("FIXO");
        Jogador jogadorAtualizado = createJogadorComId(id, "Carlos Silva Atualizado", "carlossilva", "newpassword123", posicao);

        when(jogadorRepository.findById(id)).thenReturn(Optional.of(savedJogador));
        when(jogadorRepository.save(any(Jogador.class))).thenReturn(jogadorAtualizado);

        JogadorDTO result = jogadorHandler.update(id, jogadorDTOAtualizado);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Carlos Silva Atualizado", result.getNome());
        assertEquals("carlossilva", result.getUsername());
        assertEquals("newpassword123", result.getPassword());
        assertEquals("FIXO", result.getPosicao());
        verify(jogadorRepository, times(1)).findById(id);
        verify(jogadorRepository, times(1)).save(any(Jogador.class));
    }

    @Test
    void testUpdateJogador_NomeNulo_ThrowsIllegalArgumentException() {
        // Arrange
        Long id = 1L;
        jogadorDTO.setNome(null);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> jogadorHandler.update(id, jogadorDTO));
        assertEquals("Nome é obrigatório", exception.getMessage());
    }

    @Test
    void testUpdateJogador_NomeVazio_ThrowsIllegalArgumentException() {
        // Arrange
        Long id = 1L;
        jogadorDTO.setNome("");

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> jogadorHandler.update(id, jogadorDTO));
        assertEquals("Nome é obrigatório", exception.getMessage());
    }

    @Test
    void testUpdateJogador_UsernameNulo_ThrowsIllegalArgumentException() {
        // Arrange
        Long id = 1L;
        jogadorDTO.setUsername(null);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> jogadorHandler.update(id, jogadorDTO));
        assertEquals("Username é obrigatório", exception.getMessage());
    }

    @Test
    void testUpdateJogador_UsernameVazio_ThrowsIllegalArgumentException() {
        // Arrange
        Long id = 1L;
        jogadorDTO.setUsername("");

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> jogadorHandler.update(id, jogadorDTO));
        assertEquals("Username é obrigatório", exception.getMessage());
    }

    @Test
    void testUpdateJogador_PasswordNula_ThrowsIllegalArgumentException() {
        // Arrange
        Long id = 1L;
        jogadorDTO.setPassword(null);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> jogadorHandler.update(id, jogadorDTO));
        assertEquals("Password é obrigatória", exception.getMessage());
    }

    @Test
    void testUpdateJogador_PasswordVazia_ThrowsIllegalArgumentException() {
        // Arrange
        Long id = 1L;
        jogadorDTO.setPassword("");

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> jogadorHandler.update(id, jogadorDTO));
        assertEquals("Password é obrigatória", exception.getMessage());
    }
    
    @Test
    void testUpdateJogador_PosicaoNula_ThrowsIllegalArgumentException() {
        // Arrange
        Long id = 1L;
        jogadorDTO.setPosicao(null);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> jogadorHandler.update(id, jogadorDTO));
        assertEquals("Posição é obrigatória", exception.getMessage());
    }

    @Test
    void testUpdateJogador_PosicaoVazia_ThrowsIllegalArgumentException() {
        // Arrange
        Long id = 1L;
        jogadorDTO.setPosicao("");

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> jogadorHandler.update(id, jogadorDTO));
        assertEquals("Posição é obrigatória", exception.getMessage());
    }

    @Test
    void testUpdateJogador_UsernameExistente_ThrowsIllegalArgumentException() {
        // Arrange
        Long id = 1L;
        Posicao posicao = Posicao.valueOf("PIVO");
        Jogador outroJogador = createJogadorComId(2L, "Outro Nome", "outroUsername", "outraPassword", posicao);
        jogadorDTO.setUsername("outroUsername"); // Tenta usar o username de outro jogador
        when(jogadorRepository.findByUsername("outroUsername")).thenReturn(outroJogador);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> jogadorHandler.update(id, jogadorDTO));
        assertEquals("Username já existe", exception.getMessage());
    }

    @Test
    void testUpdateJogador_NonExistingId_ThrowsIllegalArgumentException() {
        Long id = 100L;
        when(jogadorRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> jogadorHandler.update(id, jogadorDTO));

        assertEquals("Jogador não encontrado", exception.getMessage());
        verify(jogadorRepository, times(1)).findById(id);
        verify(jogadorRepository, times(0)).save(any(Jogador.class));
    }

    // TEST DELETE
    @Test
    void testDeleteJogador_ExistingId_ReturnsJogadorDTO() {
        Long id = 1L;
        when(jogadorRepository.findById(id)).thenReturn(Optional.of(savedJogador));

        JogadorDTO result = jogadorHandler.delete(id);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Carlos Silva", result.getNome());
        verify(jogadorRepository, times(1)).findById(id);
        verify(jogadorRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteJogador_NonExistingId_ThrowsIllegalArgumentException() {
        Long id = 100L;
        when(jogadorRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> jogadorHandler.delete(id));

        assertEquals("Jogador não encontrado", exception.getMessage());
        verify(jogadorRepository, times(1)).findById(id);
        verify(jogadorRepository, times(0)).deleteById(anyLong());
    }

    // TEST FIND BY ID
    @Test
    void testFindById_ExistingId_ReturnsJogadorDTO() {
        when(jogadorRepository.findById(1L)).thenReturn(Optional.of(savedJogador));

        JogadorDTO result = jogadorHandler.findById(1L);

        assertNotNull(result);
        assertEquals(savedJogador.getId(), result.getId());
        verify(jogadorRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_NonExistingId_ReturnsNull() {
        when(jogadorRepository.findById(100L)).thenReturn(Optional.empty());

        JogadorDTO result = jogadorHandler.findById(100L);

        assertNull(result);
        verify(jogadorRepository, times(1)).findById(100L);
    }

    // TEST FIND ALL
    @Test
    void testFindAll_ExistingJogadores_ReturnsListOfJogadorDTOs() {
        Posicao posicao = Posicao.valueOf("ALA");
        Jogador savedJogador2 = createJogadorComId(2L, "João", "joao",  "joao", posicao);

        List<Jogador> jogadores = Arrays.asList(savedJogador, savedJogador2);
        when(jogadorRepository.findAll()).thenReturn(jogadores);

        List<JogadorDTO> results = jogadorHandler.findAll();

        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals(savedJogador.getId(), results.get(0).getId());
        assertEquals(savedJogador2.getId(), results.get(1).getId());
        verify(jogadorRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_NoJogadores_ReturnsEmptyList() {
        when(jogadorRepository.findAll()).thenReturn(Arrays.asList());

        List<JogadorDTO> results = jogadorHandler.findAll();

        assertNotNull(results);
        assertTrue(results.isEmpty());
        verify(jogadorRepository, times(1)).findAll();
    }

    // Auxiliar
    private Jogador createJogadorComId(Long id, String nome, String username, String password, Posicao posicao) {
        Jogador jogador = new Jogador(nome, posicao, username, password);
        try {
            Field idField = Utilizador.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(jogador, id);
        } catch (Exception e) {
            fail("Falha ao definir ID");
        }
        return jogador;
    }
}
