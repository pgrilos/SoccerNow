package pt.ul.fc.css.soccernow.handlers;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pt.ul.fc.css.soccernow.Mapper;
import pt.ul.fc.css.soccernow.dominio.Arbitro;
import pt.ul.fc.css.soccernow.dominio.Campeonato;
import pt.ul.fc.css.soccernow.dominio.CampeonatoPontos;
import pt.ul.fc.css.soccernow.dominio.Cartao;
import pt.ul.fc.css.soccernow.dominio.Equipa;
import pt.ul.fc.css.soccernow.dominio.EquipaJogo;
import pt.ul.fc.css.soccernow.dominio.EstatisticasJogo;
import pt.ul.fc.css.soccernow.dominio.Jogador;
import pt.ul.fc.css.soccernow.dominio.Jogo;
import pt.ul.fc.css.soccernow.dominio.JogoAmigavel;
import pt.ul.fc.css.soccernow.dominio.JogoCampeonato;
import pt.ul.fc.css.soccernow.dto.CartaoDTO;
import pt.ul.fc.css.soccernow.dto.EquipaJogoDTO;
import pt.ul.fc.css.soccernow.dto.EstatisticasDTO;
import pt.ul.fc.css.soccernow.dto.JogoDTO;
import pt.ul.fc.css.soccernow.repositorio.ArbitroRepository;
import pt.ul.fc.css.soccernow.repositorio.CampeonatoRepository;
import pt.ul.fc.css.soccernow.repositorio.CartaoRepository;
import pt.ul.fc.css.soccernow.repositorio.EquipaJogoRepository;
import pt.ul.fc.css.soccernow.repositorio.EquipaRepository;
import pt.ul.fc.css.soccernow.repositorio.EstatisticasJogoRepository;
import pt.ul.fc.css.soccernow.repositorio.JogadorRepository;
import pt.ul.fc.css.soccernow.repositorio.JogoRepository;

@Service
@Transactional
public class JogoHandler {

    private final JogoRepository jogoRepository;
    private final ArbitroRepository arbitroRepository;
    private final EquipaRepository equipaRepository;
    private final JogadorRepository jogadorRepository;
    private final EstatisticasJogoRepository estatisticasJogoRepository;
    private final CartaoRepository cartaoRepository;
    private final CampeonatoRepository campeonatoRepository;

    public JogoHandler(JogoRepository jogoRepository, ArbitroRepository arbitroRepository,
            EquipaRepository equipaRepository, JogadorRepository jogadorRepository,
            EstatisticasJogoRepository estatisticasJogoRepository, CartaoRepository cartaoRepository,
            EquipaJogoRepository equipaJogoRepository, CampeonatoRepository campeonatoRepository) {
        this.jogoRepository = jogoRepository;
        this.arbitroRepository = arbitroRepository;
        this.equipaRepository = equipaRepository;
        this.jogadorRepository = jogadorRepository;
        this.estatisticasJogoRepository = estatisticasJogoRepository;
        this.cartaoRepository = cartaoRepository;
        this.campeonatoRepository = campeonatoRepository;
    }

