package pt.ul.fc.css.soccernow.dto;

public class LocalizacaoDTO {

    private String rua;
    private String cidade;
    
    public LocalizacaoDTO() {

    }

    public LocalizacaoDTO(String rua, String cidade) {
        this.rua = rua;
        this.cidade = cidade;
    }
    public String getRua() {
        return rua;
    }
    public void setRua(String rua) {
        this.rua = rua;
    }
    public String getCidade() {
        return cidade;
    }
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

}