package pt.ul.fc.css.soccernow.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pt.ul.fc.css.soccernow.handlers.JogoHandler;

@Controller
public class WebJogoController {

    private final JogoHandler jogoHandler;

    public WebJogoController(JogoHandler jogoHandler) {
        this.jogoHandler = jogoHandler;
    }

    @GetMapping("/web/jogos")
    public String buscarJogos(
        @RequestParam(required = false) Boolean realizados,
        @RequestParam(required = false) Integer minGolos,
        @RequestParam(required = false) String rua,
        @RequestParam(required = false) String cidade,
        @RequestParam(required = false) String turno,
        Model model
    ) {
        model.addAttribute("jogos", jogoHandler.filtrarJogos(realizados, minGolos, rua, cidade, turno));
        model.addAttribute("realizados", realizados);
        model.addAttribute("minGolos", minGolos);
        model.addAttribute("rua", rua);
        model.addAttribute("cidade", cidade);
        model.addAttribute("turno", turno);
        return "jogos";
    }
}