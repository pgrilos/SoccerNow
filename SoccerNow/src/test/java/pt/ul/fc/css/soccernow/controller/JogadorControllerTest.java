package pt.ul.fc.css.soccernow.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import pt.ul.fc.css.soccernow.dto.JogadorDTO;
import pt.ul.fc.css.soccernow.handlers.JogadorHandler;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(JogadorController.class)
public class JogadorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JogadorHandler jogadorService;

    JogadorDTO jogador;

     @BeforeEach
    void setUp(){
        jogador = new JogadorDTO(1L,"João Silva", "Atacante", "joaosilva", "senha123");

    }

//TESTE LISTAR TODOS OS JOGADORES
    @Test
    public void listarJogadores_DeveRetornarListaDeJogadores() throws Exception {
        JogadorDTO jogador2 = new JogadorDTO(2L,"Ronaldo", "Atacante", "cr7", "cr7");

        when(jogadorService.findAll()).thenReturn(Arrays.asList(jogador, jogador2));

        mockMvc.perform(get("/api/jogadores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("João Silva"))
                .andExpect(jsonPath("$[0].posicao").value("Atacante"))
                .andExpect(jsonPath("$[1].nome").value("Ronaldo"))
                .andExpect(jsonPath("$[1].posicao").value("Atacante"));
    }

    @Test
    public void listarJogadores_QuandoNaoHaJogadores_DeveRetornarNotFound() throws Exception {
        when(jogadorService.findAll()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/jogadores"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Nenhum jogador encontrado"));
    }


//TESTE REGISTRAR
    @Test
    public void registrarJogador_DeveCriarJogador() throws Exception {
        when(jogadorService.create(Mockito.any(JogadorDTO.class))).thenReturn(jogador);

        mockMvc.perform(post("/api/jogadores/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"João Silva\",\"username\":\"joaosilva\",\"password\":\"senha123\",\"posicao\":\"Atacante\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.posicao").value("Atacante"));
    }

    @Test
    public void registrarJogador_NomeNulo_DeveRetornarBadRequest() throws Exception {
        when(jogadorService.create(Mockito.any(JogadorDTO.class)))
                .thenThrow(new IllegalArgumentException("Nome é obrigatório"));

        mockMvc.perform(post("/api/jogadores/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":null,\"username\":\"joaosilva\",\"password\":\"senha123\",\"posicao\":\"Atacante\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Nome é obrigatório"));
    }

    @Test
    public void registrarJogador_NomeVazio_DeveRetornarBadRequest() throws Exception {
        when(jogadorService.create(Mockito.any(JogadorDTO.class)))
                .thenThrow(new IllegalArgumentException("Nome é obrigatório"));

        mockMvc.perform(post("/api/jogadores/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"\",\"username\":\"joaosilva\",\"password\":\"senha123\",\"posicao\":\"Atacante\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Nome é obrigatório"));
    }

    @Test
    public void registrarJogador_UsernameNulo_DeveRetornarBadRequest() throws Exception {
        // Arrange
        when(jogadorService.create(Mockito.any(JogadorDTO.class)))
            .thenThrow(new IllegalArgumentException("Username é obrigatório"));
        
        // Act & Assert
        mockMvc.perform(post("/api/jogadores/registro")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"nome\":\"João Silva\",\"posicao\":\"Atacante\",\"username\":null,\"password\":\"senha123\"}"))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Username é obrigatório"));
    }

    @Test
    public void registrarJogador_UsernameVazio_DeveRetornarBadRequest() throws Exception {
        // Arrange
        when(jogadorService.create(Mockito.any(JogadorDTO.class)))
            .thenThrow(new IllegalArgumentException("Username é obrigatório"));
        
        // Act & Assert
        mockMvc.perform(post("/api/jogadores/registro")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"nome\":\"João Silva\",\"posicao\":\"Atacante\",\"username\":\"\",\"password\":\"senha123\"}"))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Username é obrigatório"));
    }

    @Test
    public void registrarJogador_UsernameDuplicado_DeveRetornarBadRequest() throws Exception {
        when(jogadorService.create(Mockito.any(JogadorDTO.class)))
                .thenThrow(new IllegalArgumentException("Username já existe"));

        mockMvc.perform(post("/api/jogadores/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"João Silva\",\"username\":\"joaosilva\",\"password\":\"senha123\",\"posicao\":\"Atacante\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username já existe"));
    }

    @Test
    public void registrarJogador_PosicaoNula_DeveRetornarBadRequest() throws Exception {
        when(jogadorService.create(Mockito.any(JogadorDTO.class)))
            .thenThrow(new IllegalArgumentException("Posição é obrigatória"));
        
        mockMvc.perform(post("/api/jogadores/registro")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"nome\":\"João Silva\",\"posicao\":null,\"username\":\"joaosilva\",\"password\":\"senha123\"}"))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Posição é obrigatória"));
    }

    @Test
    public void registrarJogador_PosicaoVazia_DeveRetornarBadRequest() throws Exception {
        when(jogadorService.create(Mockito.any(JogadorDTO.class)))
            .thenThrow(new IllegalArgumentException("Posição é obrigatória"));
        
        mockMvc.perform(post("/api/jogadores/registro")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"nome\":\"João Silva\",\"posicao\":\"\",\"username\":\"joaosilva\",\"password\":\"senha123\"}"))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Posição é obrigatória"));
    }

    @Test
    public void registrarJogador_PasswordNula_DeveRetornarBadRequest() throws Exception {
        when(jogadorService.create(Mockito.any(JogadorDTO.class)))
            .thenThrow(new IllegalArgumentException("Password é obrigatória"));
        
        mockMvc.perform(post("/api/jogadores/registro")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"nome\":\"João Silva\",\"posicao\":\"Atacante\",\"username\":\"joaosilva\",\"password\":null}"))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Password é obrigatória"));
    }

    @Test
    public void registrarJogador_PasswordVazia_DeveRetornarBadRequest() throws Exception {
        when(jogadorService.create(Mockito.any(JogadorDTO.class)))
            .thenThrow(new IllegalArgumentException("Password é obrigatória"));
        
        mockMvc.perform(post("/api/jogadores/registro")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"nome\":\"João Silva\",\"posicao\":\"Atacante\",\"username\":\"joaosilva\",\"password\":\"\"}"))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Password é obrigatória"));
    }

