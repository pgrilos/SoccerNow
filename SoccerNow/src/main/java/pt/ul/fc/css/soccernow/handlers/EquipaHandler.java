package pt.ul.fc.css.soccernow.handlers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pt.ul.fc.css.soccernow.Mapper;
import pt.ul.fc.css.soccernow.dominio.Equipa;
import pt.ul.fc.css.soccernow.dominio.Jogador;
import pt.ul.fc.css.soccernow.dto.EquipaDTO;
import pt.ul.fc.css.soccernow.repositorio.EquipaRepository;
import pt.ul.fc.css.soccernow.repositorio.JogadorRepository;
import pt.ul.fc.css.soccernow.repositorio.JogoRepository;

@Service
@Transactional
public class EquipaHandler {

    private final EquipaRepository equipaRepository;
    private final JogadorRepository jogadorRepository;

    public EquipaHandler(EquipaRepository equipaRepository, 
                        JogadorRepository jogadorRepository,
                        JogoRepository jogoRepository) {
        this.equipaRepository = equipaRepository;
        this.jogadorRepository = jogadorRepository;
    }

    public EquipaDTO create(EquipaDTO equipaDTO) {
        validarEquipa(equipaDTO, -1L);
        
        Equipa equipa = new Equipa(equipaDTO.getNome());
        // Para criação, não precisamos buscar relações existentes
        Equipa savedEquipa = equipaRepository.save(equipa);
        
        // Após salvar a equipa, atualizamos as relações
        updateEquipaFromDTO(savedEquipa, equipaDTO, true);
        
        // Salva novamente para persistir as relações
        savedEquipa = equipaRepository.save(savedEquipa);
        equipaDTO.setId(savedEquipa.getId());

        if(equipaDTO.getJogadores() != null) {
            List<Jogador> jogadores = equipaDTO.getJogadores().stream()
                .map(username -> jogadorRepository.findByUsername(username))
                .collect(Collectors.toList());
            savedEquipa.setJogadores(jogadores);
            savedEquipa = equipaRepository.save(savedEquipa);
        }

        return Mapper.equipaMapToDto(savedEquipa);
    }

    public EquipaDTO findById(Long id) {
        Equipa equipa = equipaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Equipa não encontrada"));
        return Mapper.equipaMapToDto(equipa);
    }

    public List<EquipaDTO> findAll() {
        List<Equipa> equipas = equipaRepository.findAll();
        return equipas.stream()
                .map(Mapper::equipaMapToDto)
                .toList();
    }

    public EquipaDTO update(Long id, EquipaDTO equipaDTO) {
        validarEquipa(equipaDTO, id);
        
        Optional<Equipa> equipa = equipaRepository.findById(id);
        if (equipa.isEmpty()) {
            throw new IllegalArgumentException("Equipa não encontrada");
        }
        
        Equipa equipaExistente = equipa.get();
        updateEquipaFromDTO(equipaExistente, equipaDTO,false); 

        
        Equipa updatedEquipa = equipaRepository.save(equipaExistente);
        equipaDTO.setId(updatedEquipa.getId());

        return equipaDTO;
    }

    public EquipaDTO delete(Long id) {
        Optional<Equipa> equipa = equipaRepository.findById(id);
        
        if (equipa.isEmpty()) {
            throw new IllegalArgumentException("Equipa não encontrada");
        }

        Equipa equipaExistente = equipa.get();
        // Verifica se há jogos futuros
        boolean temJogosFuturos = equipaExistente.getEquipasJogos().stream()
        .map(equipaJogo -> equipaJogo.getJogo())
        .anyMatch(jogo -> 
            jogo.getData().isAfter(LocalDate.now()) ||
            (jogo.getData().isEqual(LocalDate.now()) && 
            LocalTime.parse(jogo.getHora()).isAfter(LocalDateTime.now().toLocalTime()))
        );
        if (temJogosFuturos) {
            throw new IllegalStateException("Não é possível remover uma equipa com jogos futuros marcados");
        }
        
        equipaRepository.deleteById(id);
        return Mapper.equipaMapToDto(equipaExistente);
    }

    public EquipaDTO findByNome(String nome) {
        Equipa equipa = equipaRepository.findByNomeIgnoreCase(nome);
        if (equipa == null) {
            throw new IllegalArgumentException("Equipa não encontrada");
        }
        return Mapper.equipaMapToDto(equipa);
    }

    public List<EquipaDTO> filtrarEquipas(
        String nome,
        Integer minJogadores,
        Integer minVitorias,
        Integer minEmpates,
        Integer minDerrotas,
        Integer minConquistas,
        String ausenciaPosicao
    ) {
        List<Equipa> equipas = equipaRepository.findAll();

        return equipas.stream()
            .filter(e -> nome == null || nome.isBlank() || e.getNome().toLowerCase().contains(nome.toLowerCase()))
            .filter(e -> minJogadores == null || (e.getJogadores() != null && e.getJogadores().size() >= minJogadores))
            .filter(e -> minVitorias == null || e.getVitorias() >= minVitorias)
            .filter(e -> minEmpates == null || e.getEmpates() >= minEmpates)
            .filter(e -> minDerrotas == null || e.getDerrotas() >= minDerrotas)
            .filter(e -> minConquistas == null || (e.getConquistas() != null && e.getConquistas().size() >= minConquistas))
            .filter(e -> ausenciaPosicao == null || ausenciaPosicao.isBlank() ||
                (e.getJogadores() != null && e.getJogadores().stream()
                    .map(j -> j.getPosicao().name())
                    .noneMatch(pos -> pos.equalsIgnoreCase(ausenciaPosicao))))
            .map(Mapper::equipaMapToDto)
            .toList();
    }


    private void validarEquipa(EquipaDTO equipaDTO, Long idAtual) {
        if (equipaDTO.getNome() == null || equipaDTO.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da equipa é obrigatório");
        }

        Equipa equipaExistente = equipaRepository.findByNomeIgnoreCase(equipaDTO.getNome());
        if (equipaExistente != null && !equipaExistente.getId().equals(idAtual)) {
            throw new IllegalArgumentException("Já existe uma equipa com este nome");
        }

        if (equipaDTO.getJogadores() != null) {
            for (String username : equipaDTO.getJogadores()) {
                if (username == null || username.trim().isEmpty()) {
                    throw new IllegalArgumentException("Username do jogador é obrigatório");
                }
                if (jogadorRepository.findByUsername(username) == null) {
                    throw new IllegalArgumentException("Jogador não encontrado: " + username);
                }
            }
        }
    }

    private void updateEquipaFromDTO(Equipa equipa, EquipaDTO dto, boolean isNew) {
        equipa.setNome(dto.getNome());
        
        // Lista de jogadores
        if (dto.getJogadores() != null && !isNew) {
            List<Jogador> jogadores = dto.getJogadores().stream()
                .map(username -> jogadorRepository.findByUsername(username))
                .collect(Collectors.toList());
            equipa.setJogadores(jogadores);
        }
    }



}