    public JogoDTO create(JogoDTO jogoDTO) {
        validarJogo(jogoDTO);

        if (jogoDTO.getCampeonato() == null) {
            // Jogo amigável
            Jogo jogo = new JogoAmigavel();
            updateJogoFromDto(jogo, jogoDTO);

            EquipaJogo equipaJogoCasa = equipaJogoDtoToEntity(jogoDTO.getEquipaJogoCasa());
            equipaJogoCasa.setJogo(jogo);
            EquipaJogo equipaJogoVisitante = equipaJogoDtoToEntity(jogoDTO.getEquipaJogoVisitante());
            equipaJogoVisitante.setJogo(jogo);

            jogo.setEquipasJogo(List.of(equipaJogoCasa, equipaJogoVisitante));

            Jogo savedJogo = jogoRepository.save(jogo);
            jogoDTO.setId(savedJogo.getId());
            jogoDTO.getEquipaJogoCasa().setId(savedJogo.getEquipaJogoCasa().getId());
            jogoDTO.getEquipaJogoVisitante().setId(savedJogo.getEquipaJogoVisitante().getId());
            return jogoDTO;
        } else {
            // Jogo de campeonato
            Optional<Campeonato> campeonatoOpt = campeonatoRepository.findByNome(jogoDTO.getCampeonato());
            if (campeonatoOpt.isEmpty()) {
                throw new IllegalArgumentException("Campeonato não encontrado");
            }
            Campeonato campeonato = campeonatoOpt.get();

            // verificar se pelo menos um árbitro é certificado
            boolean arbitroCertificado = false;
            for (String arbitroUsername : jogoDTO.getArbitros()) {
                Arbitro arbitro = arbitroRepository.findByUsername(arbitroUsername);
                if (arbitro == null) {
                    throw new IllegalArgumentException("Árbitro não encontrado: " + arbitroUsername);
                }
                if (arbitro.getIsCertificated()) {
                    arbitroCertificado = true;
                }
            }
            if (!arbitroCertificado) {
                throw new IllegalArgumentException("Pelo menos um árbitro tem de ser certificado");
            }

            JogoCampeonato jogoCampeonato = new JogoCampeonato();
            updateJogoCampeonatoFromDto(jogoCampeonato, jogoDTO, campeonato);

            // Criar as EquipaJogo e associar o Jogo
            EquipaJogo equipaJogoCasa = equipaJogoDtoToEntity(jogoDTO.getEquipaJogoCasa());
            equipaJogoCasa.setJogo(jogoCampeonato);
            EquipaJogo equipaJogoVisitante = equipaJogoDtoToEntity(jogoDTO.getEquipaJogoVisitante());
            equipaJogoVisitante.setJogo(jogoCampeonato);

            jogoCampeonato.setEquipasJogo(List.of(equipaJogoCasa, equipaJogoVisitante));

            // Adicionar o jogo ao campeonato antes de salvar
            campeonato.addJogo(jogoCampeonato);
            Campeonato savedCampeonato = campeonatoRepository.saveAndFlush(campeonato);

            // Recupera o jogo salvo (último da lista)
            JogoCampeonato savedJogo = savedCampeonato.getJogos().get(savedCampeonato.getJogos().size() - 1);
            EquipaJogo savedCasa = savedJogo.getEquipaJogoCasa();
            EquipaJogo savedVisitante = savedJogo.getEquipaJogoVisitante();

            jogoDTO.setId(savedJogo.getId());
            jogoDTO.getEquipaJogoCasa().setId(savedCasa.getId());
            jogoDTO.getEquipaJogoVisitante().setId(savedVisitante.getId());
            return jogoDTO;
        }
    }

