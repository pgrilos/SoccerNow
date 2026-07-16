package pt.ul.fc.css.soccernow.dominio;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "estatisticas_jogo")  
public class EstatisticasJogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToMany
    private List<Jogador> marcadoresEquipaCasa;  // Gols da equipa da casa

    @ManyToMany
    private List<Jogador> marcadoresEquipaVisitante;   // Gols da equipa visitante

    // @OneToOne(mappedBy = "estatisticas")
    // private Jogo jogo;

    @OneToMany(mappedBy = "estatisticas", cascade = CascadeType.ALL)
    private List<Cartao> cartoes;


    public EstatisticasJogo() {
    }

    public EstatisticasJogo(List<Jogador> marcadoresEquipaCasa, List<Jogador> marcadoresEquipaVisitante, Jogo jogo, List<Cartao> cartoes) {
        this.marcadoresEquipaCasa = marcadoresEquipaCasa;
        this.marcadoresEquipaVisitante = marcadoresEquipaVisitante;
        // this.jogo = jogo;
        this.cartoes = cartoes;
    }   

    // Getters
    public Long getId() {
        return id;
    }

    public List<Jogador> getMarcadoresEquipaCasa() {
        return  marcadoresEquipaCasa;
    }

    public List<Jogador> getMarcadoresEquipaVisitante() {
        return marcadoresEquipaVisitante;
    }

    // public Jogo getJogo() {
    //     return jogo;
    //     }                   

    public List<Cartao> getCartoes() {
        return cartoes;
    }

    // Setters


    public void setMarcadoresEquipaCasa(List<Jogador> marcadoresEquipaCasa) {
        this.marcadoresEquipaCasa = marcadoresEquipaCasa;
    }

    public void setMarcadoresEquipaVisitante(List<Jogador> marcadoresEquipaVisitante) {
        this.marcadoresEquipaVisitante = marcadoresEquipaVisitante;
    }

    // public void setJogo(Jogo jogo) {
    //     this.jogo = jogo;
    // }

    public void setCartoes(List<Cartao> cartoes) {
        this.cartoes = cartoes;
    }
    
    
}
