package pt.ul.fc.css.soccernow.dominio;

import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "equipas_jogo")
public class EquipaJogo { 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Jogadores que participam deste jogo por esta equipa
    @ManyToMany
    private List<Jogador> jogadoresParticipantes;

    @ManyToOne
    private Equipa equipa;

    @OneToOne
    private Jogo jogo;


    // Construtor padrão
    public EquipaJogo() {}

    public EquipaJogo(List<Jogador> jogadoresParticipantes, Equipa equipa) {
        this.equipa = equipa;
        this.jogadoresParticipantes = jogadoresParticipantes;
    }

    public Long getId() {
        return id;
    }

    public List<Jogador> getJogadoresParticipantes() {
        return jogadoresParticipantes;
    }

    public void setJogadoresParticipantes(List<Jogador> jogadoresParticipantes) {
        this.jogadoresParticipantes = jogadoresParticipantes;
    }

    public Equipa getEquipa() {
        return equipa;
    }

    public void setEquipa(Equipa equipa) {
        this.equipa = equipa;
    }

    public Jogo getJogo() {
        return jogo;
    }

    public void setJogo(Jogo jogo) {
        this.jogo = jogo;
    }
}