    public EstatisticasDTO updateResultado(Long id, EstatisticasDTO estatisticasDTO) {

        Optional<Jogo> optionalJogo = jogoRepository.findById(id);
        if (optionalJogo.isEmpty()) {
            throw new IllegalArgumentException("Jogo não encontrado");
        }

        Jogo existingJogo = optionalJogo.get();

        if (existingJogo.getData().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Só é possível registar resultados de jogos já realizados (data igual ou anterior a hoje)");
        }

        if (existingJogo.getEstatisticas() != null) {
            throw new IllegalArgumentException("O resultado deste jogo já foi registado");
        }

        // Validar as estatísticas com base no jogo
        validarEstatisticas(estatisticasDTO, existingJogo);

        // Validar cartões
        for (CartaoDTO cartaoDTO : estatisticasDTO.getCartoes()) {
            validarCartao(cartaoDTO);
        }

        for (CartaoDTO cartaoDTO : estatisticasDTO.getCartoes()) {
            validarCartao(cartaoDTO);
        }

        // Atualizar golos marcados dos jogadores da equipa da casa
        if (estatisticasDTO.getMarcadoresEquipaCasa() != null) {
            for (String username : estatisticasDTO.getMarcadoresEquipaCasa()) {
                Jogador jogador = jogadorRepository.findByUsername(username);
                if (jogador != null) {
                    jogador.adicionarGolo();
                    jogadorRepository.save(jogador);
                }
            }
        }

        // Atualizar golos marcados dos jogadores da equipa visitante
        if (estatisticasDTO.getMarcadoresEquipaVisitante() != null) {
            for (String username : estatisticasDTO.getMarcadoresEquipaVisitante()) {
                Jogador jogador = jogadorRepository.findByUsername(username);
                if (jogador != null) {
                    jogador.adicionarGolo();
                    jogadorRepository.save(jogador);
                }
            }
        }

        // Adicionar EquipaJogo ao objeto jogador
        for (Jogador jogador : existingJogo.getEquipaJogoCasa().getJogadoresParticipantes()) {
            if (!jogador.getJogos().contains(existingJogo.getEquipaJogoCasa())) {
                jogador.getJogos().add(existingJogo.getEquipaJogoCasa());
                jogadorRepository.save(jogador);
            }
        }

        for (Jogador jogador : existingJogo.getEquipaJogoVisitante().getJogadoresParticipantes()) {
            if (!jogador.getJogos().contains(existingJogo.getEquipaJogoVisitante())) {
                jogador.getJogos().add(existingJogo.getEquipaJogoVisitante());
                jogadorRepository.save(jogador);
            }
        }

        // Converter DTO para entidade EstatisticasJogo
        EstatisticasJogo estatisticas = estatisticasDTOToEntity(estatisticasDTO);

        // Salvar as estatísticas no repositório
        EstatisticasJogo savedEstatisticas = estatisticasJogoRepository.save(estatisticas);

        // Converter DTO para entidade Cartoes(associando ao EstatisticasJogo)
        List<Cartao> cartoes = estatisticasDTO.getCartoes().stream()
                .map(cartaoDTO -> this.cartaoDTOToEntity(cartaoDTO, savedEstatisticas))
                .collect(Collectors.toList());

        // Salvar os cartões no repositório(atribui id ao cartão)
        cartaoRepository.saveAll(cartoes);

        // Associar os cartões ao EstatisticasJogo (só apos atribuição do id ao cartao)
        savedEstatisticas.setCartoes(cartoes);

        // Associar as estatísticas ao jogo
        existingJogo.setEstatisticas(savedEstatisticas);

        // Salvar o jogo atualizado no repositório
        jogoRepository.save(existingJogo);

        if (existingJogo instanceof JogoCampeonato jogoCampeonato) {
            Campeonato campeonato = jogoCampeonato.getCampeonato();
            if (campeonato instanceof CampeonatoPontos campeonatoPontos) {
                // Obter equipas
                Equipa equipaCasa = jogoCampeonato.getEquipaJogoCasa().getEquipa();
                Equipa equipaVisitante = jogoCampeonato.getEquipaJogoVisitante().getEquipa();

                // Obter golos
                int golosCasa = savedEstatisticas.getMarcadoresEquipaCasa() != null
                        ? savedEstatisticas.getMarcadoresEquipaCasa().size()
                        : 0;
                int golosVisitante = savedEstatisticas.getMarcadoresEquipaVisitante() != null
                        ? savedEstatisticas.getMarcadoresEquipaVisitante().size()
                        : 0;

                // Atualizar pontos
                Map<Equipa, Integer> classificacao = campeonatoPontos.getClassificacao();
                classificacao.putIfAbsent(equipaCasa, 0);
                classificacao.putIfAbsent(equipaVisitante, 0);

                if (golosCasa > golosVisitante) {
                    classificacao.put(equipaCasa, classificacao.get(equipaCasa) + 3);
                } else if (golosCasa < golosVisitante) {
                    classificacao.put(equipaVisitante, classificacao.get(equipaVisitante) + 3);
                } else {
                    classificacao.put(equipaCasa, classificacao.get(equipaCasa) + 1);
                    classificacao.put(equipaVisitante, classificacao.get(equipaVisitante) + 1);
                }

                campeonatoPontos.setClassificacao(classificacao);
                campeonatoRepository.save(campeonatoPontos);

            }
        }

        // Retornar o DTO atualizado
        estatisticasDTO.setId(savedEstatisticas.getId());

        List<CartaoDTO> cartoesDTO = cartoes.stream()
                .map(cartaoDTO -> (Mapper.cartaoMapToDto(cartaoDTO)))
                .collect(Collectors.toList());

        estatisticasDTO.setCartoes(cartoesDTO);

        return estatisticasDTO;
    }

