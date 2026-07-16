package pt.ul.fc.css.soccernow.dominio;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "arbitros")
public class Arbitro extends Utilizador {
    
    @Column(nullable = false)
    private boolean isCertificated;

    @ManyToMany(mappedBy = "arbitros")
    private List<Jogo> jogosOficiados;



    // Construtor padrão
    public Arbitro() {
        super();
    }

    // Construtor com parâmetros
    public Arbitro(String nome, boolean isCertificated, String username, String password) {
        super(nome, username, password);
        this.isCertificated = isCertificated;
        this.jogosOficiados = new ArrayList<>();
    }

    // Getters e Setters
    public boolean getIsCertificated() {
        return isCertificated;
    }

    public void setCertificated(boolean isCertificated) {
        this.isCertificated = isCertificated;
    }

    public List<Jogo> getJogosOficiados() {
        return jogosOficiados;
    }

    public void setJogosOficiados(List<Jogo> jogosOficiados) {
        this.jogosOficiados = jogosOficiados;
    }

    public List<Cartao> getCartoesMostrados() {
        List<Cartao> cartoes = new ArrayList<>();
        if (jogosOficiados != null) {
            for (Jogo jogo : jogosOficiados) {
                if (jogo.getEstatisticas() != null && jogo.getEstatisticas().getCartoes() != null) {
                    for (Cartao cartao : jogo.getEstatisticas().getCartoes()) {
                        if (cartao.getArbitro() != null && cartao.getArbitro().getId().equals(this.getId())) {
                            cartoes.add(cartao);
                        }
                    }
                }
            }
        }
        return cartoes;
    }
    
    
    
}