package pt.ul.fc.css.soccernow.handlers;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.ul.fc.css.soccernow.dominio.Jogador;
import pt.ul.fc.css.soccernow.dominio.Posicao;
import pt.ul.fc.css.soccernow.dto.JogadorDTO;
import pt.ul.fc.css.soccernow.repositorio.JogadorRepository;
import pt.ul.fc.css.soccernow.Mapper;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class JogadorHandler {

    private final JogadorRepository jogadorRepository;


    public JogadorHandler(JogadorRepository jogadorRepository) {
        this.jogadorRepository = jogadorRepository;
    }

    public JogadorDTO create(JogadorDTO jogadorDTO) {
        validarJogador(jogadorDTO, -1L);
        
        Jogador jogador = new Jogador();
        jogador.setNome(jogadorDTO.getNome().trim());
        jogador.setUsername(jogadorDTO.getUsername().trim());
        jogador.setPassword(jogadorDTO.getPassword().trim());
        jogador.setPosicao(Posicao.valueOf(jogadorDTO.getPosicao().trim()));

        Jogador savedJogador = jogadorRepository.save(jogador);
        jogadorDTO.setId(savedJogador.getId());
        
        return jogadorDTO;
    }

    public JogadorDTO update(Long id, JogadorDTO jogadorDTO) {
        validarJogador(jogadorDTO, id);

        Optional<Jogador> optionalJogador = jogadorRepository.findById(id);
        if (optionalJogador.isEmpty()) {
            throw new IllegalArgumentException("Jogador não encontrado");
        }

        Jogador existingJogador = optionalJogador.get();
        existingJogador.setNome(jogadorDTO.getNome().trim());
        existingJogador.setUsername(jogadorDTO.getUsername().trim());
        existingJogador.setPassword(jogadorDTO.getPassword().trim());
        existingJogador.setPosicao(Posicao.valueOf(jogadorDTO.getPosicao().trim()));

        Jogador savedJogador = jogadorRepository.save(existingJogador);
        jogadorDTO.setId(savedJogador.getId());
        
        return jogadorDTO;
    }

    public JogadorDTO delete(Long id) {
        Optional<Jogador> optionalJogador = jogadorRepository.findById(id);
        if (optionalJogador.isEmpty()) {
            throw new IllegalArgumentException("Jogador não encontrado");
        }

        Jogador jogador = optionalJogador.get();

        jogadorRepository.deleteById(id);
        return Mapper.jogadorMapToDto(jogador);
    }

    public JogadorDTO findById(Long id) {
        Optional<Jogador> optionalJogador = jogadorRepository.findById(id);
        return optionalJogador.map(Mapper::jogadorMapToDto).orElse(null);
    }

    public List<JogadorDTO> findAll() {
        List<Jogador> jogadores = jogadorRepository.findAll();
        return jogadores.stream()
        .map(Mapper::jogadorMapToDto)
        .toList();
    }

    public List<JogadorDTO> filtrarJogadores(String nome, String posicao, Integer minGolos, Integer minCartoes, Integer minJogos) {
         Posicao posicaoEnum = null;
        if (posicao != null && !posicao.isBlank()) {
            try {
                posicaoEnum = Posicao.valueOf(posicao.trim().toUpperCase());
            } catch (Exception e) {
                // Se a posição não existir, retorna vazio
                return List.of();
            }
        }
        List<Jogador> jogadores = jogadorRepository.filtrarJogadores(
            (nome == null || nome.isBlank()) ? null : nome,
            posicaoEnum,
            minGolos,
            minCartoes,
            minJogos
        );
        return jogadores.stream()
            .map(Mapper::jogadorMapToDto)
            .toList();
    }

    public List<JogadorDTO> findByPosition(String position) {
        Posicao posicaoEnum;
        posicaoEnum = Posicao.valueOf(position.trim().toUpperCase());
        List<Jogador> jogadores = jogadorRepository.findByPosicao(posicaoEnum);
        return jogadores.stream()
        .map(Mapper::jogadorMapToDto)
        .toList();
    }

    public List<JogadorDTO> findByName(String name) {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Escreva o nome do jogador que deseja pesquisar");
        }

        List<Jogador> jogadores = jogadorRepository.findByNomeContainingIgnoreCase(name.trim());
        return jogadores.stream()
                .map(Mapper::jogadorMapToDto)
                .toList();
    }



    private void validarJogador(JogadorDTO jogadorDTO, long idAtual) {
        if (jogadorDTO.getNome() == null || jogadorDTO.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        
        if (jogadorDTO.getUsername() == null || jogadorDTO.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username é obrigatório");
        }
        
        if (jogadorDTO.getPassword() == null || jogadorDTO.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password é obrigatória");
        }
        
        if (jogadorDTO.getPosicao() == null || jogadorDTO.getPosicao().trim().isEmpty()) {
            throw new IllegalArgumentException("Posição é obrigatória");
        }

        Jogador jogadorExistente = jogadorRepository.findByUsername(jogadorDTO.getUsername());
        if (jogadorExistente != null && jogadorExistente.getId() != idAtual) {
            throw new IllegalArgumentException("Username já existe");
        }
    }
}