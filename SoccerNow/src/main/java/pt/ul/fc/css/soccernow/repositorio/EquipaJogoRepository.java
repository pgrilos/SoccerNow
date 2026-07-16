package pt.ul.fc.css.soccernow.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pt.ul.fc.css.soccernow.dominio.EquipaJogo;

@Repository
public interface EquipaJogoRepository extends JpaRepository<EquipaJogo, Long> {
    
}