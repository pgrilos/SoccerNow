package pt.ul.fc.css.soccernow.dominio;


import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

// Classe para jogos de campeonato
@Entity
@DiscriminatorValue("CAMPEONATO")
public class JogoCampeonato extends Jogo {
    
    @ManyToOne
    @JsonIgnore
    private Campeonato campeonato;

    public JogoCampeonato() {
        super();
    }

    public JogoCampeonato(LocalDate data, String hora, Localizacao local, 
                          List<EquipaJogo>  equipasJogo, List<Arbitro> arbitros,
                         Arbitro arbitroPrincipal, Campeonato campeonato, EstatisticasJogo estatisticas) {
        super(data, hora, local, equipasJogo, arbitros, arbitroPrincipal, estatisticas);
        this.campeonato = campeonato;
    }

    // Getters
    public Campeonato getCampeonato() {
        return campeonato;
    }

    // Setters
    public void setCampeonato(Campeonato campeonato) {
        this.campeonato = campeonato;
    }

}
