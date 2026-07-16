package pt.ul.fc.css.soccernow.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.soccernow.dominio.Jogador;
import pt.ul.fc.css.soccernow.dominio.Posicao;


@Repository
public interface JogadorRepository extends JpaRepository<Jogador, Long> {
    List<Jogador> findByNomeContainingIgnoreCase(String nome);
    List<Jogador> findByPosicao(Posicao posicao);
    Jogador findByUsername(String username);

    @Query("""
    SELECT j FROM Jogador j
    WHERE (:nome IS NULL OR LOWER(j.nome) LIKE LOWER(CONCAT('%', :nome, '%')))
    AND (:posicao IS NULL OR j.posicao = :posicao)
    AND (:minGolos IS NULL OR j.golosMarcados >= :minGolos)
    AND (:minCartoes IS NULL OR SIZE(j.cartoesRecebidos) >= :minCartoes)
    AND (:minJogos IS NULL OR SIZE(j.equipasJogo) >= :minJogos)
    """)
    List<Jogador> filtrarJogadores(
        @Param("nome") String nome,
        @Param("posicao") Posicao posicao,
        @Param("minGolos") Integer minGolos,
        @Param("minCartoes") Integer minCartoes,
        @Param("minJogos") Integer minJogos
    );
}