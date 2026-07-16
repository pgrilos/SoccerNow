package pt.ul.fc.css.soccernow.javaFX.fxControllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import pt.ul.fc.css.soccernow.dto.JogadorDTO;
import pt.ul.fc.css.soccernow.javaFX.AppFX;
import pt.ul.fc.css.soccernow.javaFX.fxControllersFX.UtilizadorControllerFX;
import pt.ul.fc.css.soccernow.dto.ArbitroDTO;

public class GestaoUtilizadoresController {
    @FXML
    private TableView<Object> tableUtilizadores;
    @FXML
    private TableColumn<JogadorDTO, String> colJogadorNome;
    @FXML
    private TableColumn<JogadorDTO, String> colJogadorUsername;
    @FXML
    private TableColumn<JogadorDTO, String> colJogadorPosicao;
    @FXML
    private TableColumn<ArbitroDTO, String> colArbitroNome;
    @FXML
    private TableColumn<ArbitroDTO, String> colArbitroUsername;
    @FXML
    private TableColumn<ArbitroDTO, String> colArbitroCertificado;
    @FXML
    private Button btnAdicionarJogador;
    @FXML
    private Button btnAdicionarArbitro;
    @FXML
    private Button btnListarJogadores;
    @FXML
    private Button btnListarArbitros;
    @FXML
    private Button btnAtualizar;
    @FXML
    private Button btnRemover;
    @FXML
    private Label mensagemLabel;
    @FXML
    private TableView<JogadorDTO> tableJogadores;
    @FXML
    private TableView<ArbitroDTO> tableArbitros;
    @FXML
    private Button btnVoltar;
    @FXML
    private Label labelTabela;

    private static JogadorDTO jogadorParaAtualizar;
    private static ArbitroDTO arbitroParaAtualizar;

    private final UtilizadorControllerFX controller = new UtilizadorControllerFX();

