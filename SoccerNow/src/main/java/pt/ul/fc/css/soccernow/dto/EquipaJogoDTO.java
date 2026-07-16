package pt.ul.fc.css.soccernow.dto;

import java.util.List;

public class EquipaJogoDTO {

    private Long id;
    private List<String> jogadoresParticipantes; // usernames dos jogadores
    private String equipa; // nome da equipa


    public EquipaJogoDTO() {}

    public EquipaJogoDTO(Long id, List<String> jogadoresParticipantes, String equipa, Long jogoId) {
        this.id = id;
        this.jogadoresParticipantes = jogadoresParticipantes;
        this.equipa = equipa;
    }
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getJogadoresParticipantes() {
        return jogadoresParticipantes;
    }

    public void setJogadoresParticipantes(List<String> jogadoresParticipantes) {
        this.jogadoresParticipantes = jogadoresParticipantes;
    }

    public String getEquipa() {
        return equipa;
    }

    public void setEquipa(String equipa) {
        this.equipa = equipa;
    }

}