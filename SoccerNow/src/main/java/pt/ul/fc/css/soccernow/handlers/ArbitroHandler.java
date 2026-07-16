package pt.ul.fc.css.soccernow.handlers;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pt.ul.fc.css.soccernow.Mapper;
import pt.ul.fc.css.soccernow.dominio.Arbitro;
import pt.ul.fc.css.soccernow.dto.ArbitroDTO;
import pt.ul.fc.css.soccernow.repositorio.ArbitroRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ArbitroHandler {
    
    private final ArbitroRepository arbitroRepository;


    public ArbitroHandler(ArbitroRepository arbitroRepository) {
        this.arbitroRepository = arbitroRepository;
    }

    public ArbitroDTO create(ArbitroDTO refereeDTO) {
        
        validarArbitro(refereeDTO, -1);
        
        Arbitro arbitro = new Arbitro();
        arbitro.setNome(refereeDTO.getNome().trim());
        arbitro.setUsername(refereeDTO.getUsername().trim());
        arbitro.setPassword(refereeDTO.getPassword().trim());
        arbitro.setCertificated(refereeDTO.getIsCertificated());

        Arbitro savedArbitro = arbitroRepository.save(arbitro);

        refereeDTO.setId(savedArbitro.getId());

        return refereeDTO;
    }

    public ArbitroDTO update(Long id, ArbitroDTO refereeDTO) {
        validarArbitro(refereeDTO, id);

        Optional<Arbitro> optionalReferee = arbitroRepository.findById(id);
        if (optionalReferee.isEmpty()) {
            throw new IllegalArgumentException("Árbitro não encontrado");
        }
    
        Arbitro existingReferee = optionalReferee.get();

        // Campos herdados de Utilizador
        existingReferee.setNome(refereeDTO.getNome().trim());
        existingReferee.setUsername(refereeDTO.getUsername().trim());
        existingReferee.setPassword(refereeDTO.getPassword().trim());
        
        // Campos específicos de Arbitro
        existingReferee.setCertificated(refereeDTO.getIsCertificated());

        Arbitro savedArbitro = arbitroRepository.save(existingReferee);
        refereeDTO.setId(savedArbitro.getId());
        
        return refereeDTO;
    }

    public ArbitroDTO delete(Long id) {
        Optional<Arbitro> optionalReferee = arbitroRepository.findById(id);
        if (!optionalReferee.isPresent()) {
            throw new IllegalArgumentException("Árbitro não encontrado");
        }

        Arbitro referee = optionalReferee.get();

        // Verificar se o árbitro está escalado para jogos futuros
        if (referee.getJogosOficiados() != null && referee.getJogosOficiados().stream()
                .anyMatch(jogo -> jogo.getData().isAfter(LocalDate.now()) || 
                        (jogo.getData().isEqual(LocalDate.now()) && LocalTime.parse(jogo.getHora()).isAfter(LocalDateTime.now().toLocalTime())))) {
            throw new IllegalArgumentException("Árbitro não pode ser eliminado, pois está escalado para jogos futuros.");
        }

        arbitroRepository.deleteById(id);
        return Mapper.arbitroMapToDto(referee);
    }

    public ArbitroDTO findById(Long id) {  
        Optional<Arbitro> optionalArbitro = arbitroRepository.findById(id);
        if(!optionalArbitro.isPresent()) {
            return null;
        }
        return Mapper.arbitroMapToDto(optionalArbitro.get());
    }

    public List<ArbitroDTO> findAll() {
        List<Arbitro> arbitros = arbitroRepository.findAll();
        return arbitros.stream()
                .map(Mapper::arbitroMapToDto)
                .toList();
    }




    // Função de validação centralizada
    private void validarArbitro(ArbitroDTO arbitroDTO, long idAtual) {
        // 1. Validar campos obrigatórios
        if (arbitroDTO.getNome() == null || arbitroDTO.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        
        if (arbitroDTO.getUsername() == null || arbitroDTO.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username é obrigatório");
        }
        
        if (arbitroDTO.getPassword() == null || arbitroDTO.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password é obrigatória");
        }


        Arbitro arbitroExistente = arbitroRepository.findByUsername(arbitroDTO.getUsername());
        if (arbitroExistente != null && arbitroExistente.getId() != (idAtual)) {
            throw new IllegalArgumentException("Username já existe");
        }
    }
}
