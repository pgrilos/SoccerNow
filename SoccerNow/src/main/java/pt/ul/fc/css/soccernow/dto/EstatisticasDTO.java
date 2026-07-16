package pt.ul.fc.css.soccernow.dto;

import java.util.List;

public class EstatisticasDTO {

    private Long id;
    private List<String> marcadoresEquipaCasa; 
    private List<String> marcadoresEquipaVisitante; 
    private List<CartaoDTO> cartoes;

    public EstatisticasDTO() {
    }

    public EstatisticasDTO(Long id, List<String> marcadoresEquipaCasa, List<String> marcadoresEquipaVisitante, List<CartaoDTO> cartoes) {
        this.id = id;
        this.marcadoresEquipaCasa = marcadoresEquipaCasa;
        this.marcadoresEquipaVisitante = marcadoresEquipaVisitante;
        this.cartoes = cartoes;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getMarcadoresEquipaCasa() {
        return marcadoresEquipaCasa;
    }

    public void setMarcadoresEquipaCasa(List<String> marcadoresEquipaCasa) {
        this.marcadoresEquipaCasa = marcadoresEquipaCasa;
    }

    public List<String> getMarcadoresEquipaVisitante() {
        return marcadoresEquipaVisitante;
    }

    public void setMarcadoresEquipaVisitante(List<String> marcadoresEquipaVisitante) {
        this.marcadoresEquipaVisitante = marcadoresEquipaVisitante;
    }

    public List<CartaoDTO> getCartoes() {
        return cartoes;
    }

    public void setCartoes(List<CartaoDTO> cartoes) {
        this.cartoes = cartoes;
    }
}