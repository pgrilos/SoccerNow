package pt.ul.fc.css.soccernow.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pt.ul.fc.css.soccernow.handlers.JogadorHandler;

@Controller
public class WebJogadorController {

    private final JogadorHandler jogadorHandler;

    public WebJogadorController(JogadorHandler jogadorHandler) {
        this.jogadorHandler = jogadorHandler;
    }

    @GetMapping("/web/jogadores")
    public String buscarJogadores(
        @RequestParam(required = false) String nome,
        @RequestParam(required = false) String posicao,
        @RequestParam(required = false) Integer minGolos,
        @RequestParam(required = false) Integer minCartoes,
        @RequestParam(required = false) Integer minJogos,
        Model model
    ) {
        model.addAttribute("jogadores", jogadorHandler.filtrarJogadores(
            nome, posicao, minGolos, minCartoes, minJogos
        ));
        return "jogadores";
    }
}