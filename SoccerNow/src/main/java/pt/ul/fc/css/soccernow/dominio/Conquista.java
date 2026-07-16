package pt.ul.fc.css.soccernow.dominio;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;

@Embeddable
public class Conquista {

    @Column(nullable = false)
    private int lugar;

    @ManyToOne
    private Campeonato campeonato;

    public Conquista() {
    }

    public Conquista(int lugar, Equipa equipa, Campeonato campeonato) {
        this.lugar = lugar;
        this.campeonato = campeonato;
    }

    // Getters


    public int getLugar() {
        return lugar;
    }       


    public Campeonato getCampeonato() {
        return campeonato;
    }

    // Setters

    public void setLugar(int lugar) {
        this.lugar = lugar;
    }   


    public void setCampeonato(Campeonato campeonato) {
        this.campeonato = campeonato;
    }               
    
    
}