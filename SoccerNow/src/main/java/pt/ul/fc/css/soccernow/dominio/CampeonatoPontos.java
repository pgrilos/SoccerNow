package pt.ul.fc.css.soccernow.dominio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "campeonatos_pontos")
public class CampeonatoPontos extends Campeonato {
    
    @ElementCollection
    @CollectionTable(
        name = "classificacao_campeonato",
        joinColumns = @JoinColumn(name = "campeonato_id")
    )
    @MapKeyJoinColumn(name = "equipa_id")
    @Column(name = "pontos")
    private Map<Equipa, Integer> classificacao;  

    public CampeonatoPontos() {
    }

    public CampeonatoPontos(String nome, List<Equipa> equipas, List<JogoCampeonato> jogos) {
        super(nome, equipas, jogos);
        this.classificacao = new HashMap<>();
        if (equipas != null) {
            for (Equipa equipa : equipas) {
                this.classificacao.put(equipa, 0);
            }
        }
    }

    // Getters
    public Map<Equipa, Integer> getClassificacao() {
        return classificacao;
    }

    // Setters
    public  void setClassificacao(Map<Equipa, Integer> classificacao) {
        this.classificacao = classificacao;
    }
    
}

