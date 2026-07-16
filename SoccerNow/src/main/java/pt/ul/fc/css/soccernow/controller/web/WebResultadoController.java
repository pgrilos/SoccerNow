package pt.ul.fc.css.soccernow.controller.web;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import pt.ul.fc.css.soccernow.dominio.Arbitro;
import pt.ul.fc.css.soccernow.dto.CartaoDTO;
import pt.ul.fc.css.soccernow.dto.EstatisticasDTO;
import pt.ul.fc.css.soccernow.handlers.JogoHandler;
import pt.ul.fc.css.soccernow.repositorio.ArbitroRepository;

@Controller
@RequestMapping("/web/resultados")
public class WebResultadoController {

    private final JogoHandler jogoHandler;
    private final ArbitroRepository arbitroRepository;

    public WebResultadoController(JogoHandler jogoHandler, ArbitroRepository arbitroRepository) {
        this.jogoHandler = jogoHandler;
        this.arbitroRepository = arbitroRepository;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("cartoes[].arbitroUsername");
    }

    // Mostra o formulário
    @GetMapping("/{id}")
    public String mostrarFormulario(@PathVariable Long id, Model model) {
        EstatisticasDTO estatisticas = new EstatisticasDTO();
        // Adiciona um cartão vazio para mostrar pelo menos uma linha
        estatisticas.setCartoes(new ArrayList<>());
        estatisticas.getCartoes().add(new CartaoDTO());
        model.addAttribute("jogoId", id);
        model.addAttribute("estatisticas", estatisticas);
        return "resultados";
    }

    // Submete o resultado
   @PostMapping("/{id}")
    public String registarResultado(
        @PathVariable Long id,
        @Valid @ModelAttribute EstatisticasDTO estatisticas,
        HttpServletRequest request,
        BindingResult bindingResult,
        Model model
    ) {
        model.addAttribute("jogoId", id);

        boolean erro = false;
        StringBuilder erros = new StringBuilder();

        if (estatisticas.getCartoes() != null) {
            for (int i = 0; i < estatisticas.getCartoes().size(); i++) {
                CartaoDTO cartao = estatisticas.getCartoes().get(i);

                // Validar campos obrigatórios do cartão
                if (cartao.getCorCartao() == null || cartao.getCorCartao().isBlank()) {
                    erros.append("Cor do cartão em falta na linha ").append(i + 1).append(". ");
                    erro = true;
                }
                if (cartao.getJogador() == null || cartao.getJogador().isBlank()) {
                    erros.append("Jogador em falta na linha ").append(i + 1).append(". ");
                    erro = true;
                }
                if (cartao.getEquipa() == null || cartao.getEquipa().isBlank()) {
                    erros.append("Equipa em falta na linha ").append(i + 1).append(". ");
                    erro = true;
                }

                String username = request.getParameter("cartoes[" + i + "].arbitroUsername");
                if (username == null || username.isBlank()) {
                    erros.append("Árbitro em falta na linha ").append(i + 1).append(". ");
                    erro = true;
                } else {
                    Arbitro arbitro = arbitroRepository.findByUsername(username);
                    if (arbitro != null) {
                        cartao.setArbitroId(arbitro.getId());
                    } else {
                        erros.append("Árbitro não encontrado ('").append(username).append("') na linha ").append(i + 1).append(". ");
                        erro = true;
                    }
                }
            }
        }

        if (erro || bindingResult.hasErrors()) {
            model.addAttribute("estatisticas", estatisticas);
            model.addAttribute("mensagem", "Erro: " + erros.toString());
            return "resultados";
        }

        try {
            jogoHandler.updateResultado(id, estatisticas);
            model.addAttribute("estatisticas", estatisticas);

            model.addAttribute("mensagem", "Resultado registado com sucesso!");
        } catch (Exception e) {
            model.addAttribute("estatisticas", estatisticas); 

            model.addAttribute("mensagem", "Erro ao guardar: " + e.getMessage());
        }
        return "resultados";
    }
}