    @FXML
    public void initialize() {

        colJogadorNome.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getNome()));
        colJogadorUsername
                .setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getUsername()));
        colJogadorPosicao.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getPosicao()));
        colArbitroNome.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getNome()));
        colArbitroUsername
                .setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getUsername()));
        colArbitroCertificado.setCellValueFactory(
                cellData -> new ReadOnlyStringWrapper(cellData.getValue().getIsCertificated() ? "Sim" : "Não"));

        btnListarJogadores.setOnAction(e -> listarJogadores());
        btnListarArbitros.setOnAction(e -> listarArbitros());
        btnAtualizar.setOnAction(e -> atualizarSelecionado());
        btnRemover.setOnAction(e -> removerSelecionado());
        btnAdicionarJogador.setOnAction(e -> abrirAdicionarJogador());
        btnAdicionarArbitro.setOnAction(e -> abrirAdicionarArbitro());
        btnListarJogadores.setOnAction(e -> mostrarJogadores());
        btnListarArbitros.setOnAction(e -> mostrarArbitros());
        btnVoltar.setOnAction(e -> voltar());
    }

    private void mostrarJogadores() {
        tableJogadores.setVisible(true);
        tableJogadores.setManaged(true);
        tableArbitros.setVisible(false);
        tableArbitros.setManaged(false);
        labelTabela.setText("Jogadores");
        try {
            tableJogadores.setItems(FXCollections.observableArrayList(controller.listarJogadores()));
        } catch (Exception ex) {
            if (ex.getMessage() != null && ex.getMessage().contains("Nenhum jogador encontrado")) {
                tableJogadores.setItems(FXCollections.observableArrayList());
                mensagemLabel.setText("Nenhum jogador encontrado.");
            } else {
                mensagemLabel.setText("Erro ao listar jogadores.");
                ex.printStackTrace();
            }
        }
    }

    private void mostrarArbitros() {
        tableArbitros.setVisible(true);
        tableArbitros.setManaged(true);
        tableJogadores.setVisible(false);
        tableJogadores.setManaged(false);
        labelTabela.setText("Árbitros");
        try {
            tableArbitros.setItems(FXCollections.observableArrayList(controller.listarArbitros()));
        } catch (Exception ex) {
            mensagemLabel.setText("Erro ao listar árbitros.");
        }
    }

    private void abrirAdicionarJogador() {
        try {
            pt.ul.fc.css.soccernow.javaFX.AppFX.setRoot("adicionarJogador");
        } catch (Exception e) {
            mensagemLabel.setText("Erro ao abrir página de adicionar jogador.");
        }
    }

    private void abrirAdicionarArbitro() {
        try {
            pt.ul.fc.css.soccernow.javaFX.AppFX.setRoot("adicionarArbitro");
        } catch (Exception e) {
            mensagemLabel.setText("Erro ao abrir página de adicionar árbitro.");
        }
    }

    private void listarJogadores() {
        try {
            tableUtilizadores.setItems(FXCollections.observableArrayList(controller.listarJogadores()));
            mensagemLabel.setText("Jogadores carregados.");
        } catch (Exception ex) {
            mensagemLabel.setText("Erro ao listar jogadores.");
        }
    }

    private void listarArbitros() {
        try {
            tableUtilizadores.setItems(FXCollections.observableArrayList(controller.listarArbitros()));
            mensagemLabel.setText("Árbitros carregados.");
        } catch (Exception ex) {
            mensagemLabel.setText("Erro ao listar árbitros.");
        }
    }

    private void atualizarSelecionado() {
        if (tableJogadores.isVisible()) {
            JogadorDTO jogador = tableJogadores.getSelectionModel().getSelectedItem();
            if (jogador == null) {
                mensagemLabel.setText("Seleciona um jogador.");
                return;
            }
            try {
                jogadorParaAtualizar = jogador;
                AppFX.setRoot("atualizarJogador");
            } catch (Exception ex) {
                mensagemLabel.setText("Erro ao abrir página de atualização.");
            }
        } else if (tableArbitros.isVisible()) {
            ArbitroDTO arbitro = tableArbitros.getSelectionModel().getSelectedItem();
            if (arbitro == null) {
                mensagemLabel.setText("Seleciona um árbitro.");
                return;
            }
            try {
                arbitroParaAtualizar = arbitro;
                AppFX.setRoot("atualizarArbitro");
            } catch (Exception ex) {
                mensagemLabel.setText("Erro ao abrir página de atualização.");
            }
        }
    }

    // Getters para os controllers de atualização acederem ao objeto selecionado:
    public static JogadorDTO getJogadorParaAtualizar() {
        return jogadorParaAtualizar;
    }

    public static ArbitroDTO getArbitroParaAtualizar() {
        return arbitroParaAtualizar;
    }

    private void removerSelecionado() {
        if (tableJogadores.isVisible()) {
            JogadorDTO jogador = tableJogadores.getSelectionModel().getSelectedItem();
            if (jogador == null) {
                mensagemLabel.setText("Seleciona um jogador.");
                return;
            }
            try {
                controller.eliminarJogador(jogador.getId());
                mensagemLabel.setText("Jogador removido!");
                mostrarJogadores();
            } catch (Exception ex) {
                mensagemLabel.setText("Erro ao remover jogador.");
            }
        } else if (tableArbitros.isVisible()) {
            ArbitroDTO arbitro = tableArbitros.getSelectionModel().getSelectedItem();
            if (arbitro == null) {
                mensagemLabel.setText("Seleciona um árbitro.");
                return;
            }
            try {
                controller.eliminarArbitro(arbitro.getId());
                mensagemLabel.setText("Árbitro removido!");
                mostrarArbitros();
            } catch (Exception ex) {
                mensagemLabel.setText("Erro ao remover árbitro.");
            }
        }
    }

    private void voltar() {
        try {
            AppFX.setRoot("menu");
        } catch (Exception e) {
            mensagemLabel.setText("Erro ao voltar.");
        }
    }
}