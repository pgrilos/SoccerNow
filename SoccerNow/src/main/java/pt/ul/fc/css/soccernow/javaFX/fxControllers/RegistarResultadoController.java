package pt.ul.fc.css.soccernow.javaFX.fxControllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pt.ul.fc.css.soccernow.dto.*;
import pt.ul.fc.css.soccernow.javaFX.AppFX;
import pt.ul.fc.css.soccernow.javaFX.fxControllersFX.JogoControllerFX;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RegistarResultadoController {

    @FXML
    private TableView<JogadorRow> tableMarcadoresCasa;
    @FXML
    private TableColumn<JogadorRow, String> colJogadorCasa;
    @FXML
    private TableColumn<JogadorRow, Integer> colGolosCasa;
    @FXML
    private TableView<JogadorRow> tableMarcadoresVisitante;
    @FXML
    private TableColumn<JogadorRow, String> colJogadorVisitante;
    @FXML
    private TableColumn<JogadorRow, Integer> colGolosVisitante;
    @FXML
    private TableColumn<JogadorRow, String> colCartoesCasa;
    @FXML
    private TableColumn<JogadorRow, String> colCartoesVisitante;
    @FXML
    private Button btnAvancarCartoes;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnVoltar;
    @FXML
    private Label mensagemLabel;

    private final JogoControllerFX jogoController = new JogoControllerFX();
    private static EstatisticasDTO estatisticasTemp;
    private static JogoDTO jogoSelecionado;

    public static class JogadorRow {
        private final SimpleStringProperty jogador;
        private final SimpleIntegerProperty golos;
        private final SimpleStringProperty cartao;

        public JogadorRow(String jogador) {
            this.jogador = new SimpleStringProperty(jogador);
            this.golos = new SimpleIntegerProperty(0);
            this.cartao = new SimpleStringProperty(""); // inicia vazio

        }

        public String getJogador() {
            return jogador.get();
        }

        public void setGolos(int g) {
            golos.set(g);
        }

        public int getGolos() {
            return golos.get();
        }

        // ... getters e setters ...
        public String getCartao() {
            return cartao.get();
        }

        public void setCartao(String c) {
            cartao.set(c);
        }

        public SimpleStringProperty cartaoProperty() {
            return cartao;
        }

        public SimpleIntegerProperty golosProperty() {
            return golos;
        }
    }

    public static void setEstatisticasTemp(EstatisticasDTO estatisticas) {
        estatisticasTemp = estatisticas;
    }

    public static void setJogoSelecionado(JogoDTO jogo) {
        // Só limpa se for um jogo diferente
        if (jogoSelecionado == null || !jogoSelecionado.getId().equals(jogo.getId())) {
            estatisticasTemp = null;
        }
        jogoSelecionado = jogo;
    }

    @FXML
    public void initialize() {
        jogoSelecionado = GestaoJogosController.getJogoParaResultado();
        if (jogoSelecionado == null) {
            mensagemLabel.setText("Nenhum jogo selecionado.");
            setCamposAtivos(false);
            return;
        }
        // Bloquear se já tem resultado
        if (jogoSelecionado.getEstatisticas() != null
                && jogoSelecionado.getEstatisticas().getMarcadoresEquipaCasa() != null) {
            mensagemLabel.setText("Este jogo já tem resultado registado e não pode ser editado.");
            setCamposAtivos(false);
            return;
        }
        setCamposAtivos(true);
        mensagemLabel.setText("");

        ObservableList<JogadorRow> casaRows = FXCollections.observableArrayList();
        for (String jogador : jogoSelecionado.getEquipaJogoCasa().getJogadoresParticipantes()) {
            casaRows.add(new JogadorRow(jogador));
        }
        tableMarcadoresCasa.setItems(casaRows);

        ObservableList<JogadorRow> visitanteRows = FXCollections.observableArrayList();
        for (String jogador : jogoSelecionado.getEquipaJogoVisitante().getJogadoresParticipantes()) {
            visitanteRows.add(new JogadorRow(jogador));
        }
        tableMarcadoresVisitante.setItems(visitanteRows);

        if (estatisticasTemp != null) {
            // Restaurar golos equipa casa
            if (estatisticasTemp.getMarcadoresEquipaCasa() != null) {
                for (JogadorRow row : casaRows) {
                    int count = (int) estatisticasTemp.getMarcadoresEquipaCasa().stream()
                            .filter(j -> j.equals(row.getJogador()))
                            .count();
                    row.setGolos(count);
                }
            }
            // Restaurar golos equipa visitante
            if (estatisticasTemp.getMarcadoresEquipaVisitante() != null) {
                for (JogadorRow row : visitanteRows) {
                    int count = (int) estatisticasTemp.getMarcadoresEquipaVisitante().stream()
                            .filter(j -> j.equals(row.getJogador()))
                            .count();
                    row.setGolos(count);
                }
            }
        }

        if (estatisticasTemp != null && estatisticasTemp.getCartoes() != null) {
            for (CartaoDTO cartao : estatisticasTemp.getCartoes()) {
                // Casa
                for (JogadorRow row : casaRows) {
                    if (row.getJogador().equals(cartao.getJogador())) {
                        String prev = row.getCartao();
                        row.setCartao((prev.isEmpty() ? "" : prev + ", ") + cartao.getCorCartao());
                    }
                }
                // Visitante
                for (JogadorRow row : visitanteRows) {
                    if (row.getJogador().equals(cartao.getJogador())) {
                        String prev = row.getCartao();
                        row.setCartao((prev.isEmpty() ? "" : prev + ", ") + cartao.getCorCartao());
                    }
                }
            }
        }

        colJogadorCasa.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getJogador()));
        colGolosCasa.setCellValueFactory(data -> data.getValue().golosProperty().asObject());
        colGolosCasa.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        tableMarcadoresCasa.setEditable(true);

        colJogadorVisitante.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getJogador()));
        colGolosVisitante.setCellValueFactory(data -> data.getValue().golosProperty().asObject());
        colGolosVisitante.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        tableMarcadoresVisitante.setEditable(true);

        colCartoesCasa.setCellValueFactory(data -> data.getValue().cartaoProperty());
        colCartoesVisitante.setCellValueFactory(data -> data.getValue().cartaoProperty());

        btnGuardar.setOnAction(e -> guardarResultado());
        btnAvancarCartoes.setOnAction(e -> avancarParaCartoes());
        btnVoltar.setOnAction(e -> voltar());
    }

    private void setCamposAtivos(boolean ativo) {
        tableMarcadoresCasa.setDisable(!ativo);
        tableMarcadoresVisitante.setDisable(!ativo);
        btnGuardar.setDisable(!ativo);
        btnAvancarCartoes.setDisable(!ativo);
    }

    private void guardarResultado() {
        if (jogoSelecionado == null) {
            mensagemLabel.setText("Nenhum jogo selecionado.");
            return;
        }
        if (LocalDate.parse(jogoSelecionado.getData()).isAfter(LocalDate.now())) {
            mensagemLabel.setText("Não é possível registar resultado de um jogo futuro.");
            return;
        }

        EstatisticasDTO estatisticas = new EstatisticasDTO();
        estatisticas.setMarcadoresEquipaCasa(getMarcadores(tableMarcadoresCasa));
        estatisticas.setMarcadoresEquipaVisitante(getMarcadores(tableMarcadoresVisitante));
        estatisticas.setCartoes(new ArrayList<>());
        if (estatisticasTemp != null && estatisticasTemp.getCartoes() != null) {
            estatisticas.setCartoes(new ArrayList<>(estatisticasTemp.getCartoes()));
        } else {
            estatisticas.setCartoes(new ArrayList<>());
        }

        try {
            jogoController.registarResultado(jogoSelecionado.getId(), estatisticas);
            mensagemLabel.setText("Resultado registado!");
            AppFX.setRoot("gestaoJogos");
        } catch (Exception e) {
            mensagemLabel.setText("Erro ao registar resultado: " + e.getMessage());
        }
    }

    private List<String> getMarcadores(TableView<JogadorRow> table) {
        List<String> marcadores = new ArrayList<>();
        for (JogadorRow row : table.getItems()) {
            for (int i = 0; i < row.getGolos(); i++) {
                marcadores.add(row.getJogador());
            }
        }
        return marcadores;
    }

    private void avancarParaCartoes() {
        if (estatisticasTemp == null) {
            estatisticasTemp = new EstatisticasDTO();
        }
        estatisticasTemp.setMarcadoresEquipaCasa(getMarcadores(tableMarcadoresCasa));
        estatisticasTemp.setMarcadoresEquipaVisitante(getMarcadores(tableMarcadoresVisitante));
        RegistarCartoesController.setEstatisticasTemp(estatisticasTemp);
        RegistarCartoesController.setJogoSelecionado(jogoSelecionado);
        try {
            AppFX.setRoot("registarCartoes");
        } catch (Exception e) {
            mensagemLabel.setText("Erro ao avançar para cartões.");
            e.printStackTrace();
        }
    }

    private void voltar() {
        estatisticasTemp = null;
        jogoSelecionado = null;
        try {
            AppFX.setRoot("gestaoJogos");
        } catch (Exception e) {
            mensagemLabel.setText("Erro ao voltar.");
        }
    }
}