    public JogoDTO delete(Long id) {
        Optional<Jogo> jogo = jogoRepository.findByIdAndAtivoTrue(id);
        if (jogo.isEmpty()) {
            throw new IllegalArgumentException("Jogo não encontrado");
        }
        Jogo existingJogo = jogo.get();
        existingJogo.setAtivo(false); // Soft delete
        jogoRepository.save(existingJogo);

        return Mapper.jogoMapToDto(existingJogo); // Retorna o JogoDTO do jogo excluído
    }

    public List<JogoDTO> findAll() {
        List<Jogo> jogos = jogoRepository.findByAtivoTrue()
                .stream()
                .collect(Collectors.toList());
        return jogos.stream()
                .map(Mapper::jogoMapToDto)
                .collect(Collectors.toList());
    }

    public JogoDTO cancelarJogoCampeonato(Long id) {
        Optional<Jogo> jogoOpt = jogoRepository.findByIdAndAtivoTrue(id);
        if (jogoOpt.isEmpty()) {
            throw new IllegalArgumentException("Jogo não encontrado");
        }
        Jogo jogo = jogoOpt.get();
        if (!(jogo instanceof JogoCampeonato jogoCampeonato)) {
            throw new IllegalArgumentException("Só é possível cancelar jogos de campeonato");
        }
        jogo.setAtivo(false);
        jogoRepository.save(jogo);

        // Remover da lista de jogos do campeonato
        Campeonato campeonato = jogoCampeonato.getCampeonato();
        if (campeonato != null) {
            campeonato.removeJogo(jogoCampeonato);
            campeonatoRepository.saveAndFlush(campeonato);
        }

        return Mapper.jogoMapToDto(jogo);
    }

    public List<JogoDTO> filtrarJogos(Boolean realizados, Integer minGolos, String rua, String cidade, String turno) {
        List<Jogo> jogos = jogoRepository.findAll();

        return jogos.stream()
                .filter(j -> realizados == null ||
                        (realizados && j.getEstatisticas() != null) ||
                        (!realizados && j.getEstatisticas() == null))
                .filter(j -> minGolos == null ||
                        (j.getEstatisticas() != null &&
                                (j.getEstatisticas().getMarcadoresEquipaCasa().size() +
                                        j.getEstatisticas().getMarcadoresEquipaVisitante().size()) >= minGolos))
                .filter(j -> (rua == null || rua.isBlank() ||
                        (j.getLocal() != null && j.getLocal().getRua() != null &&
                                j.getLocal().getRua().toLowerCase().contains(rua.toLowerCase())))
                        &&
                        (cidade == null || cidade.isBlank() ||
                                (j.getLocal() != null && j.getLocal().getCidade() != null &&
                                        j.getLocal().getCidade().toLowerCase().contains(cidade.toLowerCase()))))
                .filter(j -> turno == null || turno.isBlank() || turnoDoJogo(j).equalsIgnoreCase(turno))
                .map(Mapper::jogoMapToDto)
                .toList();
    }

    // função auxiliar para turno
    private String turnoDoJogo(Jogo jogo) {
        if (jogo.getHora() == null)
            return "";
        String[] partes = jogo.getHora().split(":");
        int hora = Integer.parseInt(partes[0]);
        if (hora >= 6 && hora < 12)
            return "manha";
        if (hora >= 12 && hora < 19)
            return "tarde";
        return "noite";
    }

