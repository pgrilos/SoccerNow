package pt.ul.fc.css.soccernow.javaFX.fxControllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.beans.property.ReadOnlyStringWrapper;
import pt.ul.fc.css.soccernow.dto.CampeonatoDTO;
import pt.ul.fc.css.soccernow.javaFX.AppFX;
import pt.ul.fc.css.soccernow.javaFX.fxControllersFX.CampeonatoControllerFX;

public class GestaoCampeonatosController {
    @FXML
    private TableView<CampeonatoDTO> tableCampeonatos;
    @FXML
    private TableColumn<CampeonatoDTO, String> colNome;
    @FXML
    private TableColumn<CampeonatoDTO, String> colEquipas;
    @FXML
    private Button btnAdicionarCampeonato;
    @FXML
    private Button btnAtualizarCampeonato;
    @FXML
    private Button btnRemoverCampeonato;
    @FXML
    private Button btnListarCampeonatos;
    @FXML
    private Button btnVoltar;
    @FXML
    private Label mensagemLabel;

    private static CampeonatoDTO campeonatoParaAtualizar;
    private final CampeonatoControllerFX controller = new CampeonatoControllerFX();

    @FXML
    public void initialize() {
        colNome.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getNome()));
        colEquipas.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(
                cellData.getValue().getEquipasNomes() != null ? String.join(", ", cellData.getValue().getEquipasNomes())
                        : ""));

        btnAdicionarCampeonato.setOnAction(e -> abrirAdicionarCampeonato());
        btnAtualizarCampeonato.setOnAction(e -> atualizarSelecionado());
        btnRemoverCampeonato.setOnAction(e -> removerSelecionado());
        btnVoltar.setOnAction(e -> voltar());

        listarCampeonatos();
    }

    private void abrirAdicionarCampeonato() {
        try {
            AppFX.setRoot("adicionarCampeonato");
        } catch (Exception e) {
            mensagemLabel.setText("Erro ao abrir página de adicionar campeonato.");
        }
    }

    private void atualizarSelecionado() {
        CampeonatoDTO campeonato = tableCampeonatos.getSelectionModel().getSelectedItem();
        if (campeonato == null) {
            mensagemLabel.setText("Seleciona um campeonato.");
            return;
        }
        try {
            campeonatoParaAtualizar = campeonato;
            AppFX.setRoot("atualizarCampeonato");
        } catch (Exception ex) {
            mensagemLabel.setText("Erro ao abrir página de atualização.");
        }
    }

    private void removerSelecionado() {
        CampeonatoDTO campeonato = tableCampeonatos.getSelectionModel().getSelectedItem();
        if (campeonato == null) {
            mensagemLabel.setText("Seleciona um campeonato.");
            return;
        }
        try {
            controller.removerCampeonato(campeonato.getId());
            mensagemLabel.setText("Campeonato removido!");
            listarCampeonatos();
        } catch (Exception ex) {
            mensagemLabel.setText("Erro ao remover campeonato.");
        }
    }

    private void listarCampeonatos() {
        try {
            tableCampeonatos.setItems(FXCollections.observableArrayList(controller.listarCampeonatos()));
            mensagemLabel.setText("Campeonatos carregados.");
        } catch (Exception ex) {
            mensagemLabel.setText("Erro ao listar campeonatos.");
        }
    }

    // Getter para controller de atualização
    public static CampeonatoDTO getCampeonatoParaAtualizar() {
        return campeonatoParaAtualizar;
    }

    private void voltar() {
        try {
            AppFX.setRoot("menu");
        } catch (Exception e) {
            mensagemLabel.setText("Erro ao voltar ao menu.");
        }
    }
}