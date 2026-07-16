package pt.ul.fc.css.soccernow.javaFX.fxControllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.beans.property.ReadOnlyStringWrapper;
import pt.ul.fc.css.soccernow.dto.EquipaDTO;
import pt.ul.fc.css.soccernow.javaFX.AppFX;
import pt.ul.fc.css.soccernow.javaFX.fxControllersFX.EquipaControllerFX;

public class GestaoEquipasController {
    @FXML
    private TableView<EquipaDTO> tableEquipas;
    @FXML
    private TableColumn<EquipaDTO, String> colNome;
    @FXML
    private TableColumn<EquipaDTO, String> colJogadores;
    @FXML
    private Button btnAdicionarEquipa;
    @FXML
    private Button btnAtualizarEquipa;
    @FXML
    private Button btnRemoverEquipa;
    @FXML
    private Button btnListarEquipas;
    @FXML
    private Button btnVoltar;
    @FXML
    private Label mensagemLabel;

    private static EquipaDTO equipaParaAtualizar;
    private final EquipaControllerFX controller = new EquipaControllerFX();

    @FXML
    public void initialize() {
        colNome.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getNome()));
        colJogadores.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(
                cellData.getValue().getJogadores() != null ? String.join(", ", cellData.getValue().getJogadores())
                        : ""));

        btnAdicionarEquipa.setOnAction(e -> abrirAdicionarEquipa());
        btnAtualizarEquipa.setOnAction(e -> atualizarSelecionada());
        btnRemoverEquipa.setOnAction(e -> removerSelecionada());
        btnListarEquipas.setOnAction(e -> listarEquipas());
        btnVoltar.setOnAction(e -> voltar());

        listarEquipas();
    }

    private void abrirAdicionarEquipa() {
        try {
            AppFX.setRoot("adicionarEquipa");
        } catch (Exception e) {
            e.printStackTrace();
            mensagemLabel.setText("Erro ao abrir página de adicionar equipa.");
        }
    }

    private void atualizarSelecionada() {
        EquipaDTO equipa = tableEquipas.getSelectionModel().getSelectedItem();
        if (equipa == null) {
            mensagemLabel.setText("Seleciona uma equipa.");
            return;
        }
        try {
            equipaParaAtualizar = equipa;
            AppFX.setRoot("atualizarEquipa");
        } catch (Exception ex) {
            mensagemLabel.setText("Erro ao abrir página de atualização.");
        }
    }

    private void removerSelecionada() {
        EquipaDTO equipa = tableEquipas.getSelectionModel().getSelectedItem();
        if (equipa == null) {
            mensagemLabel.setText("Seleciona uma equipa.");
            return;
        }
        try {
            controller.eliminarEquipa(equipa.getId());
            mensagemLabel.setText("Equipa removida!");
            listarEquipas();
        } catch (Exception ex) {
            mensagemLabel.setText("Erro ao remover equipa.");
        }
    }

    private void listarEquipas() {
        try {
            tableEquipas.setItems(FXCollections.observableArrayList(controller.listarEquipas()));
            mensagemLabel.setText("Equipas carregadas.");
        } catch (Exception ex) {
            mensagemLabel.setText("Erro ao listar equipas.");
        }
    }

    // Getter para controller de atualização
    public static EquipaDTO getEquipaParaAtualizar() {
        return equipaParaAtualizar;
    }

    private void voltar() {
        try {
            AppFX.setRoot("menu");
        } catch (Exception e) {
            mensagemLabel.setText("Erro ao voltar ao menu.");
        }
    }
}