    // Validações
    private void validarJogo(JogoDTO jogo) {

        // Verificar Data
        if (jogo.getData() == null) {
            throw new IllegalArgumentException("A data do jogo é obrigatória");
        }
        try {
            LocalDate.parse(jogo.getData()); // formato padrão YYYY-MM-DD
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("A data do jogo deve estar no formato YYYY-MM-DD");
        }
        // Verificar Hora
        if (jogo.getHora().isEmpty()) {
            throw new IllegalArgumentException("Hora do jogo é obrigatória");
        }
        if (!jogo.getHora().matches("^([01]\\d|2[0-3]):[0-5]\\d$")) {
            throw new IllegalArgumentException("A hora do jogo deve estar no formato HH:mm(24 horas)");
        }
        // Verificar Local
        if (jogo.getLocal() == null ||
                jogo.getLocal().getRua() == null || jogo.getLocal().getRua().isEmpty() ||
                jogo.getLocal().getCidade() == null || jogo.getLocal().getCidade().isEmpty()) {
            throw new IllegalArgumentException("Local do jogo é obrigatório");
        }

        // Verificar se já existe um jogo agendado para o mesmo local, data e hora
        LocalDate dataJogo = LocalDate.parse(jogo.getData());

        List<Jogo> jogosNoMesmoHorario = jogoRepository.findByAtivoTrue().stream()
                .filter(j -> j.getData().equals(dataJogo) &&
                        j.getHora().equals(jogo.getHora()) &&
                        j.getLocal().getRua().equalsIgnoreCase(jogo.getLocal().getRua()) &&
                        j.getLocal().getCidade().equalsIgnoreCase(jogo.getLocal().getCidade()))
                .collect(Collectors.toList());
        if (!jogosNoMesmoHorario.isEmpty()) {
            throw new IllegalArgumentException("Já existe um jogo agendado para este local, data e hora");
        }

        // Vericar Equipas
        if (jogo.getEquipaJogoCasa() == null || jogo.getEquipaJogoVisitante() == null) {
            throw new IllegalArgumentException("As equipas são obrigatórias");
        }
        if (jogo.getEquipaJogoCasa().getEquipa().equals(jogo.getEquipaJogoVisitante().getEquipa())) {
            throw new IllegalArgumentException("As equipas não podem ser iguais");
        }
        if (jogo.getEquipaJogoCasa().getJogadoresParticipantes().size() < 4
                || jogo.getEquipaJogoVisitante().getJogadoresParticipantes().size() < 4) {
            throw new IllegalArgumentException("As equipas tem de ter pelo menos 5 jogadores");
        }
        Equipa equipaCasa = equipaRepository.findByNomeIgnoreCase(jogo.getEquipaJogoCasa().getEquipa());
        Equipa equipaVisitante = equipaRepository.findByNomeIgnoreCase(jogo.getEquipaJogoVisitante().getEquipa());
        if (equipaCasa == null || equipaVisitante == null) {
            throw new IllegalArgumentException("Equipa não encontrada");
        }
        // Verificar se cada jogador realmente pertence à equipa da casa
        for (String username : jogo.getEquipaJogoCasa().getJogadoresParticipantes()) {
            Jogador jogador = jogadorRepository.findByUsername(username);
            if (!equipaCasa.getJogadores().contains(jogador)) {
                throw new IllegalArgumentException(
                        "O jogador " + username + " não pertence à equipa da casa " + equipaCasa.getNome());
            }
        }
        // Verificar se cada jogador realmente pertence à equipa visitante
        for (String username : jogo.getEquipaJogoVisitante().getJogadoresParticipantes()) {
            Jogador jogador = jogadorRepository.findByUsername(username);
            if (!equipaVisitante.getJogadores().contains(jogador)) {
                throw new IllegalArgumentException(
                        "O jogador " + username + " não pertence à equipa visitante " + equipaVisitante.getNome());
            }
        }

        // Verifica se há pelo menos um GUARDA_REDES em cada equipa
        boolean casaTemGR = jogo.getEquipaJogoCasa().getJogadoresParticipantes().stream()
                .map(username -> jogadorRepository.findByUsername(username))
                .anyMatch(j -> j != null && j.getPosicao().name().equals("GUARDA_REDES"));

        boolean visitanteTemGR = jogo.getEquipaJogoVisitante().getJogadoresParticipantes().stream()
                .map(username -> jogadorRepository.findByUsername(username))
                .anyMatch(j -> j != null && j.getPosicao().name().equals("GUARDA_REDES"));

        if (!casaTemGR || !visitanteTemGR) {
            throw new IllegalArgumentException("Cada equipa deve ter pelo menos um GUARDA_REDES");
        }

        // Arbitros
        if (jogo.getArbitroPrincipal() == null || jogo.getArbitros() == null || jogo.getArbitros().isEmpty()) {
            throw new IllegalArgumentException("Árbitro principal e lista de árbitros são obrigatórios");
        }
        Arbitro arbitroPrincipal = arbitroRepository.findByUsername(jogo.getArbitroPrincipal());
        if (arbitroPrincipal == null) {
            throw new IllegalArgumentException("Árbitro principal não encontrado");
        }
        List<Arbitro> arbitros = jogo.getArbitros().stream()
                .map(username -> arbitroRepository.findByUsername(username)).collect(Collectors.toList());
        if (arbitros.contains(null)) {
            throw new IllegalArgumentException("Árbitros não encontrados");
        }
        if (jogo.getArbitros() == null || jogo.getArbitros().isEmpty()) {
            throw new IllegalArgumentException("Tem que haver pelo menos um árbitro");
        }
        if (jogo.getArbitroPrincipal() == null) {
            throw new IllegalArgumentException("O árbitro principal é obrigatório");
        }
        if (jogo.getArbitros().contains(jogo.getArbitroPrincipal()) == false) {
            throw new IllegalArgumentException("O árbitro principal tem de ser um dos árbitros da partida");
        }

    }

