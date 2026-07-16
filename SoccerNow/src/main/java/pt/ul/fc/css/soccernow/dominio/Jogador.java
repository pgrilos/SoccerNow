package pt.ul.fc.css.soccernow.dominio;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

// Jogador
@Entity
@Table(name = "jogadores")
public class Jogador extends Utilizador {

    @Enumerated(EnumType.STRING)
    private Posicao posicao;

    @ManyToMany(mappedBy = "jogadores")
    private List<Equipa> equipas;

    @OneToMany(mappedBy = "jogador")
    private List<Cartao> cartoesRecebidos;

    @ManyToMany
    private List<EquipaJogo> equipasJogo;

    private int golosMarcados = 0;

    // Construtor padrão
    public Jogador() {
    }

    // Construtor com parâmetros
    public Jogador(String nome, Posicao posicao, String username, String password) {
        super(nome, username, password);
        
        this.posicao = posicao;
        this.equipas = new ArrayList<>();
        this.cartoesRecebidos = new ArrayList<>();
        this.equipasJogo = new ArrayList<>();
        this.golosMarcados = 0;
    }

    // Getters e Setters
    public Posicao getPosicao() {
        return posicao;
    }

    public void setPosicao(Posicao posicao) {
        this.posicao = posicao;
    }

    public List<Equipa> getEquipas() {
        return equipas;
    }

    public void setEquipas(List<Equipa> equipas) {
        this.equipas = equipas;
    }

    public List<Cartao> getCartoesRecebidos() {
        return cartoesRecebidos;
    }

    public void setCartoesRecebidos(List<Cartao> cartoesRecebidos) {
        this.cartoesRecebidos = cartoesRecebidos;
    }

    public List<EquipaJogo> getJogos() {
        return equipasJogo;
    }

    public void setJogos(List<EquipaJogo> equipasJogo) {
        this.equipasJogo = equipasJogo;
    }

    public int getGolosMarcados() {
        return golosMarcados;
    }

    public void setGolosMarcados(int golosMarcados) {
        this.golosMarcados = golosMarcados;
    }

    public void adicionarGolo() {
        this.golosMarcados++;
    }
}