package pt.ul.fc.css.soccernow.javaFX.fxControllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.controlsfx.control.CheckListView;
import javafx.collections.FXCollections;
import pt.ul.fc.css.soccernow.dto.*;
import pt.ul.fc.css.soccernow.javaFX.AppFX;
import pt.ul.fc.css.soccernow.javaFX.fxControllersFX.CampeonatoControllerFX;
import pt.ul.fc.css.soccernow.javaFX.fxControllersFX.EquipaControllerFX;
import pt.ul.fc.css.soccernow.javaFX.fxControllersFX.JogoControllerFX;
import pt.ul.fc.css.soccernow.javaFX.fxControllersFX.UtilizadorControllerFX;

import java.util.ArrayList;
import java.util.List;

public class AdicionarJogoController {

    @FXML
    private TextField dataField;
    @FXML
    private TextField horaField;
    @FXML
    private TextField ruaField;
    @FXML
    private TextField cidadeField;
    @FXML
    private ComboBox<String> equipaCasaCombo;
    @FXML
    private ComboBox<String> equipaVisitanteCombo;
    @FXML
    private ComboBox<String> arbitroCombo;
    @FXML
    private ComboBox<String> campeonatoCombo;
    @FXML
    private Button btnCriar;
    @FXML
    private Button btnVoltar;
    @FXML
    private CheckListView<String> checkListJogadoresCasa;
    @FXML
    private CheckListView<String> checkListJogadoresVisitante;
    @FXML
    private CheckListView<String> checkListArbitros;
 

    @FXML
    private Label mensagemLabel;
    private final JogoControllerFX jogoController = new JogoControllerFX();
    private final EquipaControllerFX equipaController = new EquipaControllerFX();
    private final UtilizadorControllerFX utilizadorController = new UtilizadorControllerFX();
    private final CampeonatoControllerFX campeonatoController = new CampeonatoControllerFX();
    private List<CampeonatoDTO> todosCampeonatos = new ArrayList<>();


    @FXML
    public void initialize() {
        try {
            List<EquipaDTO> equipas = equipaController.listarEquipas();
            equipaCasaCombo
                    .setItems(FXCollections.observableArrayList(equipas.stream().map(EquipaDTO::getNome).toList()));
            equipaVisitanteCombo
                    .setItems(FXCollections.observableArrayList(equipas.stream().map(EquipaDTO::getNome).toList()));

            equipaCasaCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
                carregarJogadoresEquipa(newVal, checkListJogadoresCasa);
                filtrarCampeonatos();
            });

            equipaVisitanteCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
                carregarJogadoresEquipa(newVal, checkListJogadoresVisitante);
                filtrarCampeonatos();
            });

            //Carregar Arbitros
            List<String> arbitros = utilizadorController.listarArbitros().stream().map(arbitro -> arbitro.getUsername())
                    .toList();
            arbitroCombo.setItems(FXCollections.observableArrayList(arbitros));
            checkListArbitros.getItems().setAll(arbitros);

            //Carregar Campeonatos
            List<String> nomesCampeonatos = new java.util.ArrayList<>();
            nomesCampeonatos.add("jogo amigável");
            todosCampeonatos = campeonatoController.listarCampeonatos();todosCampeonatos = campeonatoController.listarCampeonatos();
            nomesCampeonatos.addAll(todosCampeonatos.stream().map(CampeonatoDTO::getNome).toList());
            campeonatoCombo.setItems(FXCollections.observableArrayList(nomesCampeonatos));
            campeonatoCombo.getSelectionModel().selectFirst(); // seleciona "Sem campeonato" por padrão

            btnCriar.setOnAction(e -> criarJogo());
            btnVoltar.setOnAction(e -> voltar());
        } catch (Exception e) {
            mensagemLabel.setText("Erro ao carregar dados.");
        }
    }

    private void filtrarCampeonatos() {
    String equipaCasa = equipaCasaCombo.getValue();
    String equipaVisitante = equipaVisitanteCombo.getValue();

    List<String> nomesFiltrados = new ArrayList<>();
    nomesFiltrados.add("jogo amigável");

    if (equipaCasa != null && equipaVisitante != null) {
        for (CampeonatoDTO camp : todosCampeonatos) {
            List<String> nomesEquipas = camp.getEquipasNomes();
            if (nomesEquipas.contains(equipaCasa) && nomesEquipas.contains(equipaVisitante)) {
                nomesFiltrados.add(camp.getNome());
            }
        }
    } else {
        // Se não há duas equipas selecionadas, mostra só "jogo amigável"
    }
        campeonatoCombo.setItems(FXCollections.observableArrayList(nomesFiltrados));
        campeonatoCombo.getSelectionModel().selectFirst();
    }

    private void criarJogo() {
        try {
            JogoDTO dto = new JogoDTO();
            dto.setData(dataField.getText());
            dto.setHora(horaField.getText());

            // Localização simples (adapta se tiveres DTO próprio)
            LocalizacaoDTO local = new LocalizacaoDTO();
            local.setRua(ruaField.getText());
            local.setCidade(cidadeField.getText());
            dto.setLocal(local);

            List<String> jogadoresCasa = checkListJogadoresCasa.getCheckModel().getCheckedItems();
            List<String> jogadoresVisitante = checkListJogadoresVisitante.getCheckModel().getCheckedItems();

             // Verificação de jogadores repetidos entre as equipas
            for (String jogador : jogadoresCasa) {
                if (jogadoresVisitante.contains(jogador)) {
                    mensagemLabel.setText("Não pode haver jogadores iguais nas duas equipas escaladas para o jogo!");
                    return;
                }
            }


            if (jogadoresCasa.size() != 5 || jogadoresVisitante.size() != 5) {
                mensagemLabel.setText("Seleciona 5 jogadores para cada equipa.");
                return;
            }

            // Equipas
            EquipaJogoDTO equipaCasa = new EquipaJogoDTO();
            equipaCasa.setEquipa(equipaCasaCombo.getValue());
            equipaCasa.setJogadoresParticipantes(jogadoresCasa);
            dto.setEquipaJogoCasa(equipaCasa);

            EquipaJogoDTO equipaVisitante = new EquipaJogoDTO();
            equipaVisitante.setEquipa(equipaVisitanteCombo.getValue());
            equipaVisitante.setJogadoresParticipantes(jogadoresVisitante);
            dto.setEquipaJogoVisitante(equipaVisitante);

            if (arbitroCombo.getValue() == null || arbitroCombo.getValue().isBlank()) {
                mensagemLabel.setText("Selecione o arbitro principal!");
                return;
            }

            // Árbitro principal
            dto.setArbitroPrincipal(arbitroCombo.getValue());
            // Árbitros adicionais
            dto.setArbitros(checkListArbitros.getCheckModel().getCheckedItems());

            
            String campeonatoSelecionado = campeonatoCombo.getValue();
            

            if (campeonatoSelecionado == null || campeonatoSelecionado.equals("jogo amigável")) {
                dto.setCampeonato(null);
            } else { 
                dto.setCampeonato(campeonatoSelecionado);
            }
            

            

            String erro = jogoController.criarJogo(dto);
            if (erro == null) {
                mensagemLabel.setText("Jogo criado com sucesso!");
                AppFX.setRoot("gestaoJogos");
            } else {
                mensagemLabel.setText("Erro: " + erro);
            }
        } catch (Exception e) {
            mensagemLabel.setText("Erro ao criar jogo: " + e.getMessage());
        }
    }

    private void carregarJogadoresEquipa(String equipaNome, CheckListView<String> checkListView) {
        try {
            if (equipaNome == null) {
                checkListView.getItems().setAll();
                return;
            }
            EquipaDTO equipa = equipaController.listarEquipas().stream()
                    .filter(eq -> eq.getNome().equals(equipaNome))
                    .findFirst().orElse(null);
            if (equipa != null && equipa.getJogadores() != null) {
                checkListView.getItems().setAll(equipa.getJogadores());
            } else {
                checkListView.getItems().setAll();
            }
        } catch (Exception e) {
            mensagemLabel.setText("Erro ao carregar jogadores da equipa.");
        }
    }

    private void voltar() {
        try {
            AppFX.setRoot("gestaoJogos");
        } catch (Exception e) {
            mensagemLabel.setText("Erro ao voltar.");
        }
    }
}