//TESTE ATUALIZAR 
    @Test
    public void atualizarJogador_DeveAtualizarJogador() throws Exception {
        JogadorDTO jogadorAtualizado = new JogadorDTO(1L, "João Silva", "Meio-Campo", "joaosilva", "novaSenha123");
        
        when(jogadorService.update(eq(1L), Mockito.any(JogadorDTO.class))).thenReturn(jogadorAtualizado);

        mockMvc.perform(put("/api/jogadores/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"João Silva\",\"posicao\":\"Meio-Campo\",\"username\":\"joaosilva\",\"password\":\"novaSenha123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.posicao").value("Meio-Campo"))
                .andExpect(jsonPath("$.username").value("joaosilva"));
    }

    @Test
    public void atualizarJogador_NomeNulo_DeveRetornarBadRequest() throws Exception {
        when(jogadorService.update(eq(1L), Mockito.any(JogadorDTO.class)))
                .thenThrow(new IllegalArgumentException("Nome é obrigatório"));

        mockMvc.perform(put("/api/jogadores/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":null,\"posicao\":\"Atacante\",\"username\":\"joaosilva\",\"password\":\"senha123\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Nome é obrigatório"));
    }

    @Test
    public void atualizarJogador_NomeVazio_DeveRetornarBadRequest() throws Exception {
        when(jogadorService.update(eq(1L), Mockito.any(JogadorDTO.class)))
                .thenThrow(new IllegalArgumentException("Nome é obrigatório"));

        mockMvc.perform(put("/api/jogadores/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"\",\"posicao\":\"Atacante\",\"username\":\"joaosilva\",\"password\":\"senha123\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Nome é obrigatório"));
    }

    @Test
    public void atualizarJogador_PosicaoNula_DeveRetornarBadRequest() throws Exception {
        when(jogadorService.update(eq(1L), Mockito.any(JogadorDTO.class)))
                .thenThrow(new IllegalArgumentException("Posição é obrigatória"));

        mockMvc.perform(put("/api/jogadores/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"João Silva\",\"posicao\":null,\"username\":\"joaosilva\",\"password\":\"senha123\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Posição é obrigatória"));
    }

    @Test
    public void atualizarJogador_PosicaoVazia_DeveRetornarBadRequest() throws Exception {
        when(jogadorService.update(eq(1L), Mockito.any(JogadorDTO.class)))
                .thenThrow(new IllegalArgumentException("Posição é obrigatória"));

        mockMvc.perform(put("/api/jogadores/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"João Silva\",\"posicao\":\"\",\"username\":\"joaosilva\",\"password\":\"senha123\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Posição é obrigatória"));
    }

    @Test
    public void atualizarJogador_UsernameNulo_DeveRetornarBadRequest() throws Exception {
        when(jogadorService.update(eq(1L), Mockito.any(JogadorDTO.class)))
                .thenThrow(new IllegalArgumentException("Username é obrigatório"));

        mockMvc.perform(put("/api/jogadores/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"João Silva\",\"posicao\":\"Atacante\",\"username\":null,\"password\":\"senha123\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username é obrigatório"));
    }

    @Test
    public void atualizarJogador_UsernameVazio_DeveRetornarBadRequest() throws Exception {
        when(jogadorService.update(eq(1L), Mockito.any(JogadorDTO.class)))
                .thenThrow(new IllegalArgumentException("Username é obrigatório"));

        mockMvc.perform(put("/api/jogadores/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"João Silva\",\"posicao\":\"Atacante\",\"username\":\"\",\"password\":\"senha123\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username é obrigatório"));
    }

    @Test
    public void atualizarJogador_UsernameExistente_DeveRetornarBadRequest() throws Exception {
        when(jogadorService.update(eq(1L), Mockito.any(JogadorDTO.class)))
                .thenThrow(new IllegalArgumentException("Username já existe"));

        mockMvc.perform(put("/api/jogadores/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"João Silva\",\"posicao\":\"Atacante\",\"username\":\"joaosilva\",\"password\":\"senha123\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username já existe"));
    }

    @Test
    public void atualizarJogador_PasswordNula_DeveRetornarBadRequest() throws Exception {
        when(jogadorService.update(eq(1L), Mockito.any(JogadorDTO.class)))
                .thenThrow(new IllegalArgumentException("Password é obrigatória"));

        mockMvc.perform(put("/api/jogadores/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"João Silva\",\"posicao\":\"Atacante\",\"username\":\"joaosilva\",\"password\":null}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Password é obrigatória"));
    }

    @Test
    public void atualizarJogador_PasswordVazia_DeveRetornarBadRequest() throws Exception {
        when(jogadorService.update(eq(1L), Mockito.any(JogadorDTO.class)))
                .thenThrow(new IllegalArgumentException("Password é obrigatória"));

        mockMvc.perform(put("/api/jogadores/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"João Silva\",\"posicao\":\"Atacante\",\"username\":\"joaosilva\",\"password\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Password é obrigatória"));
    }
    
    @Test
    public void atualizarJogador_IdInvalido_DeveRetornarBadRequest() throws Exception {
        when(jogadorService.update(eq(99L), Mockito.any(JogadorDTO.class)))
                .thenThrow(new IllegalArgumentException("Jogador não encontrado"));

        mockMvc.perform(put("/api/jogadores/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"João Silva\",\"posicao\":\"Atacante\",\"username\":\"joaosilva\",\"password\":\"senha123\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Jogador não encontrado"));
    }


