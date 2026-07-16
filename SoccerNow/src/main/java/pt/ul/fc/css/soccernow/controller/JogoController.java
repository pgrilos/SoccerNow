package pt.ul.fc.css.soccernow.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pt.ul.fc.css.soccernow.dto.EstatisticasDTO;
import pt.ul.fc.css.soccernow.dto.JogoDTO;
import pt.ul.fc.css.soccernow.handlers.JogoHandler;

@RestController
@RequestMapping("/api/jogo")
public class JogoController {
    
    private final JogoHandler jogoHandler;

    public JogoController(JogoHandler jogoHandler) {
        this.jogoHandler = jogoHandler;
    }

    @GetMapping
    public ResponseEntity<?> listarJogoResponseEntity() {
        try {
            return ResponseEntity.ok(jogoHandler.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/registro")
    public ResponseEntity<?> criarJogo(@RequestBody JogoDTO dto) {
        try {
            return ResponseEntity.ok(jogoHandler.create(dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarResultadoJogo(@PathVariable Long id, @RequestBody EstatisticasDTO dto) {
        try {
            return ResponseEntity.ok(jogoHandler.updateResultado(id, dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelarJogoCampeonato(@PathVariable Long id) {
        try {
            JogoDTO jogoDTO = jogoHandler.cancelarJogoCampeonato(id);
            return ResponseEntity.ok(jogoDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }   
    }
}

