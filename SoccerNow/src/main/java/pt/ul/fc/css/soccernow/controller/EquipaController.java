package pt.ul.fc.css.soccernow.controller;

import org.springframework.http.HttpStatus;
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

import pt.ul.fc.css.soccernow.dto.EquipaDTO;
import pt.ul.fc.css.soccernow.handlers.EquipaHandler;

@RestController
@RequestMapping("/api/equipas")
public class EquipaController {
    
    private final EquipaHandler equipaHandler;

    public EquipaController(EquipaHandler equipaHandler) {
        this.equipaHandler = equipaHandler;
    }

    @GetMapping
    public ResponseEntity<?> listarEquipas() {
        try {
            return ResponseEntity.ok(equipaHandler.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registrarEquipa(@RequestBody EquipaDTO dto) {
        try {
            return ResponseEntity.ok(equipaHandler.create(dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarEquipa(@PathVariable Long id, @RequestBody EquipaDTO dto) {
        try {
            return ResponseEntity.ok(equipaHandler.update(id, dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarEquipa(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(equipaHandler.delete(id));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/buscar/nome")
    public ResponseEntity<?> buscarPorNome(@RequestParam String nome) {
        try {
            EquipaDTO equipa = equipaHandler.findByNome(nome);
            return ResponseEntity.ok(equipa);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } 
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}