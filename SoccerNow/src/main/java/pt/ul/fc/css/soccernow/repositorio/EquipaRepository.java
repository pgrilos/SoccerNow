package pt.ul.fc.css.soccernow.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pt.ul.fc.css.soccernow.dominio.Equipa;

@Repository
public interface EquipaRepository extends JpaRepository<Equipa, Long> {
    Equipa findByNomeIgnoreCase(String nome);
}