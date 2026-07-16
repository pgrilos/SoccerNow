package pt.ul.fc.css.soccernow.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

public class EquipaDTO {
    @NotBlank(message = "Nome da equipa é obrigatório")
    private String nome;
    private Long id;
    private long vitorias;
    private long empates;
    private long derrotas;
    private List<String> jogadores;
    private List<ConquistaDTO> conquistas;

    public EquipaDTO() {
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    
    public List<String> getJogadores() { return jogadores; }
    public void setJogadores(List<String> jogadores) { this.jogadores = jogadores; }
    
    
    public List<ConquistaDTO> getConquistas() { return conquistas; }
    public void setConquistas(List<ConquistaDTO> conquistas) { this.conquistas = conquistas; }

    public long getVitorias() { return vitorias; }
    public void setVitorias(long vitorias) { this.vitorias = vitorias; }

    public long getEmpates() { return empates; }
    public void setEmpates(long empates) { this.empates = empates; }

    public long getDerrotas() { return derrotas; }
    public void setDerrotas(long derrotas) { this.derrotas = derrotas; }


}
