package pt.ul.fc.css.soccernow.repositorio;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pt.ul.fc.css.soccernow.dominio.Jogo;

@Repository
public interface JogoRepository extends JpaRepository<Jogo, Long> {
    List<Jogo> findByAtivoTrue();
    Optional<Jogo> findByIdAndAtivoTrue(Long id);

}
