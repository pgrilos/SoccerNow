package pt.ul.fc.css.soccernow.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pt.ul.fc.css.soccernow.dto.ArbitroDTO;
import pt.ul.fc.css.soccernow.handlers.ArbitroHandler;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ArbitroController.class)
public class ArbitroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArbitroHandler arbitroHandler;

    private ArbitroDTO arbitro;


    @BeforeEach
    void setUp(){
        // Cria o dto Arbitro
        arbitro = new ArbitroDTO(1L,"Carlos Pereira", false, "carlos", "senha123");
    }

//TESTE LISTAR TODOS OS ARBITROS
    @Test
    void listarArbitros_DeveRetornarListaDeArbitros() throws Exception {
        ArbitroDTO arbitro2 = new ArbitroDTO(2L, "Joana Silva", true, "joana", "senha456");
        // Configura o mock
        when(arbitroHandler.findAll()).thenReturn(Arrays.asList(arbitro, arbitro2));

        // Simula a requisição GET
        mockMvc.perform(get("/api/arbitros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Carlos Pereira"))
                .andExpect(jsonPath("$[0].isCertificated").value(false))
                .andExpect(jsonPath("$[1].nome").value("Joana Silva"))
                .andExpect(jsonPath("$[1].isCertificated").value(true));
    }

    @Test
    void listarArbitros_QuandoNaoHaArbitros_DeveRetornarNotFound() throws Exception {
        when(arbitroHandler.findAll()).thenReturn(Arrays.asList());
        
        mockMvc.perform(get("/api/arbitros"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Nenhum árbitro encontrado"));
    }
    
//TESTE REGISTRAR
    @Test
    void registrarArbitro_DeveCriarArbitro() throws Exception {
        when(arbitroHandler.create(Mockito.any(ArbitroDTO.class))).thenReturn(arbitro);

        mockMvc.perform(post("/api/arbitros/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Carlos Pereira\",\"username\":\"carlos\",\"password\":\"senha123\",\"certificated\":false}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Carlos Pereira"))
                .andExpect(jsonPath("$.isCertificated").value(false));
    }

    @Test
    public void registrarArbitro_NomeNulo_DeveRetornarBadRequest() throws Exception {
        // Arrange
        when(arbitroHandler.create(Mockito.any(ArbitroDTO.class)))
                .thenThrow(new IllegalArgumentException("Nome é obrigatório"));

        // Act & Assert
        mockMvc.perform(post("/api/arbitros/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":null,\"username\":\"carlos\",\"password\":\"senha123\",\"certificated\":false}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Nome é obrigatório"));
    }

    @Test
    public void registrarArbitro_NomeVazio_DeveRetornarBadRequest() throws Exception {
    // Arrange
    when(arbitroHandler.create(Mockito.any(ArbitroDTO.class)))
    .thenThrow(new IllegalArgumentException("Nome é obrigatório"));

    // Act & Assert
    mockMvc.perform(post("/api/arbitros/registro")
    .contentType(MediaType.APPLICATION_JSON)
    .content("{\"nome\":\"\",\"username\":\"carlos\",\"password\":\"senha123\",\"certificated\":false}")) // Nome é ""
    .andExpect(status().isBadRequest())
    .andExpect(content().string("Nome é obrigatório"));
    }

    @Test
    public void registrarArbitro_UsernameNulo_DeveRetornarBadRequest() throws Exception {
        // Arrange
        when(arbitroHandler.create(Mockito.any(ArbitroDTO.class)))
        .thenThrow(new IllegalArgumentException("Username é obrigatório"));
    
        // Act & Assert
        mockMvc.perform(post("/api/arbitros/registro")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"nome\":\"Carlos Pereira\",\"username\":null,\"password\":\"senha123\",\"certificated\":false}"))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Username é obrigatório"));
    }

    @Test
    public void registrarArbitro_UsernameVazio_DeveRetornarBadRequest() throws Exception {
        // Arrange
        when(arbitroHandler.create(Mockito.any(ArbitroDTO.class)))
        .thenThrow(new IllegalArgumentException("Username é obrigatório"));

        // Act & Assert
        mockMvc.perform(post("/api/arbitros/registro")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"nome\":\"Carlos Pereira\",\"username\":\"\",\"password\":\"senha123\",\"certificated\":false}"))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Username é obrigatório"));
    }

    @Test
    public void registrarArbitro_UsernameDuplicado_DeveRetornarBadRequest() throws Exception {
        // Arrange
        when(arbitroHandler.create(Mockito.any(ArbitroDTO.class)))
                .thenThrow(new IllegalArgumentException("Username já existe"));

        // Act & Assert
        mockMvc.perform(post("/api/arbitros/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Carlos Pereira\",\"username\":\"carlos\",\"password\":\"senha123\",\"certificated\":false}" ))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username já existe"));
    }

    @Test
    public void registrarArbitro_PasswordNulo_DeveRetornarBadRequest() throws Exception {
        // Arrange
        when(arbitroHandler.create(Mockito.any(ArbitroDTO.class)))
        .thenThrow(new IllegalArgumentException("Password é obrigatória"));
    
        // Act & Assert
        mockMvc.perform(post("/api/arbitros/registro")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"nome\":\"Carlos Pereira\",\"username\":\"carlos\",\"password\":null,\"certificated\":false}"))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Password é obrigatória"));
    }

    @Test
    public void registrarArbitro_PasswordVazio_DeveRetornarBadRequest() throws Exception {
        // Arrange
        when(arbitroHandler.create(Mockito.any(ArbitroDTO.class)))
        .thenThrow(new IllegalArgumentException("Password é obrigatória"));

        // Act & Assert
        mockMvc.perform(post("/api/arbitros/registro")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"nome\":\"Carlos Pereira\",\"username\":\"carlos\",\"password\":\"\",\"certificated\":false}"))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Password é obrigatória"));
    }


