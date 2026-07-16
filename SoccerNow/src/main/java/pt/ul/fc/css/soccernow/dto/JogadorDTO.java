package pt.ul.fc.css.soccernow.dto;

public class JogadorDTO {
    private Long id;
    private String nome;
    private String username;
    private String password;
    private String posicao;
    private long numJogos;
    private int numGolos;
    private long numCartoesAmarelos;
    private long numCartoesVermelhos;

    // Empty constructor (for frameworks)
    public JogadorDTO() {
    }

    // Full constructor (with ID)
    public JogadorDTO(Long id, String nome, String posicao, String username, String password) {
        this.id = id;
        this.nome = nome;
        this.posicao = posicao;
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPosicao() { return posicao; }
    public void setPosicao(String posicao) { this.posicao = posicao; }
    
    public long getNumJogos() { return numJogos; }
    public void setNumJogos(long numJogos) { this.numJogos = numJogos; }

    public int getNumGolos() { return numGolos; }
    public void setNumGolos(int numGolos) { this.numGolos = numGolos; }

    public long getNumCartoesAmarelos() { return numCartoesAmarelos; }
    public void setNumCartoesAmarelos(long numCartoesAmarelos) { this.numCartoesAmarelos = numCartoesAmarelos; }

    public long getNumCartoesVermelhos() { return numCartoesVermelhos; }
    public void setNumCartoesVermelhos(long numCartoesVermelhos) { this.numCartoesVermelhos = numCartoesVermelhos; }
}