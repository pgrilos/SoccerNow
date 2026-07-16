package pt.ul.fc.css.soccernow.javaFX.fxControllersFX;

import java.net.http.*;
import java.net.URI;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import pt.ul.fc.css.soccernow.dto.JogadorDTO;
import pt.ul.fc.css.soccernow.dto.ArbitroDTO;

public class UtilizadorControllerFX {

    private static final String JOGADOR_URL = "http://localhost:8080/api/jogadores";
    private static final String ARBITRO_URL = "http://localhost:8080/api/arbitros";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    // --- Jogador ---
    public void registarJogador(JogadorDTO dto) throws Exception {
        String json = mapper.writeValueAsString(dto);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(JOGADOR_URL + "/registro"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public List<JogadorDTO> listarJogadores() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(JOGADOR_URL))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), new TypeReference<List<JogadorDTO>>() {
        });
    }

    public void atualizarJogador(Long id, JogadorDTO dto) throws Exception {
        String json = mapper.writeValueAsString(dto);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(JOGADOR_URL + "/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void eliminarJogador(Long id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(JOGADOR_URL + "/" + id))
                .DELETE()
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    // --- Árbitro ---
    public void registarArbitro(ArbitroDTO dto) throws Exception {
        String json = mapper.writeValueAsString(dto);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ARBITRO_URL + "/registro"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public List<ArbitroDTO> listarArbitros() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ARBITRO_URL))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), new TypeReference<List<ArbitroDTO>>() {
        });
    }

    public void atualizarArbitro(Long id, ArbitroDTO dto) throws Exception {
        String json = mapper.writeValueAsString(dto);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ARBITRO_URL + "/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void eliminarArbitro(Long id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ARBITRO_URL + "/" + id))
                .DELETE()
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}