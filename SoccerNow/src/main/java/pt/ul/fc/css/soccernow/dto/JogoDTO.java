package pt.ul.fc.css.soccernow.dto;

import java.util.List;



public class JogoDTO {
    private Long id;
    private String data;
    private String hora; 
    private LocalizacaoDTO local;
    private EquipaJogoDTO equipaCasa;
    private EquipaJogoDTO equipaVisitante;
    private String arbitroPrincipal;
    private List <String> arbitros;
    private String campeonato;
    private EstatisticasDTO estatisticas;

    public JogoDTO() {
        // Construtor padrão
    }

    public JogoDTO(Long id, LocalizacaoDTO local, EquipaJogoDTO equipaCasa, EquipaJogoDTO equipaVisitante, String data, String hora, String arbitroPrincipal, List <String> arbitros, String campeonato) {
        this.id = id;
        this.equipaCasa = equipaCasa;
        this.equipaVisitante = equipaVisitante;
        this.data = data;
        this.hora = hora;
        this.local = local;
        this.arbitroPrincipal = arbitroPrincipal;
        this.arbitros = arbitros;
        this.campeonato = campeonato;

    }


    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }
    public String getData() { return this.data; }
    public void setData(String data) { this.data = data; }
    public String getHora() { return this.hora; }
    public void setHora(String hora) { this.hora = hora; }  
    public LocalizacaoDTO getLocal() { return this.local; }
    public void setLocal(LocalizacaoDTO local) { this.local = local; }
    public EquipaJogoDTO getEquipaJogoCasa() { return this.equipaCasa; }
    public void setEquipaJogoCasa(EquipaJogoDTO equipaCasa) { this.equipaCasa = equipaCasa; }
    public EquipaJogoDTO getEquipaJogoVisitante() { return this.equipaVisitante; }
    public void setEquipaJogoVisitante(EquipaJogoDTO equipaVisitante) {this.equipaVisitante = equipaVisitante; }
    public String getArbitroPrincipal() { return this.arbitroPrincipal; }
    public void setArbitroPrincipal(String arbitroPrincipal) { this.arbitroPrincipal = arbitroPrincipal;  } 
    public List<String> getArbitros() { return this.arbitros; }
    public void setArbitros(List<String> arbitros) { this.arbitros = arbitros; }  
    public String getCampeonato() { return this.campeonato;}
    public void setCampeonato(String campeonato) { this.campeonato = campeonato; }
    public EstatisticasDTO getEstatisticas() { return this.estatisticas; }
    public void setEstatisticas(EstatisticasDTO estatisticas) { this.estatisticas = estatisticas;  }
}



