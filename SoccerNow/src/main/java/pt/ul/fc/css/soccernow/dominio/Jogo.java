package pt.ul.fc.css.soccernow.dominio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "jogos")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_jogo")
public class Jogo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ativo")
    private boolean ativo = true;

    @Column(nullable = false)   
    private LocalDate data;

    // Formato: "HH:mm" (fazer verificação)
    @Column(nullable = false)
    private String hora;

    @Embedded
    private Localizacao local;

    @OneToMany(mappedBy = "jogo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EquipaJogo> equipasJogo = new ArrayList<>();

    @ManyToMany
    private List<Arbitro> arbitros;

    @ManyToOne
    @JoinColumn(name = "arbitro_principal_id")
    private Arbitro arbitroPrincipal;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "estatisticas_id")
    private EstatisticasJogo estatisticas;


    public Jogo() {
    }

    // Construtor
    public Jogo(LocalDate data, String hora, Localizacao local, List<EquipaJogo> equipasJogo,List<Arbitro> arbitros, Arbitro arbitroPrincipal, EstatisticasJogo estatisticas) {
        this.data = data;
        this.hora = hora;
        this.local = local;
        this.equipasJogo = equipasJogo;
        this.arbitros = arbitros;   
        this.arbitroPrincipal = arbitroPrincipal;
        this.estatisticas = estatisticas;
    }


    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Localizacao getLocal() { return local; }

    public void setLocal(Localizacao local) { this.local = local; }

    public void setEquipasJogo(List<EquipaJogo> equipasJogo) {
        this.equipasJogo = equipasJogo;
    }

    public EquipaJogo getEquipaJogoCasa() {
        return equipasJogo.get(0);
    }

    public void setEquipaJogoCasa(EquipaJogo equipaCasa) {
        this.equipasJogo.set(0, equipaCasa);
    }

    public EquipaJogo getEquipaJogoVisitante() {
        return equipasJogo.get(1);
    }

    public void setEquipaJogoVisitante(EquipaJogo equipaVisitante) {
        this.equipasJogo.set(1, equipaVisitante);
    }

    public List<Arbitro> getArbitros() {
        return arbitros;
    }

    public void setArbitros(List<Arbitro> arbitros) {
        this.arbitros = arbitros;
    }

    public Arbitro getArbitroPrincipal() {
        return arbitroPrincipal;
    }

    public void setArbitroPrincipal(Arbitro arbitroPrincipal) {
        this.arbitroPrincipal = arbitroPrincipal;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public EstatisticasJogo getEstatisticas() {
        return estatisticas;
    }

    public void setEstatisticas(EstatisticasJogo estatisticas) {
        this.estatisticas = estatisticas;
    }
}    