    private void validarEstatisticas(EstatisticasDTO estatisticasDTO, Jogo jogo) {
        if (estatisticasDTO == null) {
            throw new IllegalArgumentException("As estatísticas do jogo são obrigatórias");
        }

        // Obter as equipas do jogo
        EquipaJogo equipaJogoCasa = jogo.getEquipaJogoCasa();
        EquipaJogo equipaJogoVisitante = jogo.getEquipaJogoVisitante();

        // Validar marcadores da equipa da casa
        for (String marcador : estatisticasDTO.getMarcadoresEquipaCasa()) {
            Jogador jogador = jogadorRepository.findByUsername(marcador);
            if (jogador == null || !equipaJogoCasa.getJogadoresParticipantes().contains(jogador)) {
                throw new IllegalArgumentException(
                        "O marcador " + marcador + " não pertence à equipa da casa escalada para este jogo: "
                                + equipaJogoCasa.getEquipa().getNome());
            }
        }

        // Validar marcadores da equipa visitante
        for (String marcador : estatisticasDTO.getMarcadoresEquipaVisitante()) {
            Jogador jogador = jogadorRepository.findByUsername(marcador);
            if (jogador == null || !equipaJogoVisitante.getJogadoresParticipantes().contains(jogador)) {
                throw new IllegalArgumentException("O marcador " + marcador + " não pertence à equipa visitante: "
                        + equipaJogoVisitante.getEquipa().getNome());
            }
        }

        // Validar cartões
        for (CartaoDTO cartaoDTO : estatisticasDTO.getCartoes()) {
            Jogador jogador = jogadorRepository.findByUsername(cartaoDTO.getJogador());
            if (jogador == null) {
                throw new IllegalArgumentException("Jogador do cartão não encontrado: " + cartaoDTO.getJogador());
            }

            // Verificar se o jogador pertence a uma das equipas do jogo
            if (!equipaJogoCasa.getJogadoresParticipantes().contains(jogador)
                    && !equipaJogoVisitante.getJogadoresParticipantes().contains(jogador)) {
                throw new IllegalArgumentException("O jogador " + jogador.getUsername()
                        + " do cartão não pertence a nenhuma das equipas escaldas para o jogo");
            }
        }

    }

