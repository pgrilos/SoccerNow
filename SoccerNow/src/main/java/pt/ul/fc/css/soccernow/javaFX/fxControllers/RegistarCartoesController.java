package pt.ul.fc.css.soccernow.javaFX.fxControllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pt.ul.fc.css.soccernow.dto.*;
import pt.ul.fc.css.soccernow.javaFX.AppFX;
import pt.ul.fc.css.soccernow.javaFX.fxControllersFX.UtilizadorControllerFX;

import java.util.ArrayList;
import java.util.List;


public class RegistarCartoesController {

    @FXML
    private TableView<CartaoDTO> tableCartoes;
    @FXML
    private TableColumn<CartaoDTO, String> colCartaoCor;
    @FXML
    private TableColumn<CartaoDTO, String> colCartaoJogador;
    @FXML
    private TableColumn<CartaoDTO, String> colCartaoEquipa;
    @FXML
    private TableColumn<CartaoDTO, String> colCartaoArbitro;
    @FXML
    private ComboBox<String> comboCartaoEquipa;
    @FXML
    private ComboBox<String> comboCartaoJogador;
    @FXML
    private ComboBox<String> comboCartaoCor;
    @FXML
    private Button btnAdicionarCartao;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnVoltar;
    @FXML
    private Button btnRemoverCartao;
    @FXML
    private Label mensagemLabel;

    private static EstatisticasDTO estatisticasTemp;
    private static JogoDTO jogoSelecionado;
    private ObservableList<CartaoDTO> cartoes = FXCollections.observableArrayList();
    private final UtilizadorControllerFX utilizadorController = new UtilizadorControllerFX();

    public static void setEstatisticasTemp(EstatisticasDTO estatisticas) {
        estatisticasTemp = estatisticas;
    }

    public static void setJogoSelecionado(JogoDTO jogo) {
        jogoSelecionado = jogo;
    }

    @FXML
    public void initialize() {
        cartoes.clear();
        if (estatisticasTemp != null && estatisticasTemp.getCartoes() != null) {
            cartoes.addAll(estatisticasTemp.getCartoes());
        }
        tableCartoes.setItems(cartoes);
        colCartaoCor.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCorCartao()));
        colCartaoJogador.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getJogador()));
        colCartaoEquipa.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEquipa()));
        colCartaoArbitro.setCellValueFactory(data -> new SimpleStringProperty(
                jogoSelecionado != null ? jogoSelecionado.getArbitroPrincipal() : ""));

        // Preencher combo de equipas do jogo
        if (jogoSelecionado != null) {
            comboCartaoEquipa.setItems(FXCollections.observableArrayList(
                    jogoSelecionado.getEquipaJogoCasa().getEquipa(),
                    jogoSelecionado.getEquipaJogoVisitante().getEquipa()));
        }

        comboCartaoCor.setItems(FXCollections.observableArrayList("Amarelo", "Vermelho"));

        // Listener: ao selecionar equipa, preenche jogadores dessa equipa
        comboCartaoEquipa.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (jogoSelecionado == null || newVal == null) {
                comboCartaoJogador.setItems(FXCollections.observableArrayList());
                return;
            }
            List<String> jogadores = new ArrayList<>();
            if (newVal.equals(jogoSelecionado.getEquipaJogoCasa().getEquipa())) {
                jogadores = jogoSelecionado.getEquipaJogoCasa().getJogadoresParticipantes();
            } else if (newVal.equals(jogoSelecionado.getEquipaJogoVisitante().getEquipa())) {
                jogadores = jogoSelecionado.getEquipaJogoVisitante().getJogadoresParticipantes();
            }
            comboCartaoJogador.setItems(FXCollections.observableArrayList(jogadores));
        });

        btnAdicionarCartao.setOnAction(e -> adicionarCartao());
        btnRemoverCartao.setOnAction(e -> removerCartaoSelecionado());
        btnGuardar.setOnAction(e -> guardarCartoesEVoltar());
        btnVoltar.setOnAction(e -> voltar());
    }

    private void adicionarCartao() {
        String equipa = comboCartaoEquipa.getValue();
        String jogador = comboCartaoJogador.getValue();
        String cor = comboCartaoCor.getValue();
        if (equipa == null || jogador == null || cor == null)
            return;
        // Verifica se já existe cartão para este jogador
        boolean jaTemCartao = cartoes.stream().anyMatch(c -> c.getJogador().equals(jogador));
        if (jaTemCartao) {
            mensagemLabel.setText("Este jogador já tem um cartão atribuído!");
            return;
        }
        CartaoDTO cartao = new CartaoDTO();
        cartao.setCorCartao(cor);
        cartao.setJogador(jogador);
        cartao.setEquipa(equipa);
        if (jogoSelecionado != null) {
            try {
                String arbritoNome = jogoSelecionado.getArbitroPrincipal();
                List<ArbitroDTO> arbitros = utilizadorController.listarArbitros();
                ArbitroDTO arbitroPrincipal = arbitros.stream()
                        .filter(a -> a.getUsername().equals(arbritoNome)).findFirst().get();
                cartao.setArbitroId(arbitroPrincipal.getId());
            } catch (Exception ex) {
                mensagemLabel.setText("Erro ao listar árbitros.");
                ex.printStackTrace();
            }

        }
        cartoes.add(cartao);
    }

    private void removerCartaoSelecionado() {
        CartaoDTO selecionado = tableCartoes.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            cartoes.remove(selecionado);
        }
    }

    private void guardarCartoesEVoltar() {
        if (estatisticasTemp == null) {
            mensagemLabel.setText("Dados em falta.");
            return;
        }
        estatisticasTemp.setCartoes(new ArrayList<>(cartoes));
        // Volta à página de registo de resultado, mantendo os dados
        RegistarResultadoController.setEstatisticasTemp(estatisticasTemp);
        RegistarResultadoController.setJogoSelecionado(jogoSelecionado);
        try {
            AppFX.setRoot("registarResultado");
        } catch (Exception e) {
            mensagemLabel.setText("Erro ao voltar.");
        }
    }

    private void voltar() {
        // Volta sem guardar cartões novos
        RegistarResultadoController.setEstatisticasTemp(estatisticasTemp);
        RegistarResultadoController.setJogoSelecionado(jogoSelecionado);
        try {
            AppFX.setRoot("registarResultado");
        } catch (Exception e) {
            mensagemLabel.setText("Erro ao voltar.");
        }
    }
}