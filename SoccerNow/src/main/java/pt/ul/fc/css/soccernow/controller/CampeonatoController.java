package pt.ul.fc.css.soccernow.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pt.ul.fc.css.soccernow.dto.CampeonatoDTO;
import pt.ul.fc.css.soccernow.handlers.CampeonatoHandler;

@RestController
@RequestMapping("/api/campeonatos")
public class CampeonatoController {

    private final CampeonatoHandler handler;

    public CampeonatoController(CampeonatoHandler handler) {
        this.handler = handler;
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody CampeonatoDTO dto) {
        try {
            CampeonatoDTO camp = handler.criarCampeonato(dto.getNome(), dto.getEquipasNomes());
            return ResponseEntity.ok(camp);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public List<CampeonatoDTO> listar() {
        return handler.listarCampeonatos();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remover(@PathVariable Long id) {
        CampeonatoDTO campeonato = handler.removerCampeonato(id);
        return ResponseEntity.ok(campeonato);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody CampeonatoDTO dto) {
        try {
            CampeonatoDTO camp = handler.atualizarCampeonato(id, dto);
            return ResponseEntity.ok(camp);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/filtro")
    public ResponseEntity<?> filtrar(
        @RequestParam(required = false) String nome,
        @RequestParam(required = false) String equipa,
        @RequestParam(required = false) Integer realizados,
        @RequestParam(required = false) Integer porRealizar
    ) {
        List<CampeonatoDTO> campeonatos = handler.filtrarCampeonatos(nome, equipa, realizados, porRealizar);
        return ResponseEntity.ok(campeonatos);
    }
}