    private void validarCartao(CartaoDTO cartaoDTO) {
        // Obter o jogador associado ao cartão
        Jogador jogador = jogadorRepository.findByUsername(cartaoDTO.getJogador());
        if (jogador == null) {
            throw new IllegalArgumentException("Jogador não encontrado: " + cartaoDTO.getJogador());
        }

        // Obter a equipa associada ao cartão
        Equipa equipa = equipaRepository.findByNomeIgnoreCase(cartaoDTO.getEquipa());
        if (equipa == null) {
            throw new IllegalArgumentException("Equipa não encontrada: " + cartaoDTO.getEquipa());
        }

        // Validar se o jogador pertence à equipa correta
        if (!equipa.getJogadores().contains(jogador)) {
            throw new IllegalArgumentException(
                    "O jogador " + jogador.getUsername() + " não pertence à equipa " + equipa.getNome());
        }

        // validar arbitro
        arbitroRepository.findById(cartaoDTO.getArbitroId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Árbitro não encontrado com ID: " + cartaoDTO.getArbitroId()));

    }

    // ToEntity
    private EstatisticasJogo estatisticasDTOToEntity(EstatisticasDTO estatisticasDTO) {
        EstatisticasJogo estatisticas = new EstatisticasJogo();
        estatisticas.setMarcadoresEquipaCasa(estatisticasDTO.getMarcadoresEquipaCasa().stream()
                .map(jogador -> jogadorRepository.findByUsername(jogador)).collect(Collectors.toList()));
        estatisticas.setMarcadoresEquipaVisitante(estatisticasDTO.getMarcadoresEquipaVisitante().stream()
                .map(jogador -> jogadorRepository.findByUsername(jogador)).collect(Collectors.toList()));

        return estatisticas;
    }

    private Cartao cartaoDTOToEntity(CartaoDTO cartaoDTO, EstatisticasJogo estatisticas) {
        Cartao cartao = new Cartao();

        // Associar o cartão às estatísticas e configurar os outros atributos
        cartao.setCorCartao(Cartao.CorCartao.valueOf(cartaoDTO.getCorCartao().toUpperCase()));
        cartao.setJogador(jogadorRepository.findByUsername(cartaoDTO.getJogador()));
        cartao.setEquipa(equipaRepository.findByNomeIgnoreCase(cartaoDTO.getEquipa()));
        cartao.setEstatisticas(estatisticas); // Associar a estatística salva
        cartao.setArbitro(arbitroRepository.findById(cartaoDTO.getArbitroId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Árbitro não encontrado com ID: " + cartaoDTO.getArbitroId())));

        return cartao;
    }

    public EquipaJogo equipaJogoDtoToEntity(EquipaJogoDTO equipaJogoDTO) {
        EquipaJogo equipaJogo = new EquipaJogo();
        equipaJogo.setEquipa(equipaRepository.findByNomeIgnoreCase(equipaJogoDTO.getEquipa()));
        equipaJogo.setJogadoresParticipantes(
                equipaJogoDTO.getJogadoresParticipantes().stream()
                        .map(username -> {
                            Jogador jogador = jogadorRepository.findByUsername(username);
                            if (jogador == null) {
                                throw new IllegalArgumentException("Jogador não encontrado: " + username);
                            }
                            return jogador;
                        })
                        .collect(Collectors.toList()));

        return equipaJogo;
    }

    private void updateJogoFromDto(Jogo jogo, JogoDTO dto) {
        jogo.setData(LocalDate.parse(dto.getData()));
        jogo.setHora(dto.getHora());
        jogo.setLocal(Mapper.localizacaoDtoToEntity(dto.getLocal()));
        jogo.setArbitroPrincipal(arbitroRepository.findByUsername(dto.getArbitroPrincipal()));
        jogo.setArbitros(dto.getArbitros().stream()
                .map(username -> arbitroRepository.findByUsername(username))
                .collect(Collectors.toList()));

    }

    private void updateJogoCampeonatoFromDto(Jogo jogo, JogoDTO dto, Campeonato campeonato) {
        updateJogoFromDto(jogo, dto);
        if (jogo instanceof JogoCampeonato jogoCampeonato) {
            jogoCampeonato.setCampeonato(campeonato);
        } else {
            throw new IllegalArgumentException("O jogo não é do tipo JogoCampeonato");
        }
    }

}
