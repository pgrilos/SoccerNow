package pt.ul.fc.css.soccernow.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ConquistaDTO {
    @NotNull(message = "Lugar é obrigatório")
    @Min(value = 1, message = "Lugar deve ser maior que 0")
    private int lugar;
    
    private Long campeonatoId;
    private String campeonatoNome;

    // Construtor padrão
    public ConquistaDTO() {
    }

    // Construtor com parâmetros
    public ConquistaDTO(int lugar, Long campeonatoId, String campeonatoNome) {
        this.lugar = lugar;
        this.campeonatoId = campeonatoId;
        this.campeonatoNome = campeonatoNome;
    }

    // Getters e Setters
    public int getLugar() {
        return lugar;
    }

    public void setLugar(int lugar) {
        this.lugar = lugar;
    }

    public Long getCampeonatoId() {
        return campeonatoId;
    }

    public void setCampeonatoId(Long campeonatoId) {
        this.campeonatoId = campeonatoId;
    }

    public String getCampeonatoNome() {
        return campeonatoNome;
    }

    public void setCampeonatoNome(String campeonatoNome) {
        this.campeonatoNome = campeonatoNome;
    }
}