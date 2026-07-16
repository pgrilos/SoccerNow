package pt.ul.fc.css.soccernow.javaFX.fxControllersFX;

import java.net.http.*;
import java.net.URI;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import pt.ul.fc.css.soccernow.dto.CampeonatoDTO;

public class CampeonatoControllerFX {
    private static final String CAMPEONATO_URL = "http://localhost:8080/api/campeonatos";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public String criarCampeonato(CampeonatoDTO dto) throws Exception {
        String json = mapper.writeValueAsString(dto);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(CAMPEONATO_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return null; // Sucesso
        } else {
            return response.body(); // Mensagem de erro do backend
        }
    }

    public List<CampeonatoDTO> listarCampeonatos() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(CAMPEONATO_URL))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), new TypeReference<List<CampeonatoDTO>>() {});
    }

    // Adicione no CampeonatoControllerFX.java
    public void atualizarCampeonato(Long id, CampeonatoDTO dto) throws Exception {
        String json = mapper.writeValueAsString(dto);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(CAMPEONATO_URL + "/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void removerCampeonato(Long id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(CAMPEONATO_URL + "/" + id))
                .DELETE()
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}