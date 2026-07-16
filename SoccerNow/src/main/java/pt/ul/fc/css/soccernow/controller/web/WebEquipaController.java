package pt.ul.fc.css.soccernow.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pt.ul.fc.css.soccernow.dominio.Posicao;
import pt.ul.fc.css.soccernow.handlers.EquipaHandler;

@Controller
public class WebEquipaController {

    private final EquipaHandler equipaHandler;

    public WebEquipaController(EquipaHandler equipaHandler) {
        this.equipaHandler = equipaHandler;
    }

    @GetMapping("/web/equipas")
    public String buscarEquipas(
        @RequestParam(required = false) String nome,
        @RequestParam(required = false) Integer minJogadores,
        @RequestParam(required = false) Integer minVitorias,
        @RequestParam(required = false) Integer minEmpates,
        @RequestParam(required = false) Integer minDerrotas,
        @RequestParam(required = false) Integer minConquistas,
        @RequestParam(required = false) String ausenciaPosicao,
        Model model
    ) {
        model.addAttribute("equipas", equipaHandler.filtrarEquipas(
            nome, minJogadores, minVitorias, minEmpates, minDerrotas, minConquistas, ausenciaPosicao
        ));
        model.addAttribute("posicoes", Posicao.values()); 
        return "equipas";
}

   
}
