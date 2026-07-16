package pt.ul.fc.css.soccernow.handlers;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pt.ul.fc.css.soccernow.Mapper;
import pt.ul.fc.css.soccernow.dominio.Campeonato;
import pt.ul.fc.css.soccernow.dominio.CampeonatoPontos;
import pt.ul.fc.css.soccernow.dominio.Equipa;
import pt.ul.fc.css.soccernow.dto.CampeonatoDTO;
import pt.ul.fc.css.soccernow.repositorio.CampeonatoRepository;
import pt.ul.fc.css.soccernow.repositorio.EquipaRepository;

@Service
@Transactional
public class CampeonatoHandler {

    private final CampeonatoRepository campeonatoRepository;
    private final EquipaRepository equipaRepository;

    public CampeonatoHandler(CampeonatoRepository campeonatoRepository, EquipaRepository equipaRepository) {
        this.campeonatoRepository = campeonatoRepository;
        this.equipaRepository = equipaRepository;
    }

    public CampeonatoDTO criarCampeonato(String nome, List<String> equipasNomes) {
        if (equipasNomes.size() < 8) throw new IllegalArgumentException("Campeonato precisa de pelo menos 8 equipas");
        if (campeonatoRepository.findByNome(nome).isPresent()) {
            throw new IllegalArgumentException("Já existe um campeonato com esse nome");
        }
        List<Equipa> equipas = equipasNomes.stream()
            .map(equipaRepository::findByNomeIgnoreCase)
            .toList();
        if (equipas.contains(null)) throw new IllegalArgumentException("Uma ou mais equipas não existem");
        CampeonatoPontos campeonato = new CampeonatoPontos(nome, equipas, List.of());
        Campeonato saved = campeonatoRepository.save(campeonato);
        return Mapper.campeonatoMapToDto(saved);
    }

    public List<CampeonatoDTO> listarCampeonatos() {
        return campeonatoRepository.findAll().stream()
            .map(Mapper::campeonatoMapToDto)
            .toList();
    }

    public CampeonatoDTO removerCampeonato(Long id) {
        Optional<Campeonato> opt = campeonatoRepository.findById(id);
        if (opt.isEmpty()) throw new IllegalArgumentException("Campeonato não encontrado");
        campeonatoRepository.deleteById(id);
        return Mapper.campeonatoMapToDto(opt.get());
    }

    public CampeonatoDTO atualizarCampeonato(Long id, CampeonatoDTO dto) {
        Optional<Campeonato> opt = campeonatoRepository.findById(id);
        if (opt.isEmpty()) throw new IllegalArgumentException("Campeonato não encontrado");
        Campeonato camp = opt.get();

        // Atualiza nome
        camp.setNome(dto.getNome());

        // Atualiza equipas (se fornecido)
        if (dto.getEquipasNomes() != null && !dto.getEquipasNomes().isEmpty()) {
            List<Equipa> equipas = dto.getEquipasNomes().stream()
                .map(equipaRepository::findByNomeIgnoreCase)
                .toList();
            if (equipas.contains(null)) throw new IllegalArgumentException("Uma ou mais equipas não existem");
            camp.getEquipas().clear();
            camp.getEquipas().addAll(equipas);
        }

        Campeonato updated = campeonatoRepository.save(camp);
        return Mapper.campeonatoMapToDto(updated);
    }

    public List<CampeonatoDTO> filtrarCampeonatos(
        String nome,
        String equipa,
        Integer realizados,
        Integer porRealizar
    ) {
        List<Campeonato> campeonatos = campeonatoRepository.findAll();

        return campeonatos.stream()
            .filter(c -> nome == null || nome.isBlank() || c.getNome().toLowerCase().contains(nome.toLowerCase()))
            .filter(c -> equipa == null || equipa.isBlank() || 
                (c.getEquipas() != null && c.getEquipas().stream()
                    .anyMatch(e -> e.getNome().equalsIgnoreCase(equipa))))
            .filter(c -> realizados == null || 
                (int) c.getJogos().stream().filter(j -> j.getEstatisticas() != null).count() == realizados)
            .filter(c -> porRealizar == null || 
                (int) c.getJogos().stream().filter(j -> j.getEstatisticas() == null).count() == porRealizar)
            .map(Mapper::campeonatoMapToDto)
            .toList();
    }
}