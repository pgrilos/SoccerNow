package pt.ul.fc.css.soccernow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import pt.ul.fc.css.soccernow.dto.CartaoDTO;
import pt.ul.fc.css.soccernow.dto.EquipaJogoDTO;
import pt.ul.fc.css.soccernow.dto.EstatisticasDTO;
import pt.ul.fc.css.soccernow.dto.JogoDTO;
import pt.ul.fc.css.soccernow.dto.LocalizacaoDTO;
import pt.ul.fc.css.soccernow.handlers.JogoHandler;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(JogoController.class)
public class JogoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JogoHandler jogoHandler;

    private JogoDTO jogoDTO;
    private EstatisticasDTO estatisticasDTO;

    @BeforeEach
    void setUp() {
        jogoDTO = new JogoDTO();
        LocalizacaoDTO locDTO = new LocalizacaoDTO();
        locDTO.setRua("Rua Exemplo");
        locDTO.setCidade("Cidade Exemplo");
                
        EquipaJogoDTO equipaJogoCasaDTO = new EquipaJogoDTO();
        equipaJogoCasaDTO.setEquipa("Equipa A");
        equipaJogoCasaDTO.setJogadoresParticipantes(Arrays.asList("jogadorCasa1", "jogadorCasa2"));

        EquipaJogoDTO equipaJogoVisitanteDTO = new EquipaJogoDTO();
        equipaJogoVisitanteDTO.setEquipa("Equipa B");
        equipaJogoVisitanteDTO.setJogadoresParticipantes(Arrays.asList("jogadorVisitante1"));


        jogoDTO.setId(1L);
        jogoDTO.setData("2025-05-02");
        jogoDTO.setHora("15:00");
        jogoDTO.setLocal(locDTO);
        jogoDTO.setEquipaJogoCasa(equipaJogoCasaDTO);
        jogoDTO.setEquipaJogoVisitante(equipaJogoVisitanteDTO);
        jogoDTO.setArbitroPrincipal("principal");
        jogoDTO.setArbitros(Arrays.asList("principal"));

        estatisticasDTO = new EstatisticasDTO();
        estatisticasDTO.setMarcadoresEquipaCasa(Arrays.asList("jogadorCasa1", "jogadorCasa2"));
        estatisticasDTO.setMarcadoresEquipaVisitante(Arrays.asList("jogadorVisitante1"));
        estatisticasDTO.setCartoes(Arrays.asList(
            new CartaoDTO(1L, "AMARELO", "jogadorCasa1", "Equipa A"),
            new CartaoDTO(2L, "VERMELHO", "jogadorVisitante1", "Equipa B")
        ));
    }

    @Test
    void listarJogos_DeveRetornarListaDeJogos() throws Exception {
        when(jogoHandler.findAll()).thenReturn(Arrays.asList(jogoDTO));

        mockMvc.perform(get("/api/jogo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(jogoDTO.getId()))
                .andExpect(jsonPath("$[0].local.rua").value(jogoDTO.getLocal().getRua()))
                .andExpect(jsonPath("$[0].local.cidade").value(jogoDTO.getLocal().getCidade()));
    }

    @Test
    void listarJogos_QuandoNaoHaJogos_DeveRetornarBadRequest() throws Exception {
        when(jogoHandler.findAll()).thenThrow(new IllegalArgumentException("Nenhum jogo encontrado"));

        mockMvc.perform(get("/api/jogo"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Nenhum jogo encontrado"));
    }

    @Test
    void criarJogo_DeveCriarJogo() throws Exception {
        when(jogoHandler.create(any(JogoDTO.class))).thenReturn(jogoDTO);

        mockMvc.perform(post("/api/jogo/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(jogoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(jogoDTO.getId()))
                .andExpect(jsonPath("$.local.rua").value(jogoDTO.getLocal().getRua()))
                .andExpect(jsonPath("$.local.cidade").value(jogoDTO.getLocal().getCidade()));
    }

    @Test
    void criarJogo_DadosInvalidos_DeveRetornarBadRequest() throws Exception {
        when(jogoHandler.create(any(JogoDTO.class))).thenThrow(new IllegalArgumentException("Dados inválidos"));

        mockMvc.perform(post("/api/jogo/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(jogoDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Dados inválidos"));
    }

    @Test
    void atualizarResultadoJogo_DeveAtualizarResultado() throws Exception {
        when(jogoHandler.updateResultado(eq(1L), any(EstatisticasDTO.class))).thenReturn(estatisticasDTO);

        mockMvc.perform(put("/api/jogo/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(estatisticasDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void atualizarResultadoJogo_JogoNaoEncontrado_DeveRetornarBadRequest() throws Exception {
        when(jogoHandler.updateResultado(eq(1L), any(EstatisticasDTO.class)))
                .thenThrow(new IllegalArgumentException("Jogo não encontrado"));

        mockMvc.perform(put("/api/jogo/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(estatisticasDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Jogo não encontrado"));
    }
}