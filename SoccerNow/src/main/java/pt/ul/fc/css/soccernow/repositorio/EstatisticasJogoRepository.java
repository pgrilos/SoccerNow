package pt.ul.fc.css.soccernow.repositorio;

import org.springframework.stereotype.Repository;

import pt.ul.fc.css.soccernow.dominio.EstatisticasJogo;

import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface EstatisticasJogoRepository extends JpaRepository<EstatisticasJogo, Long> {
    // Nenhum método adicional é necessário para salvar
}