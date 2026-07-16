package pt.ul.fc.css.soccernow.javaFX.fxControllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;

import java.util.List;

import javafx.beans.property.ReadOnlyStringWrapper;
import pt.ul.fc.css.soccernow.dto.JogoDTO;
import pt.ul.fc.css.soccernow.javaFX.AppFX;
import pt.ul.fc.css.soccernow.javaFX.fxControllersFX.JogoControllerFX;

public class GestaoJogosController {
    @FXML
    private TableView<JogoDTO> tableJogos;
    @FXML
    private TableColumn<JogoDTO, String> colData;
    @FXML
    private TableColumn<JogoDTO, String> colHora;
    @FXML
    private TableColumn<JogoDTO, String> colEquipaCasa;
    @FXML
    private TableColumn<JogoDTO, String> colEquipaVisitante;
    @FXML
    private TableColumn<JogoDTO, String> colResultado;
    @FXML
    private TableColumn<JogoDTO, String> colCampeonato;
    @FXML
    private TableColumn<JogoDTO, String> colArbitroPrincipal;
    @FXML
    private TableColumn<JogoDTO, String> colArbitros;
    @FXML
    private Button btnAdicionarJogo;
    @FXML
    private Button btnRegistarResultado;
    @FXML
    private Button btnRemoverJogoCampeonato;
    @FXML
    private Button btnVoltar;
    @FXML
    private Label mensagemLabel;

    private static JogoDTO jogoParaResultado;

    private final JogoControllerFX controller = new JogoControllerFX();

    @FXML
    public void initialize() {
        colData.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getData()));
        colHora.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getHora()));
        colEquipaCasa.setCellValueFactory(
                cellData -> new ReadOnlyStringWrapper(cellData.getValue().getEquipaJogoCasa().getEquipa()));
        colEquipaVisitante.setCellValueFactory(
                cellData -> new ReadOnlyStringWrapper(cellData.getValue().getEquipaJogoVisitante().getEquipa()));
        colResultado.setCellValueFactory(cellData -> {
            var est = cellData.getValue().getEstatisticas();
            if (est != null) {
                int golosCasa = est.getMarcadoresEquipaCasa() != null ? est.getMarcadoresEquipaCasa().size() : 0;
                int golosVisitante = est.getMarcadoresEquipaVisitante() != null
                        ? est.getMarcadoresEquipaVisitante().size()
                        : 0;
                return new ReadOnlyStringWrapper(golosCasa + " - " + golosVisitante);
            }
            return new ReadOnlyStringWrapper("-");
        });
        colCampeonato.setCellValueFactory(cellData -> {
            String nome = cellData.getValue().getCampeonato();
            if (nome == null || nome.isBlank()) {
                nome = "jogo amigável";
            }
            return new ReadOnlyStringWrapper(nome);
        });
        colArbitroPrincipal
                .setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getArbitroPrincipal()));
        colArbitros.setCellValueFactory(cellData -> {
            List<String> arbitros = cellData.getValue().getArbitros();
            String nomes = (arbitros == null || arbitros.isEmpty()) ? "-" : String.join(", ", arbitros);
            return new ReadOnlyStringWrapper(nomes);
        });

        btnAdicionarJogo.setOnAction(e -> abrirAdicionarJogo());
        btnRegistarResultado.setOnAction(e -> abrirRegistarResultado());
        btnRemoverJogoCampeonato.setOnAction(e -> cancelarJogoCampeonato());
        btnVoltar.setOnAction(e -> voltar());

        listarJogos();
    }

    private void cancelarJogoCampeonato() {
        JogoDTO selecionado = tableJogos.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mensagemLabel.setText("Seleciona um jogo.");
            return;
        }
        // Só permite cancelar jogos de campeonato
        if (selecionado.getCampeonato() == null || selecionado.getCampeonato().isBlank()) {
            mensagemLabel.setText("Só é possível cancelar jogos de campeonato.");
            return;
        }

        if (selecionado.getEstatisticas() != null) {
            mensagemLabel.setText("Este jogo já tem resultado registado, não pode ser cancelado.");
            return;
        }

        try {
            controller.removerJogoCampeonato(selecionado.getId());
            listarJogos();
            mensagemLabel.setText("Jogo de campeonato cancelado com sucesso.");
        } catch (Exception e) {
            mensagemLabel.setText("Erro ao cancelar jogo: " + e.getMessage());
        }
    }

    private void listarJogos() {
        try {
            tableJogos.setItems(FXCollections.observableArrayList(controller.listarJogos()));
            mensagemLabel.setText("Jogos carregados.");
        } catch (Exception ex) {
            mensagemLabel.setText("Erro ao listar jogos.");
        }
    }

    private void abrirAdicionarJogo() {
        try {
            AppFX.setRoot("adicionarJogo");
        } catch (Exception e) {
            mensagemLabel.setText("Erro ao abrir página de adicionar jogo.");
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirRegistarResultado() {
        JogoDTO selecionado = tableJogos.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mensagemLabel.setText("Seleciona um jogo.");
            return;
        }
        // Bloquear se já tem resultado
        if (selecionado.getEstatisticas() != null && selecionado.getEstatisticas().getMarcadoresEquipaCasa() != null) {
            mensagemLabel.setText("Este jogo já tem resultado registado.");
            return;
        }
        jogoParaResultado = selecionado;
        try {
            RegistarResultadoController.setJogoSelecionado(selecionado); // Isso limpa estatisticasTemp
            AppFX.setRoot("registarResultado");
        } catch (Exception e) {
            mensagemLabel.setText("Erro ao abrir página de registo de resultado.");
        }
    }

    private void voltar() {
        try {
            AppFX.setRoot("menu");
        } catch (Exception e) {
            mensagemLabel.setText("Erro ao voltar ao menu.");
        }
    }

    public static JogoDTO getJogoParaResultado() {
        return jogoParaResultado;
    }
}