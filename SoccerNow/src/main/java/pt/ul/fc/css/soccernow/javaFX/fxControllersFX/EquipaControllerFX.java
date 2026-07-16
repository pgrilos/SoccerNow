package pt.ul.fc.css.soccernow.javaFX.fxControllersFX;

import java.net.http.*;
import java.net.URI;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import pt.ul.fc.css.soccernow.dto.EquipaDTO;

public class EquipaControllerFX {
    private static final String EQUIPA_URL = "http://localhost:8080/api/equipas";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public void registarEquipa(EquipaDTO dto) throws Exception {
        String json = mapper.writeValueAsString(dto);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(EQUIPA_URL + "/registro"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public List<EquipaDTO> listarEquipas() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(EQUIPA_URL))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), new TypeReference<List<EquipaDTO>>() {
        });
    }

    public void atualizarEquipa(Long id, EquipaDTO dto) throws Exception {
        String json = mapper.writeValueAsString(dto);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(EQUIPA_URL + "/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void eliminarEquipa(Long id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(EQUIPA_URL + "/" + id))
                .DELETE()
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}