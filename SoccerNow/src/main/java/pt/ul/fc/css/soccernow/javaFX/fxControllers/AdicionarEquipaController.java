package pt.ul.fc.css.soccernow.javaFX.fxControllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pt.ul.fc.css.soccernow.dto.EquipaDTO;
import pt.ul.fc.css.soccernow.dto.JogadorDTO;
import pt.ul.fc.css.soccernow.javaFX.AppFX;
import pt.ul.fc.css.soccernow.javaFX.fxControllersFX.EquipaControllerFX;
import pt.ul.fc.css.soccernow.javaFX.fxControllersFX.UtilizadorControllerFX;

import java.util.List;
import java.util.stream.Collectors;

public class AdicionarEquipaController {
    @FXML
    private TextField nomeField;
    @FXML
    private ListView<String> listJogadoresEquipa;
    @FXML
    private ComboBox<String> comboJogadoresDisponiveis;
    @FXML
    private Button btnAdicionarJogador;
    @FXML
    private Button btnRemoverJogador;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnVoltar;
    @FXML
    private Label mensagemLabel;
    @FXML
    private Label labelPosicaoJogador;

    private List<JogadorDTO> todosJogadores;

    private final EquipaControllerFX controller = new EquipaControllerFX();
    private final UtilizadorControllerFX utilizadorController = new UtilizadorControllerFX();

    private ObservableList<String> jogadoresEquipa = FXCollections.observableArrayList();
    private ObservableList<String> jogadoresDisponiveis = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        try {
            // Carregar todos os jogadores disponíveis
            todosJogadores = utilizadorController.listarJogadores();
            jogadoresDisponiveis
                    .setAll(todosJogadores.stream().map(JogadorDTO::getUsername).collect(Collectors.toList()));
            comboJogadoresDisponiveis.setItems(jogadoresDisponiveis);
            listJogadoresEquipa.setItems(jogadoresEquipa);

            // Listener para mostrar a posição do jogador selecionado
            comboJogadoresDisponiveis.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    JogadorDTO jogador = todosJogadores.stream()
                            .filter(j -> j.getUsername().equals(newVal))
                            .findFirst()
                            .orElse(null);
                    if (jogador != null) {
                        labelPosicaoJogador.setText("Posição: " + jogador.getPosicao());
                    } else {
                        labelPosicaoJogador.setText("Posição: ");
                    }
                } else {
                    labelPosicaoJogador.setText("Posição: ");
                }
            });

            btnAdicionarJogador.setOnAction(e -> adicionarJogador());
            btnRemoverJogador.setOnAction(e -> removerJogador());
            btnGuardar.setOnAction(e -> guardar());
            btnVoltar.setOnAction(e -> voltar());
        } catch (Exception ex) {
            mensagemLabel.setText("Erro ao carregar jogadores.");
        }
    }

    private void adicionarJogador() {
        String jogador = comboJogadoresDisponiveis.getValue();
        if (jogador != null && !jogadoresEquipa.contains(jogador)) {
            jogadoresEquipa.add(jogador);
            jogadoresDisponiveis.remove(jogador);
        }
    }

    private void removerJogador() {
        String jogador = listJogadoresEquipa.getSelectionModel().getSelectedItem();
        if (jogador != null) {
            jogadoresEquipa.remove(jogador);
            jogadoresDisponiveis.add(jogador);
        }
    }

    private void guardar() {
        String nome = nomeField.getText();
        if (nome.isEmpty()) {
            mensagemLabel.setText("Preenche o nome.");
            return;
        }
        try {
            EquipaDTO dto = new EquipaDTO();
            dto.setNome(nome);
            dto.setJogadores(jogadoresEquipa.stream().collect(Collectors.toList()));
            controller.registarEquipa(dto);
            mensagemLabel.setText("Equipa registada!");
            AppFX.setRoot("gestaoEquipas");
        } catch (Exception ex) {
            mensagemLabel.setText("Erro ao registar equipa.");
        }
    }

    private void voltar() {
        try {
            AppFX.setRoot("gestaoEquipas");
        } catch (Exception e) {
            mensagemLabel.setText("Erro ao voltar.");
        }
    }
}