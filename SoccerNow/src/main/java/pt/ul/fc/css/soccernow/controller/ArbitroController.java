package pt.ul.fc.css.soccernow.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ul.fc.css.soccernow.dto.ArbitroDTO;
import pt.ul.fc.css.soccernow.handlers.ArbitroHandler;

@RestController
@RequestMapping("/api/arbitros")
public class ArbitroController {
    
    private final ArbitroHandler arbitroService;

    public ArbitroController(ArbitroHandler arbitroService) {
        this.arbitroService = arbitroService;
    }

    @GetMapping
    public ResponseEntity<?> listarArbitros() {
        List<ArbitroDTO> arbitros = arbitroService.findAll();
        if (arbitros.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum árbitro encontrado");
        }
        return ResponseEntity.ok(arbitros);
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registrarArbitro(@RequestBody ArbitroDTO dto) {
        try {
            return ResponseEntity.ok(arbitroService.create(dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarArbitro(@PathVariable Long id, @RequestBody ArbitroDTO dto) {
        try {             
            return ResponseEntity.ok(arbitroService.update(id, dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarArbitro(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(arbitroService.delete(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}