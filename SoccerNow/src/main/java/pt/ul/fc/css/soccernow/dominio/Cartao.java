package pt.ul.fc.css.soccernow.dominio;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cartoes")
public class Cartao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private CorCartao corCartao;

    @ManyToOne
    private Arbitro arbitro; 

    @ManyToOne
    private Jogador jogador;

    @ManyToOne
    private Equipa equipa;

   @ManyToOne
    private EstatisticasJogo estatisticas;

    public enum CorCartao {
        VERMELHO,
        AMARELO
    }

    public Cartao() {
    }                   

    public Cartao(CorCartao corCartao, Jogador jogador, Equipa equipa, Jogo jogo, EstatisticasJogo estatisticas, Arbitro arbitro) {
        this.corCartao = corCartao;
        this.jogador = jogador;
        this.equipa = equipa; 
        this.estatisticas = estatisticas;
        this.arbitro = arbitro; // <-- Adiciona ao construtor

    }

    public Long getId() {
        return id;
    }

    public CorCartao getCorCartao() {
        return corCartao;
    }

    public Jogador getJogador() {   
        return jogador;
    }

    public Equipa getEquipa() {
        return equipa;
    }

    public EstatisticasJogo getEstatisticas() {
        return estatisticas;
    }
    // Setters

    public Arbitro getArbitro() {
        return arbitro;
    }

    public void setArbitro(Arbitro arbitro) {
        this.arbitro = arbitro;
    }

    public void setCorCartao(CorCartao corCartao) {
        this.corCartao = corCartao;
    }

    public void setJogador(Jogador jogador) {
        this.jogador = jogador; 
    }

    public void setEquipa(Equipa equipa) {
        this.equipa = equipa;
    }
    public void setEstatisticas(EstatisticasJogo estatisticas) {
        this.estatisticas = estatisticas;
    }
}
