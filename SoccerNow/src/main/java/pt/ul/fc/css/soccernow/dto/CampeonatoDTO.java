package pt.ul.fc.css.soccernow.dto;

import java.util.List;

public class CampeonatoDTO {
    private Long id;
    private String nome;
    private List<String> equipas;
    private List<JogoDTO> jogos;
    private List<EquipaPontuacaoDTO> classificacao;



    public CampeonatoDTO() {
    }

    public CampeonatoDTO(Long id, String nome, List<String> equipas, List<JogoDTO> jogos) {
        this.id = id;
        this.nome = nome;
        this.equipas = equipas;
        this.jogos = jogos;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public List<String> getEquipasNomes() { return equipas; }
    public void setEquipasNomes(List<String> equipasNomes) { this.equipas = equipasNomes; }
    public List<JogoDTO> getJogos() { return jogos; }
    public void setJogos(List<JogoDTO> jogos) { this.jogos = jogos; }
    public List<EquipaPontuacaoDTO> getClassificacao() { return classificacao; }
    public void setClassificacao(List<EquipaPontuacaoDTO> classificacao) { this.classificacao = classificacao; }

}