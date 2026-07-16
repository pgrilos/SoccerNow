package pt.ul.fc.css.soccernow.dto;

import java.util.List;

public class ArbitroDTO {

    private Long id;
    private String nome;
    private String username;
    private String password;
    private boolean isCertificated;

    private List<Long> jogosOficiados;      
    private List<Long> cartoesMostrados;

    public ArbitroDTO() {}

    public ArbitroDTO(Long id, String nome, boolean isCertificated, String username, String password) {
        this.id = id;
        this.nome = nome;
        this.isCertificated = isCertificated;
        this.username = username;
        this.password = password;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public boolean getIsCertificated() { return isCertificated; }
    public void setIsCertificated(boolean certificated) { isCertificated = certificated; }

    public List<Long> getJogosOficiados() { return jogosOficiados; }
    public void setJogosOficiados(List<Long> jogosOficiados) { this.jogosOficiados = jogosOficiados; }

    public List<Long> getCartoesMostrados() { return cartoesMostrados; }
    public void setCartoesMostrados(List<Long> cartoesMostrados) { this.cartoesMostrados = cartoesMostrados; }
}