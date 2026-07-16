package pt.ul.fc.css.soccernow.dominio;


import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("AMIGAVEL")
public class JogoAmigavel extends Jogo {
    
    public JogoAmigavel(LocalDate data, String hora, Localizacao local, 
                       List<EquipaJogo> equipasJogo, List<Arbitro> arbitros, Arbitro arbitroPrincipal, EstatisticasJogo estatisticas) {
        super(data, hora, local, equipasJogo, arbitros, arbitroPrincipal, estatisticas);
    }                   
    public JogoAmigavel() {
        // Construtor padrão
    }
}
