package pt.ul.fc.css.soccernow.dominio;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "campeonatos")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Campeonato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;
    
    @ManyToMany
    private List<Equipa> equipas;

    @OneToMany(mappedBy = "campeonato", cascade = CascadeType.ALL)
    private List<JogoCampeonato> jogos;

    public Campeonato() {
    }

    public Campeonato(String nome, List<Equipa> equipas, List<JogoCampeonato> jogos) {
        this.nome = nome;
        this.equipas = equipas; 
        this.jogos = jogos;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public List<Equipa> getEquipas() {
        return equipas;
    }

    public List<JogoCampeonato> getJogos() {
        return jogos;
    }

    // Setters

    public void setNome(String nome) {
        this.nome = nome;
    }                   

    public void setEquipas(List<Equipa> equipas) {
        this.equipas = equipas;
    }

    public void setJogos(List<JogoCampeonato> jogos) {
        this.jogos = jogos;
    }
    
    public void addJogo(JogoCampeonato jogo) {
        if (this.jogos == null) {
            this.jogos = new java.util.ArrayList<>();
        }
        this.jogos.add(jogo);
        jogo.setCampeonato(this); // garantir bidirecionalidade
    }

    public void removeJogo(JogoCampeonato jogo) {
        if (this.jogos != null && jogo != null) {
            this.jogos.removeIf(j -> j.getId().equals(jogo.getId()));
            jogo.setCampeonato(null); // remove a referência do lado do jogo
        }
    }
}
