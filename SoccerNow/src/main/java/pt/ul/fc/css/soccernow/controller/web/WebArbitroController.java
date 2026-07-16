package pt.ul.fc.css.soccernow.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pt.ul.fc.css.soccernow.handlers.ArbitroHandler;
import pt.ul.fc.css.soccernow.dto.ArbitroDTO;

import java.util.List;

@Controller
public class WebArbitroController {

    private final ArbitroHandler arbitroHandler;

    public WebArbitroController(ArbitroHandler arbitroHandler) {
        this.arbitroHandler = arbitroHandler;
    }

    @GetMapping("/web/arbitros")
    public String buscarArbitros(
        @RequestParam(required = false) String nome,
        @RequestParam(required = false) Integer jogosOficiados,
        @RequestParam(required = false) Integer cartoesMostrados,
        Model model
    ) {
        List<ArbitroDTO> arbitros = arbitroHandler.findAll();

        if (nome != null && !nome.isBlank()) {
            arbitros = arbitros.stream()
                .filter(a -> a.getNome() != null && a.getNome().toLowerCase().contains(nome.toLowerCase()))
                .toList();
        }
        if (jogosOficiados != null) {
            arbitros = arbitros.stream()
                .filter(a -> a.getJogosOficiados() != null && a.getJogosOficiados().size() == jogosOficiados)
                .toList();
        }
        if (cartoesMostrados != null) {
            arbitros = arbitros.stream()
                .filter(a -> a.getCartoesMostrados() != null && a.getCartoesMostrados().size() == cartoesMostrados)
                .toList();
        }

        model.addAttribute("arbitros", arbitros);
        return "arbitros";
    }
}