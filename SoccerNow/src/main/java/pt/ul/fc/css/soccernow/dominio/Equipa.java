package pt.ul.fc.css.soccernow.dominio;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "equipas")
public class Equipa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @ManyToMany
    private List<Jogador> jogadores;

    @OneToMany(mappedBy = "equipa")
    private List<EquipaJogo> equipasJogos;

    @ElementCollection
    private List<Conquista> conquistas;

    // Construtor padrão
    public Equipa() {
    }

    // Construtor com parâmetros
    public Equipa(String nome) {
        this.nome = nome;
        this.jogadores = new ArrayList<>();
        this.equipasJogos = new ArrayList<>();
        this.conquistas = new ArrayList<>();
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Jogador> getJogadores() {
        return jogadores;
    }

    public void setJogadores(List<Jogador> jogadores) {
        this.jogadores = jogadores;
    }

    public void setEquipasJogos(List<EquipaJogo> equipasJogos) {
        this.equipasJogos = equipasJogos;
    }   

    public List<EquipaJogo> getEquipasJogos() {
        return equipasJogos;
    }

    public List<Conquista> getConquistas() {
        return conquistas;
    }

    public void setConquistas(List<Conquista> conquistas) {
        this.conquistas = conquistas;
    }


    public long getVitorias() {
        return equipasJogos.stream()
            .filter(ej -> ej.getJogo() != null && ej.getJogo().getEstatisticas() != null)
            .filter(ej -> {
                Jogo jogo = ej.getJogo();
                EstatisticasJogo est = jogo.getEstatisticas();
                int meusGolos = 0, adversarioGolos = 0;
                if (jogo.getEquipaJogoCasa().getEquipa().equals(this)) {
                    meusGolos = est.getMarcadoresEquipaCasa() != null ? est.getMarcadoresEquipaCasa().size() : 0;
                    adversarioGolos = est.getMarcadoresEquipaVisitante() != null ? est.getMarcadoresEquipaVisitante().size() : 0;
                } else if (jogo.getEquipaJogoVisitante().getEquipa().equals(this)) {
                    meusGolos = est.getMarcadoresEquipaVisitante() != null ? est.getMarcadoresEquipaVisitante().size() : 0;
                    adversarioGolos = est.getMarcadoresEquipaCasa() != null ? est.getMarcadoresEquipaCasa().size() : 0;
                }
                return meusGolos > adversarioGolos;
            })
            .count();
    }

    public long getEmpates() {
        return equipasJogos.stream()
            .filter(ej -> ej.getJogo() != null && ej.getJogo().getEstatisticas() != null)
            .filter(ej -> {
                Jogo jogo = ej.getJogo();
                EstatisticasJogo est = jogo.getEstatisticas();
                int meusGolos = 0, adversarioGolos = 0;
                if (jogo.getEquipaJogoCasa().getEquipa().equals(this)) {
                    meusGolos = est.getMarcadoresEquipaCasa() != null ? est.getMarcadoresEquipaCasa().size() : 0;
                    adversarioGolos = est.getMarcadoresEquipaVisitante() != null ? est.getMarcadoresEquipaVisitante().size() : 0;
                } else if (jogo.getEquipaJogoVisitante().getEquipa().equals(this)) {
                    meusGolos = est.getMarcadoresEquipaVisitante() != null ? est.getMarcadoresEquipaVisitante().size() : 0;
                    adversarioGolos = est.getMarcadoresEquipaCasa() != null ? est.getMarcadoresEquipaCasa().size() : 0;
                }
                return meusGolos == adversarioGolos;
            })
            .count();
    }

    public long getDerrotas() {
        return equipasJogos.stream()
            .filter(ej -> ej.getJogo() != null && ej.getJogo().getEstatisticas() != null)
            .filter(ej -> {
                Jogo jogo = ej.getJogo();
                EstatisticasJogo est = jogo.getEstatisticas();
                int meusGolos = 0, adversarioGolos = 0;
                if (jogo.getEquipaJogoCasa().getEquipa().equals(this)) {
                    meusGolos = est.getMarcadoresEquipaCasa() != null ? est.getMarcadoresEquipaCasa().size() : 0;
                    adversarioGolos = est.getMarcadoresEquipaVisitante() != null ? est.getMarcadoresEquipaVisitante().size() : 0;
                } else if (jogo.getEquipaJogoVisitante().getEquipa().equals(this)) {
                    meusGolos = est.getMarcadoresEquipaVisitante() != null ? est.getMarcadoresEquipaVisitante().size() : 0;
                    adversarioGolos = est.getMarcadoresEquipaCasa() != null ? est.getMarcadoresEquipaCasa().size() : 0;
                }
                return meusGolos < adversarioGolos;
            })
            .count();
    }

}