//TESTE ATUALIZAR
    @Test
    void atualizarArbitro_DeveAtualizarArbitro() throws Exception {
        when(arbitroHandler.update(eq(1L), Mockito.any(ArbitroDTO.class))).thenReturn(arbitro);

        mockMvc.perform(put("/api/arbitros/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Carlos Pereira\",\"username\":\"carlos\",\"password\":\"senha123\",\"certificated\":false}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Carlos Pereira"))
                .andExpect(jsonPath("$.isCertificated").value(false));
    }

    @Test
    public void atualizarArbitro_NomeNulo_DeveRetornarBadRequest() throws Exception {
        // Arrange
        when(arbitroHandler.update(eq(1L), Mockito.any(ArbitroDTO.class)))
        .thenThrow(new IllegalArgumentException("Nome é obrigatório"));

        // Act & Assert
        mockMvc.perform(put("/api/arbitros/{id}", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"nome\":null,\"username\":\"carlos\",\"password\":\"senha123\",\"certificated\":false}"))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Nome é obrigatório"));
    }

    @Test
    public void atualizarArbitro_NomeVazio_DeveRetornarBadRequest() throws Exception {
        // Arrange
        when(arbitroHandler.update(eq(1L), Mockito.any(ArbitroDTO.class)))
        .thenThrow(new IllegalArgumentException("Nome é obrigatório"));

        // Act & Assert
        mockMvc.perform(put("/api/arbitros/{id}", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"nome\":\"\",\"username\":\"carlos\",\"password\":\"senha123\",\"certificated\":false}"))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Nome é obrigatório"));
    }

    @Test
    public void atualizarArbitro_UsernameNulo_DeveRetornarBadRequest() throws Exception {
        // Arrange
        when(arbitroHandler.update(eq(1L), Mockito.any(ArbitroDTO.class)))
        .thenThrow(new IllegalArgumentException("Username é obrigatório"));

        // Act & Assert
        mockMvc.perform(put("/api/arbitros/{id}", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"nome\":\"Carlos Pereira\",\"username\":null,\"password\":\"senha123\",\"certificated\":false}"))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Username é obrigatório"));
    }

    @Test
    public void atualizarArbitro_UsernameVazio_DeveRetornarBadRequest() throws Exception {
        // Arrange
        when(arbitroHandler.update(eq(1L), Mockito.any(ArbitroDTO.class)))
        .thenThrow(new IllegalArgumentException("Username é obrigatório"));

        // Act & Assert
        mockMvc.perform(put("/api/arbitros/{id}", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"nome\":\"Carlos Pereira\",\"username\":\"\",\"password\":\"senha123\",\"certificated\":false}"))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Username é obrigatório"));
    }

    @Test
    public void atualizarArbitro_UsernameExistente_DeveRetornarBadRequest() throws Exception {
        // Arrange
        when(arbitroHandler.update(eq(1L), Mockito.any(ArbitroDTO.class)))
                .thenThrow(new IllegalArgumentException("Username já existe"));

        // Act & Assert
        mockMvc.perform(put("/api/arbitros/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Novo Nome\",\"username\":\"carlos\",\"password\":\"novaSenha\",\"certificated\":true}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username já existe"));
    }

    @Test
    public void atualizarArbitro_PasswordNulo_DeveRetornarBadRequest() throws Exception {
        // Arrange
        when(arbitroHandler.update(eq(1L), Mockito.any(ArbitroDTO.class)))
        .thenThrow(new IllegalArgumentException("Password é obrigatória"));

        // Act & Assert
        mockMvc.perform(put("/api/arbitros/{id}", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"nome\":\"Carlos Pereira\",\"username\":\"carlos\",\"password\":null,\"certificated\":false}"))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Password é obrigatória"));
    }

    @Test
    public void atualizarArbitro_PasswordVazio_DeveRetornarBadRequest() throws Exception {
        // Arrange
        when(arbitroHandler.update(eq(1L), Mockito.any(ArbitroDTO.class)))
        .thenThrow(new IllegalArgumentException("Password é obrigatória"));

        // Act & Assert
        mockMvc.perform(put("/api/arbitros/{id}", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"nome\":\"Carlos Pereira\",\"username\":\"carlos\",\"password\":\"\",\"certificated\":false}"))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Password é obrigatória"));
    }

    @Test
    public void atualizarArbitro_IdInvalido_DeveRetornarBadRequest() throws Exception {
        when(arbitroHandler.update(eq(1L), Mockito.any(ArbitroDTO.class)))
        .thenThrow(new IllegalArgumentException("Árbitro não encontrado")); // Mensagem de erro específica para ID não encontrado

        mockMvc.perform(put("/api/arbitros/{id}", 1L)  //Envia a requisição com o ID inválido
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"nome\":\"Carlos Pereira\",\"username\":\"carlos\",\"password\":\"senha123\",\"certificated\":false}"))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Árbitro não encontrado"));
    }


//TESTE ELIMINAR
    @Test
    void eliminarArbitro_DeveDeletarArbitro() throws Exception {

        when((arbitroHandler).delete(1L)).thenReturn(arbitro);

        // Act & Assert
        mockMvc.perform(delete("/api/arbitros/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Carlos Pereira"))
                .andExpect(jsonPath("$.isCertificated").value(false));
    }

    @Test
    void eliminarArbitro_IdInvalido_DeveRetornarBadRequest() throws Exception {

        when(arbitroHandler.delete(2L)).thenThrow(new IllegalArgumentException("Árbitro não encontrado"));

        // Act & Assert
        mockMvc.perform(delete("/api/arbitros/{id}", 2L))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Árbitro não encontrado"));
    }

    @Test
    void eliminarArbitro_ArbitroComJogosFuturos_DeveRetornarBadRequest() throws Exception {

        when(arbitroHandler.delete(1L)).thenThrow(new IllegalArgumentException("Árbitro não pode ser eliminado, pois está escalado para jogos futuros."));

        // Act & Assert
        mockMvc.perform(delete("/api/arbitros/{id}", 1L))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Árbitro não pode ser eliminado, pois está escalado para jogos futuros."));
    }


}
