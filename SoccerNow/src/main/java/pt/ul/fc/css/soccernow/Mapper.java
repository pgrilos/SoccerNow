package pt.ul.fc.css.soccernow;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import pt.ul.fc.css.soccernow.dominio.Arbitro;
import pt.ul.fc.css.soccernow.dominio.Campeonato;
import pt.ul.fc.css.soccernow.dominio.CampeonatoPontos;
import pt.ul.fc.css.soccernow.dominio.Cartao;
import pt.ul.fc.css.soccernow.dominio.Equipa;
import pt.ul.fc.css.soccernow.dominio.EquipaJogo;
import pt.ul.fc.css.soccernow.dominio.EstatisticasJogo;
import pt.ul.fc.css.soccernow.dominio.Jogador;
import pt.ul.fc.css.soccernow.dominio.Jogo;
import pt.ul.fc.css.soccernow.dominio.JogoCampeonato;
import pt.ul.fc.css.soccernow.dominio.Localizacao;
import pt.ul.fc.css.soccernow.dto.ArbitroDTO;
import pt.ul.fc.css.soccernow.dto.CampeonatoDTO;
import pt.ul.fc.css.soccernow.dto.CartaoDTO;
import pt.ul.fc.css.soccernow.dto.EquipaDTO;
import pt.ul.fc.css.soccernow.dto.EquipaJogoDTO;
import pt.ul.fc.css.soccernow.dto.EquipaPontuacaoDTO;
import pt.ul.fc.css.soccernow.dto.EstatisticasDTO;
import pt.ul.fc.css.soccernow.dto.JogadorDTO;
import pt.ul.fc.css.soccernow.dto.JogoDTO;
import pt.ul.fc.css.soccernow.dto.LocalizacaoDTO;

@Component 
public class Mapper {
    
    public static JogadorDTO jogadorMapToDto(Jogador jogador) {
        JogadorDTO dto = new JogadorDTO();
        dto.setId(jogador.getId());
        dto.setNome(jogador.getNome());
        dto.setUsername(jogador.getUsername());
        dto.setPassword(jogador.getPassword());
        dto.setPosicao(jogador.getPosicao().toString());

        dto.setNumJogos(jogador.getJogos().size());
        dto.setNumGolos(jogador.getGolosMarcados());
        dto.setNumCartoesAmarelos(jogador.getCartoesRecebidos().stream()
            .filter(cartao -> cartao.getCorCartao().toString().equals("AMARELO"))
            .count());
        dto.setNumCartoesVermelhos(jogador.getCartoesRecebidos().stream()
            .filter(cartao -> cartao.getCorCartao().toString().equals("VERMELHO"))
            .count());

        return dto;
    }

    public static ArbitroDTO arbitroMapToDto(Arbitro arbitro) {
        ArbitroDTO dto = new ArbitroDTO();
        dto.setId(arbitro.getId());
        dto.setNome(arbitro.getNome());
        dto.setUsername(arbitro.getUsername());
        dto.setPassword(arbitro.getPassword());
        dto.setIsCertificated(arbitro.getIsCertificated());

        if (arbitro.getJogosOficiados() != null) {
            dto.setJogosOficiados(
                arbitro.getJogosOficiados().stream()
                    .map(j -> j.getId())
                    .toList()
            );
        }

        if (arbitro.getCartoesMostrados() != null) {
            dto.setCartoesMostrados(
                arbitro.getCartoesMostrados().stream()
                    .map(c -> c.getId())
                    .toList()
            );
        }

        return dto;
    }

    public static EquipaDTO equipaMapToDto(Equipa equipa) {
        EquipaDTO dto = new EquipaDTO();
        dto.setId(equipa.getId());
        dto.setNome(equipa.getNome());
        dto.setVitorias(equipa.getVitorias());
        dto.setEmpates(equipa.getEmpates());
        dto.setDerrotas(equipa.getDerrotas());
        //dto.setConquistas(equipa.getConquistas()); falta esta parte
        
        // Mapear jogadores
        if (equipa.getJogadores() != null) {
            List<JogadorDTO> jogadoresDTO = equipa.getJogadores().stream()
                .map(jogador -> {
                    return jogadorMapToDto(jogador);
                })
                .collect(Collectors.toList());
            
            dto.setJogadores(jogadoresDTO.stream()
                .map(jogadorDTO -> jogadorDTO.getUsername())
                .collect(Collectors.toList()));
        }
        
        return dto;
    }

