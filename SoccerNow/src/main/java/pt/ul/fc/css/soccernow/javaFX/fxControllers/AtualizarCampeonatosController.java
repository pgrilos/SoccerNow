package pt.ul.fc.css.soccernow.javaFX.fxControllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pt.ul.fc.css.soccernow.dto.CampeonatoDTO;
import pt.ul.fc.css.soccernow.dto.EquipaDTO;
import pt.ul.fc.css.soccernow.javaFX.AppFX;
import pt.ul.fc.css.soccernow.javaFX.fxControllersFX.CampeonatoControllerFX;
import pt.ul.fc.css.soccernow.javaFX.fxControllersFX.EquipaControllerFX;

import java.util.List;
import java.util.stream.Collectors;

public class AtualizarCampeonatosController {
    @FXML
    private TextField nomeField;
    @FXML
    private ListView<String> listEquipasCampeonato;
    @FXML
    private ComboBox<String> comboEquipasDisponiveis;
    @FXML
    private Button btnAdicionarEquipa;
    @FXML
    private Button btnRemoverEquipa;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnVoltar;
    @FXML
    private Label mensagemLabel;

    private CampeonatoDTO campeonato;
    private final CampeonatoControllerFX campeonatoController = new CampeonatoControllerFX();
    private final EquipaControllerFX equipaController = new EquipaControllerFX();

    private ObservableList<String> equipasCampeonato = FXCollections.observableArrayList();
    private ObservableList<String> equipasDisponiveis = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        try {
            // Obter o campeonato a atualizar (ajuste conforme seu controller de gestão)
            campeonato = pt.ul.fc.css.soccernow.javaFX.fxControllers.GestaoCampeonatosController.getCampeonatoParaAtualizar();

            // Preencher o campo do nome
            if (campeonato != null) {
                nomeField.setText(campeonato.getNome());
                if (campeonato.getEquipasNomes() != null) {
                    equipasCampeonato.setAll(campeonato.getEquipasNomes());
                }
            }

            // Carregar todas as equipas disponíveis
            List<EquipaDTO> todasEquipas = equipaController.listarEquipas();
            List<String> nomesEquipas = todasEquipas.stream().map(EquipaDTO::getNome).toList();
            equipasDisponiveis.setAll(nomesEquipas);
            equipasDisponiveis.removeAll(equipasCampeonato);

            comboEquipasDisponiveis.setItems(equipasDisponiveis);
            listEquipasCampeonato.setItems(equipasCampeonato);

            btnAdicionarEquipa.setOnAction(e -> adicionarEquipa());
            btnRemoverEquipa.setOnAction(e -> removerEquipa());
            btnGuardar.setOnAction(e -> guardar());
            btnVoltar.setOnAction(e -> voltar());
        } catch (Exception ex) {
            mensagemLabel.setText("Erro ao carregar equipas.");
        }
    }

    private void adicionarEquipa() {
        String equipa = comboEquipasDisponiveis.getValue();
        if (equipa != null && !equipasCampeonato.contains(equipa)) {
            equipasCampeonato.add(equipa);
            equipasDisponiveis.remove(equipa);
        }
    }

    private void removerEquipa() {
        String equipa = listEquipasCampeonato.getSelectionModel().getSelectedItem();
        if (equipa != null) {
            equipasCampeonato.remove(equipa);
            equipasDisponiveis.add(equipa);
        }
    }

    private void guardar() {
        String nome = nomeField.getText();
        if (nome.isEmpty()) {
            mensagemLabel.setText("Preenche o nome.");
            return;
        }
        if (equipasCampeonato.size() < 8) {
            mensagemLabel.setText("O campeonato deve ter pelo menos 8 equipas.");
            return;
        }
        try {
            if (campeonato == null)
                campeonato = new CampeonatoDTO();
            campeonato.setNome(nome);
            campeonato.setEquipasNomes(equipasCampeonato.stream().collect(Collectors.toList()));

            campeonatoController.atualizarCampeonato(campeonato.getId(), campeonato);
            mensagemLabel.setText("Campeonato atualizado!");
            AppFX.setRoot("gestaoCampeonatos");
        } catch (Exception ex) {
            mensagemLabel.setText("Erro ao guardar campeonato.");
        }
    }

    private void voltar() {
        try {
            AppFX.setRoot("gestaoCampeonatos");
        } catch (Exception e) {
            mensagemLabel.setText("Erro ao voltar.");
        }
    }
}