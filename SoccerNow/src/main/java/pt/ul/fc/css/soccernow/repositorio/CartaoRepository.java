package pt.ul.fc.css.soccernow.repositorio;

import org.springframework.stereotype.Repository;

import pt.ul.fc.css.soccernow.dominio.Cartao;

import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface CartaoRepository extends JpaRepository<Cartao, Long>  {
    
}
