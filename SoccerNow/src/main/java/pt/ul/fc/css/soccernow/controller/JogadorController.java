package pt.ul.fc.css.soccernow.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pt.ul.fc.css.soccernow.dto.JogadorDTO;
import pt.ul.fc.css.soccernow.handlers.JogadorHandler;

import java.util.List;

@RestController
@RequestMapping("/api/jogadores")
public class JogadorController {
    
    private final JogadorHandler jogadorHandler;

    public JogadorController(JogadorHandler jogadorHandler) {
        this.jogadorHandler = jogadorHandler;
    }

    @GetMapping
    public ResponseEntity<?> listarJogadores() {
       List<JogadorDTO> jogadores = jogadorHandler.findAll();
        if (jogadores.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum jogador encontrado");
        }
        return ResponseEntity.ok(jogadores);
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarComFiltros(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String posicao,
            @RequestParam(required = false) Integer minGolos,
            @RequestParam(required = false) Integer minCartoes,
            @RequestParam(required = false) Integer minJogos) {
        try {
            List<JogadorDTO> jogadores = jogadorHandler.filtrarJogadores(nome, posicao, minGolos, minCartoes, minJogos);
            if (jogadores.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Nenhum jogador encontrado com os filtros fornecidos.");
            }
            return ResponseEntity.ok(jogadores);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registrarJogador(@RequestBody JogadorDTO dto) {
        try {
            return ResponseEntity.ok(jogadorHandler.create(dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarJogador(@PathVariable Long id, @RequestBody JogadorDTO dto) {
        try {
            return ResponseEntity.ok(jogadorHandler.update(id, dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarJogador(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(jogadorHandler.delete(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/buscar/nome/")
    public ResponseEntity<?> buscarPorNome(@RequestParam String nome) {
        try {
            List<JogadorDTO> jogadores = jogadorHandler.findByName(nome);
            if (jogadores.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Nenhum jogador encontrado com o nome: " + nome);
            }
            return ResponseEntity.ok(jogadores);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/buscar/posicao/{posicao}")
    public ResponseEntity<?> buscarPorPosicao(@RequestParam String posicao) {
        try {
            List<JogadorDTO> jogadores = jogadorHandler.findByPosition(posicao);
            if (jogadores.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Nenhum jogador encontrado na posição: " + posicao);
            }
            return ResponseEntity.ok(jogadores);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}