package pt.ul.fc.css.soccernow.dto;

public class CartaoDTO {
    private Long id;
    private String corCartao; // "Amarelo" ou "Vermelho"
    private String jogador; // Username do jogador que recebeu o cartão
    private Long arbitroId; // ID do árbitro que mostrou o cartão
    private String equipa; // Nome da equipa do jogador que recebeu o cartão

    public CartaoDTO() {
        // Construtor padrão
    }

    public CartaoDTO(Long id, String corCartao, String jogador, String equipa) {
        this.id = id;
        this.corCartao = corCartao;
        this.jogador = jogador;
        this.equipa = equipa;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getArbitroId() {
        return arbitroId;
    }

    public void setArbitroId(Long arbitroId) {
        this.arbitroId = arbitroId;
    }

    public String getCorCartao() {
        return corCartao;
    }

    public void setCorCartao(String corCartao) {
        this.corCartao = corCartao;
    }

    public String getJogador() {
        return jogador;
    }

    public void setJogador(String jogador) {
        this.jogador = jogador;
    }

    public String getEquipa() {
        return equipa;
    }

    public void setEquipa(String equipa) {
        this.equipa = equipa;
    }
}
