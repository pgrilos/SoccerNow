package pt.ul.fc.css.soccernow.controller;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import pt.ul.fc.css.soccernow.dto.EquipaDTO;
import pt.ul.fc.css.soccernow.handlers.EquipaHandler;

@WebMvcTest(EquipaController.class)
public class EquipaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EquipaHandler equipaHandler;

    private EquipaDTO equipa;

    @BeforeEach
    void setUp() {
        equipa = new EquipaDTO();
        equipa.setId(1L);
        equipa.setNome("Benfica");
    }

    // TESTE LISTAR TODAS AS EQUIPAS
    @Test
    public void listarEquipas_DeveRetornarListaDeEquipas() throws Exception {
        EquipaDTO equipa2 = new EquipaDTO();
        equipa2.setId(2L);
        equipa2.setNome("Porto");

        when(equipaHandler.findAll()).thenReturn(Arrays.asList(equipa, equipa2));

        mockMvc.perform(get("/api/equipas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Benfica"))
                .andExpect(jsonPath("$[1].nome").value("Porto"));
    }

    // TESTE REGISTRAR
    @Test
    public void registrarEquipa_DeveCriarEquipa() throws Exception {
        when(equipaHandler.create(Mockito.any(EquipaDTO.class))).thenReturn(equipa);

        mockMvc.perform(post("/api/equipas/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Benfica\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Benfica"));
    }

    @Test
    public void registrarEquipa_NomeNulo_DeveRetornarBadRequest() throws Exception {
        when(equipaHandler.create(Mockito.any(EquipaDTO.class)))
                .thenThrow(new IllegalArgumentException("Nome da equipa é obrigatório"));

        mockMvc.perform(post("/api/equipas/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":null}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Nome da equipa é obrigatório"));
    }

    @Test
    public void registrarEquipa_NomeVazio_DeveRetornarBadRequest() throws Exception {
        when(equipaHandler.create(Mockito.any(EquipaDTO.class)))
                .thenThrow(new IllegalArgumentException("Nome da equipa é obrigatório"));

        mockMvc.perform(post("/api/equipas/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Nome da equipa é obrigatório"));
    }

    @Test
    public void registrarEquipa_NomeExistente_DeveRetornarBadRequest() throws Exception {
        when(equipaHandler.create(Mockito.any(EquipaDTO.class)))
                .thenThrow(new IllegalArgumentException("Já existe uma equipa com este nome"));

        mockMvc.perform(post("/api/equipas/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Benfica\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Já existe uma equipa com este nome"));
    }

    // TESTE ATUALIZAR
    @Test
    public void atualizarEquipa_DeveAtualizarEquipa() throws Exception {
        EquipaDTO equipaAtualizada = new EquipaDTO();
        equipaAtualizada.setId(1L);
        equipaAtualizada.setNome("Benfica 2024");

        when(equipaHandler.update(eq(1L), Mockito.any(EquipaDTO.class))).thenReturn(equipaAtualizada);

        mockMvc.perform(put("/api/equipas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Benfica 2024\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Benfica 2024"));
    }

    @Test
    public void atualizarEquipa_NomeNulo_DeveRetornarBadRequest() throws Exception {
        when(equipaHandler.update(eq(1L), Mockito.any(EquipaDTO.class)))
                .thenThrow(new IllegalArgumentException("Nome da equipa é obrigatório"));

        mockMvc.perform(put("/api/equipas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":null}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Nome da equipa é obrigatório"));
    }

    @Test
    public void atualizarEquipa_IdInvalido_DeveRetornarBadRequest() throws Exception {
        when(equipaHandler.update(eq(99L), Mockito.any(EquipaDTO.class)))
                .thenThrow(new IllegalArgumentException("Equipa não encontrada"));

        mockMvc.perform(put("/api/equipas/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Benfica\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Equipa não encontrada"));
    }

    // TESTE ELIMINAR
    @Test
    public void eliminarEquipa_DeveDeletarEquipa() throws Exception {
        when(equipaHandler.delete(1L)).thenReturn(equipa);

        mockMvc.perform(delete("/api/equipas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Benfica"));
    }

    @Test
    public void eliminarEquipa_IdInvalido_DeveRetornarBadRequest() throws Exception {
        when(equipaHandler.delete(99L))
                .thenThrow(new IllegalArgumentException("Equipa não encontrada"));

        mockMvc.perform(delete("/api/equipas/99"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Equipa não encontrada"));
    }

    @Test
    public void eliminarEquipa_ComJogosFuturos_DeveRetornarConflict() throws Exception {
        when(equipaHandler.delete(1L))
                .thenThrow(new IllegalStateException("Não é possível remover uma equipa com jogos futuros marcados"));

        mockMvc.perform(delete("/api/equipas/1"))
                .andExpect(status().isConflict())
                .andExpect(content().string("Não é possível remover uma equipa com jogos futuros marcados"));
    }

    // TESTE BUSCAR EQUIPA POR NOME
    @Test
    public void buscarPorNome_DeveRetornarEquipa() throws Exception {
        when(equipaHandler.findByNome("Benfica")).thenReturn(equipa);

        mockMvc.perform(get("/api/equipas/buscar/nome")
                .param("nome", "Benfica"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Benfica"));
    }

    @Test
    public void buscarPorNome_EquipaNaoEncontrada_DeveRetornarNotFound() throws Exception {
        when(equipaHandler.findByNome("Inexistente"))
                .thenThrow(new IllegalArgumentException("Equipa não encontrada"));

        mockMvc.perform(get("/api/equipas/buscar/nome")
                .param("nome", "Inexistente"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Equipa não encontrada"));
    }
}