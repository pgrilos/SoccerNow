package pt.ul.fc.css.soccernow.dto;

public class EquipaPontuacaoDTO {
    private String equipa;
    private int pontos;

    public EquipaPontuacaoDTO() {}

    public EquipaPontuacaoDTO(String equipa, int pontos) {
        this.equipa = equipa;
        this.pontos = pontos;
    }

    public String getEquipa() { return equipa; }
    public void setEquipa(String equipa) { this.equipa = equipa; }
    public int getPontos() { return pontos; }
    public void setPontos(int pontos) { this.pontos = pontos; }
}