//TESTE ELIMINAR 
    @Test
    public void eliminarJogador_DeveDeletarJogador() throws Exception {

        when((jogadorService).delete(1L)).thenReturn(jogador);

        mockMvc.perform(delete("/api/jogadores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.posicao").value("Atacante"));
    }

    @Test
    public void eliminarJogador_IdInvalido_DeveRetornarNotFound() throws Exception {
        doThrow(new IllegalArgumentException("Jogador não encontrado"))
                .when(jogadorService).delete(99L);

        mockMvc.perform(delete("/api/jogadores/99"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Jogador não encontrado"));
    }


//TESTE BUSCAR JOGADOR POR NOME
    @Test
    public void buscarPorNome_DeveRetornarJogador() throws Exception {
        JogadorDTO jogador2 = new JogadorDTO(2L,"João Silva", "Defesa", "joaosilva2", "senha123");

        when(jogadorService.findByName("João Silva")).thenReturn(Arrays.asList(jogador,jogador2));

        mockMvc.perform(get("/api/jogadores/buscar/nome/")
                .param("nome", "João Silva"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("João Silva"))
                .andExpect(jsonPath("$[0].posicao").value("Atacante"))
                .andExpect(jsonPath("$[1].nome").value("João Silva"))
                .andExpect(jsonPath("$[1].posicao").value("Defesa")); 
    }

    @Test
    public void buscarPorNome_SemResultados_DeveRetornarNotFound() throws Exception {
        when(jogadorService.findByName("Inexistente")).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/jogadores/buscar/nome/")
                .param("nome", "Inexistente")) 
                .andExpect(status().isNotFound())
                .andExpect(content().string("Nenhum jogador encontrado com o nome: Inexistente"));
    }

}