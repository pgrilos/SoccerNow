package pt.ul.fc.css.soccernow.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pt.ul.fc.css.soccernow.dominio.Campeonato;

public interface CampeonatoRepository extends JpaRepository<Campeonato, Long> {
    Optional<Campeonato> findByNome(String nome);

    @Query("SELECT c FROM Campeonato c JOIN c.equipas e WHERE e.nome = :nomeEquipa")
    List<Campeonato> findByEquipaNome(@Param("nomeEquipa") String nomeEquipa);
}
