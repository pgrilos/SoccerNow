package pt.ul.fc.css.soccernow.javaFX.fxControllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import pt.ul.fc.css.soccernow.javaFX.AppFX;
import pt.ul.fc.css.soccernow.javaFX.fxControllersFX.EquipaControllerFX;
import pt.ul.fc.css.soccernow.dto.CampeonatoDTO;
import pt.ul.fc.css.soccernow.javaFX.fxControllersFX.CampeonatoControllerFX;
import pt.ul.fc.css.soccernow.dto.EquipaDTO;
import org.controlsfx.control.CheckListView;

import java.util.List;

public class AdicionarCampeonatoController {

    @FXML
    private TextField nomeField;
    @FXML
    private CheckListView<String> equipasCheckListView;
    @FXML
    private Button btnCriar;
    @FXML
    private Button btnVoltar;
    @FXML
    private Label mensagemLabel;

    private final EquipaControllerFX equipaController = new EquipaControllerFX();
    private final CampeonatoControllerFX campeonatoController = new CampeonatoControllerFX();

    @FXML
    public void initialize() {
        try {
            List<EquipaDTO> equipas = equipaController.listarEquipas();
            equipasCheckListView.getItems().setAll(
                    equipas.stream().map(EquipaDTO::getNome).toList());

            btnCriar.setOnAction(e -> criarCampeonato());
            btnVoltar.setOnAction(e -> voltar());
        } catch (Exception e) {
            mensagemLabel.setText("Erro ao carregar equipas.");
            e.printStackTrace();
        }
    }

    private void criarCampeonato() {
        String nome = nomeField.getText();
        List<String> equipasSelecionadas = equipasCheckListView.getCheckModel().getCheckedItems();

        if (nome.isBlank() || equipasSelecionadas.size() < 8) {
            mensagemLabel.setText("Preencha o nome e selecione pelo menos 8 equipas.");
            return;
        }

        try {
            CampeonatoDTO dto = new CampeonatoDTO();
            dto.setNome(nome);
            dto.setEquipasNomes(equipasSelecionadas);

            String erro = campeonatoController.criarCampeonato(dto);
            if (erro == null) {
                mensagemLabel.setText("Campeonato criado com sucesso!");
                AppFX.setRoot("gestaoCampeonatos");
            } else {
                mensagemLabel.setText("Erro: " + erro);
            }
        } catch (Exception e) {
            mensagemLabel.setText("Erro ao criar campeonato: " + e.getMessage());
        }
    }

    private void voltar() {
        try {
            AppFX.setRoot("gestaoCampeonatos"); // ou outro root conforme seu menu
        } catch (Exception e) {
            mensagemLabel.setText("Erro ao voltar.");
        }
    }
}