    public static JogoDTO jogoMapToDto(Jogo jogo) {
        JogoDTO dto = new JogoDTO();
        dto.setId(jogo.getId());
        dto.setData(jogo.getData().toString());
        dto.setHora(jogo.getHora());    
        dto.setLocal(localizacaoMapToDto(jogo.getLocal()));

        dto.setEquipaJogoCasa(equipaJogoMapToDto(jogo.getEquipaJogoCasa()));
        dto.setEquipaJogoVisitante(equipaJogoMapToDto(jogo.getEquipaJogoVisitante()));
        
        dto.setArbitroPrincipal(jogo.getArbitroPrincipal().getUsername());
        dto.setArbitros(jogo.getArbitros().stream() 
            .map(arbitro -> arbitro.getUsername())
            .collect(Collectors.toList()));
        dto.setEstatisticas(jogo.getEstatisticas() != null ? estatisticasMapToDto(jogo.getEstatisticas()) : null);
        if (jogo instanceof JogoCampeonato jc && jc.getCampeonato() != null) {
            dto.setCampeonato(jc.getCampeonato().getNome());
        } else {
            dto.setCampeonato(null);
        }

        return dto;
    }

    public static EquipaJogoDTO equipaJogoMapToDto(EquipaJogo equipaJogo) {
        EquipaJogoDTO dto = new EquipaJogoDTO();
        dto.setId(equipaJogo.getId());
        dto.setEquipa(equipaJogo.getEquipa().getNome());
        dto.setJogadoresParticipantes(equipaJogo.getJogadoresParticipantes().stream()
            .map(jogador -> jogador.getUsername())
            .collect(Collectors.toList()));
        return dto;
    }

    public static EstatisticasDTO estatisticasMapToDto(EstatisticasJogo estatisticas) {
        EstatisticasDTO estatisticasDTO = new EstatisticasDTO();
        estatisticasDTO.setId(estatisticas.getId());
        estatisticasDTO.setMarcadoresEquipaCasa(estatisticas.getMarcadoresEquipaCasa().stream()
            .map(jogador -> jogador.getNome())
            .collect(Collectors.toList()));
        estatisticasDTO.setMarcadoresEquipaVisitante(estatisticas.getMarcadoresEquipaVisitante().stream()
            .map(jogador -> jogador.getNome())
            .collect(Collectors.toList()));
        estatisticasDTO.setCartoes(estatisticas.getCartoes().stream()
            .map(cartao -> cartaoMapToDto(cartao))
            .collect(Collectors.toList()));
        return estatisticasDTO;
    }

    public static CartaoDTO cartaoMapToDto(Cartao cartao) {
        CartaoDTO dto = new CartaoDTO();
        dto.setId(cartao.getId());
        dto.setEquipa(cartao.getEquipa().getNome());
        dto.setJogador(cartao.getJogador().getNome());
        // Corrige para "Amarelo"/"Vermelho"
        String cor = cartao.getCorCartao().name();
        cor = cor.substring(0, 1).toUpperCase() + cor.substring(1).toLowerCase();
        dto.setCorCartao(cor);
        return dto;
    }
    
    public static LocalizacaoDTO localizacaoMapToDto(Localizacao local) {
        LocalizacaoDTO dto = new LocalizacaoDTO();
        dto.setCidade(local.getCidade());
        dto.setRua(local.getRua());

        return dto;
    }

    public static Localizacao localizacaoDtoToEntity(LocalizacaoDTO local) {
        Localizacao loc = new Localizacao(local.getRua(), local.getCidade());
        return loc;
    }

    public static CampeonatoDTO campeonatoMapToDto(Campeonato campeonato) {
        CampeonatoDTO dto = new CampeonatoDTO();
        dto.setId(campeonato.getId());
        dto.setNome(campeonato.getNome());
        
        if (campeonato.getJogos() != null) {
            dto.setJogos(
                campeonato.getJogos().stream()
                    .filter(j -> j != null && j.isAtivo())
                    .map(Mapper::jogoMapToDto)
                    .collect(Collectors.toList())
            );
        }

        dto.setEquipasNomes(
            campeonato.getEquipas().stream()
                .map(Equipa::getNome)
                .collect(Collectors.toList())
        );   
        // Adiciona os jogos
        if (campeonato.getJogos() != null) {
            dto.setJogos(
                campeonato.getJogos().stream()
                    .filter(Objects::nonNull)
                    .map(Mapper::jogoMapToDto)
                    .collect(Collectors.toList())
            );
        }

        if (campeonato instanceof CampeonatoPontos cp && cp.getClassificacao() != null) {
            dto.setClassificacao(
                cp.getClassificacao().entrySet().stream()
                    .map(e -> new EquipaPontuacaoDTO(e.getKey().getNome(), e.getValue()))
                    .sorted((a, b) -> Integer.compare(b.getPontos(), a.getPontos())) // ordem decrescente
                    .toList()
            );
        }

        return dto;
    }

   
    



}
