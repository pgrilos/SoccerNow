// package pt.ul.fc.css.soccernow.handlers;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.anyList;
// import static org.mockito.ArgumentMatchers.anyLong;
// import static org.mockito.Mockito.*;

// import java.lang.reflect.Field;
// import java.time.LocalDate;
// import java.util.Arrays;
// import java.util.List;
// import java.util.Optional;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;

// import pt.ul.fc.css.soccernow.dominio.*;
// import pt.ul.fc.css.soccernow.dto.CartaoDTO;
// import pt.ul.fc.css.soccernow.dto.EquipaJogoDTO;
// import pt.ul.fc.css.soccernow.dto.EstatisticasDTO;
// import pt.ul.fc.css.soccernow.dto.JogoDTO;
// import pt.ul.fc.css.soccernow.dto.LocalizacaoDTO;
// import pt.ul.fc.css.soccernow.repositorio.ArbitroRepository;
// import pt.ul.fc.css.soccernow.repositorio.CartaoRepository;
// import pt.ul.fc.css.soccernow.repositorio.EquipaJogoRepository;
// import pt.ul.fc.css.soccernow.repositorio.EquipaRepository;
// import pt.ul.fc.css.soccernow.repositorio.EstatisticasJogoRepository;
// import pt.ul.fc.css.soccernow.repositorio.JogadorRepository;
// import pt.ul.fc.css.soccernow.repositorio.JogoRepository;

// @ExtendWith(MockitoExtension.class)
// public class JogoHandlerTest {

//     @Mock
//     private JogoRepository jogoRepository;
//     @Mock
//     private ArbitroRepository arbitroRepository;
//     @Mock
//     private EquipaRepository equipaRepository;
//     @Mock
//     private JogadorRepository jogadorRepository;
//     @Mock
//     private CartaoRepository cartaoRepository;
//     @Mock
//     private EstatisticasJogoRepository estatisticasJogoRepository;
//     @Mock
//     private EquipaJogoRepository equipaJogoRepository;

//     @InjectMocks
//     private JogoHandler jogoHandler;

//     private JogoDTO jogoDTO;
//     private Jogo savedJogo;
//     private Equipa equipaCasa;
//     private Equipa equipaVisitante;
//     private Arbitro arbitroPrincipal;
//     private List<Arbitro> arbitros;

//     @BeforeEach
//     void setUp() {
//         equipaCasa = new Equipa("Equipa A");
//         equipaVisitante = new Equipa("Equipa B");
//         arbitroPrincipal = new Arbitro();
//         arbitroPrincipal.setNome("Arbitro Principal");
//         arbitroPrincipal.setUsername("principal");
//         arbitroPrincipal.setPassword("123");
//         arbitroPrincipal.setCertificated(true);
//         arbitros = Arrays.asList(arbitroPrincipal);

//         List<String> jogadoresCasaUsernames = Arrays.asList("casa1", "casa2", "casa3", "casa4", "casa5");
//         List<String> jogadoresVisitanteUsernames = Arrays.asList("visitante1", "visitante2", "visitante3", "visitante4", "visitante5");

//         List<Jogador> jogadoresCasa = Arrays.asList(
//             new Jogador("Jogador Casa 1", Posicao.FIXO, "casa1", "senha1"),
//             new Jogador("Jogador Casa 2", Posicao.FIXO, "casa2", "senha2"),
//             new Jogador("Jogador Casa 3", Posicao.PIVO, "casa3", "senha3"),
//             new Jogador("Jogador Casa 4", Posicao.GUARDA_REDES, "casa4", "senha4"),
//             new Jogador("Jogador Casa 5", Posicao.ALA, "casa5", "senha5")
//         );
//         equipaCasa.setJogadores(jogadoresCasa);

//         List<Jogador> jogadoresVisitante = Arrays.asList(
//             new Jogador("Jogador Visitante 1", Posicao.FIXO, "visitante1", "senha1"),
//             new Jogador("Jogador Visitante 2", Posicao.PIVO, "visitante2", "senha2"),
//             new Jogador("Jogador Visitante 3", Posicao.FIXO, "visitante3", "senha3"),
//             new Jogador("Jogador Visitante 4", Posicao.GUARDA_REDES, "visitante4", "senha4"),
//             new Jogador("Jogador Visitante 5", Posicao.ALA, "visitante5", "senha5")
//         );
//         equipaVisitante.setJogadores(jogadoresVisitante);

//         // Crie EquipaJogo para cada equipa
//         EquipaJogo equipaJogoCasa = new EquipaJogo(jogadoresCasa, equipaCasa);
//         equipaJogoCasa.setEquipa(equipaCasa);

//         EquipaJogo equipaJogoVisitante = new EquipaJogo(jogadoresVisitante, equipaVisitante);
//         equipaJogoVisitante.setEquipa(equipaVisitante);


//         EquipaJogoDTO equipaJogoCasaDTO = new EquipaJogoDTO();
//         equipaJogoCasaDTO.setEquipa("Equipa A");
//         equipaJogoCasaDTO.setJogadoresParticipantes(jogadoresCasaUsernames);

//         EquipaJogoDTO equipaJogoVisitanteDTO = new EquipaJogoDTO();
//         equipaJogoVisitanteDTO.setEquipa("Equipa B");
//         equipaJogoVisitanteDTO.setJogadoresParticipantes(jogadoresVisitanteUsernames);



//         jogoDTO = new JogoDTO();


//         LocalizacaoDTO loc = new LocalizacaoDTO();
//         loc.setRua("Estádio Municipal");
//         loc.setCidade("Lisboa");

//         jogoDTO.setData("2025-05-02");
//         jogoDTO.setHora("15:00");
//         jogoDTO.setLocal(loc);
//         jogoDTO.setEquipaJogoCasa(equipaJogoCasaDTO);
//         jogoDTO.setEquipaJogoVisitante(equipaJogoVisitanteDTO);
//         jogoDTO.setArbitroPrincipal("principal");
//         jogoDTO.setArbitros(Arrays.asList("principal"));


//         savedJogo = createJogoComId(1L, LocalDate.parse("2025-05-02"), "15:00", new Localizacao("Estádio Municipal","Lisboa"), equipaJogoCasa, equipaJogoVisitante, arbitros, arbitroPrincipal);
//     }

//     @Test
//     void testCreateJogo() {

//         List<Jogador> jogadoresCasa = Arrays.asList(
//             new Jogador("Jogador Casa 1", Posicao.FIXO, "casa1", "senha1"),
//             new Jogador("Jogador Casa 2", Posicao.FIXO, "casa2", "senha2"),
//             new Jogador("Jogador Casa 3", Posicao.PIVO, "casa3", "senha3"),
//             new Jogador("Jogador Casa 4", Posicao.GUARDA_REDES, "casa4", "senha4"),
//             new Jogador("Jogador Casa 5", Posicao.ALA, "casa5", "senha5")
//         );
//         equipaCasa.setJogadores(jogadoresCasa);
    
//         // Criar jogadores para a equipaVisitante
//         List<Jogador> jogadoresVisitante = Arrays.asList(
//             new Jogador("Jogador Visitante 1", Posicao.FIXO, "visitante1", "senha1"),
//             new Jogador("Jogador Visitante 2", Posicao.PIVO, "visitante2", "senha2"),
//             new Jogador("Jogador Visitante 3", Posicao.FIXO, "visitante3", "senha3"),
//             new Jogador("Jogador Visitante 4", Posicao.GUARDA_REDES, "visitante4", "senha4"),
//             new Jogador("Jogador Visitante 5", Posicao.ALA, "visitante5", "senha5")
//         );
//         equipaVisitante.setJogadores(jogadoresVisitante);

        
//         // Mock para garantir que o mesmo objeto é retornado
//         for (Jogador jogador : jogadoresCasa) {
//             when(jogadorRepository.findByUsername(jogador.getUsername())).thenReturn(jogador);
//         }
//         for (Jogador jogador : jogadoresVisitante) {
//             when(jogadorRepository.findByUsername(jogador.getUsername())).thenReturn(jogador);
//         }

//         lenient().when(equipaRepository.findByNome("Equipa A")).thenReturn(equipaCasa);
//         lenient().when(equipaRepository.findByNome("Equipa B")).thenReturn(equipaVisitante);
//         lenient().when(arbitroRepository.findByUsername("principal")).thenReturn(arbitroPrincipal);
//         lenient().when(arbitroRepository.findAll()).thenReturn(arbitros);
//         lenient().when(jogoRepository.save(any(Jogo.class))).thenReturn(savedJogo);

//         JogoDTO result = jogoHandler.create(jogoDTO);

//         assertNotNull(result);
//         assertEquals(1L, result.getId());
//         assertEquals("2025-05-02", result.getData());
//         assertEquals("15:00", result.getHora());
//         assertEquals("Estádio Municipal", result.getLocal().getRua());
//         verify(jogoRepository, times(1)).save(any(Jogo.class));
//     }

//     @Test
//     void testCreateJogo_EquipaCasaNula_ThrowsIllegalArgumentException() {
//         jogoDTO.setEquipaJogoCasa(null);

//         Exception exception = assertThrows(IllegalArgumentException.class, () -> jogoHandler.create(jogoDTO));
//         assertEquals("As equipas são obrigatórias", exception.getMessage());
//         verify(jogoRepository, times(0)).save(any(Jogo.class));
//     }

//     @Test
//     void testCreateJogo_EquipaVisitanteIgualCasa_ThrowsIllegalArgumentException() {
        
//         EquipaJogoDTO equipaJogoCasaDTO = new EquipaJogoDTO();
//         equipaJogoCasaDTO.setEquipa("Equipa A");
//         equipaJogoCasaDTO.setJogadoresParticipantes(Arrays.asList("casa1", "casa2", "casa3", "casa4", "casa5"));

//         EquipaJogoDTO equipaJogoVisitanteDTO = new EquipaJogoDTO();
//         equipaJogoVisitanteDTO.setEquipa("Equipa A");
//         equipaJogoVisitanteDTO.setJogadoresParticipantes(Arrays.asList("visitante1", "visitante2", "visitante3", "visitante4", "visitante5"));

//         jogoDTO.setEquipaJogoCasa(equipaJogoCasaDTO);
//         jogoDTO.setEquipaJogoVisitante(equipaJogoVisitanteDTO);


//         Exception exception = assertThrows(IllegalArgumentException.class, () -> jogoHandler.create(jogoDTO));
//         assertEquals("As equipas não podem ser iguais", exception.getMessage());
//         verify(jogoRepository, times(0)).save(any(Jogo.class));
//     }

// @Test
// void testDeleteJogo_ExistingId_ReturnsJogoDTO() {
//     when(jogoRepository.findByIdAndAtivoTrue(1L)).thenReturn(Optional.of(savedJogo));

//     JogoDTO result = jogoHandler.delete(1L);

//     assertNotNull(result);
//     assertEquals(1L, result.getId());
//     assertEquals("2025-05-02", result.getData());
//     assertFalse(savedJogo.isAtivo(), "O campo 'ativo' deve ser false após soft delete");
//     verify(jogoRepository, times(1)).findByIdAndAtivoTrue(1L);
//     verify(jogoRepository, times(1)).save(savedJogo);
//     verify(jogoRepository, times(0)).deleteById(anyLong());
// }

//     @Test
//     void testDeleteJogo_NonExistingId_ThrowsIllegalArgumentException() {
//         when(jogoRepository.findByIdAndAtivoTrue(100L)).thenReturn(Optional.empty());

//         Exception exception = assertThrows(IllegalArgumentException.class, () -> jogoHandler.delete(100L));
//         assertEquals("Jogo não encontrado", exception.getMessage());
//         verify(jogoRepository, times(1)).findByIdAndAtivoTrue(100L);
//         verify(jogoRepository, times(0)).save(any(Jogo.class));
//         verify(jogoRepository, times(0)).deleteById(anyLong());
//     }

//     @Test
//     void testFindAll_ExistingJogos_ReturnsListOfJogoDTOs() {
//         Equipa equipaC = new Equipa("Equipa C");
//         Equipa equipaD = new Equipa("Equipa D");
//         List<Jogador> jogadoresC = Arrays.asList(
//             new Jogador("Jogador C1", Posicao.FIXO, "c1", "senha"),
//             new Jogador("Jogador C2", Posicao.ALA, "c2", "senha"),
//             new Jogador("Jogador C3", Posicao.PIVO, "c3", "senha"),
//             new Jogador("Jogador C4", Posicao.GUARDA_REDES, "c4", "senha")
//         );
//         List<Jogador> jogadoresD = Arrays.asList(
//             new Jogador("Jogador D1", Posicao.FIXO, "d1", "senha"),
//             new Jogador("Jogador D2", Posicao.ALA, "d2", "senha"),
//             new Jogador("Jogador D3", Posicao.PIVO, "d3", "senha"),
//             new Jogador("Jogador D4", Posicao.GUARDA_REDES, "d4", "senha")
//         );
//         equipaC.setJogadores(jogadoresC);
//         equipaD.setJogadores(jogadoresD);

//         EquipaJogo equipaJogoC = new EquipaJogo(jogadoresC, equipaC);
//         equipaJogoC.setEquipa(equipaC);
//         EquipaJogo equipaJogoD = new EquipaJogo(jogadoresD, equipaD);
//         equipaJogoD.setEquipa(equipaD);
//         Jogo savedJogo2 = createJogoComId(2L, LocalDate.parse("2025-05-03"), "16:00", new Localizacao("Outro Estádio", "Lisboa"), equipaJogoC, equipaJogoD, arbitros, arbitroPrincipal);

//         List<Jogo> jogos = Arrays.asList(savedJogo, savedJogo2);
//         when(jogoRepository.findAll()).thenReturn(jogos);

//         List<JogoDTO> results = jogoHandler.findAll();

//         assertNotNull(results);
//         assertEquals(2, results.size());
//         assertEquals(savedJogo.getId(), results.get(0).getId());
//         assertEquals(savedJogo2.getId(), results.get(1).getId());
//         verify(jogoRepository, times(1)).findAll();
//     }

//     @Test
//     void testDeleteJogo_ChamaSoftDelete() {
//         when(jogoRepository.findByIdAndAtivoTrue(1L)).thenReturn(Optional.of(savedJogo));

//         JogoDTO result = jogoHandler.delete(1L);

//         assertNotNull(result);
//         assertEquals(1L, result.getId());
//         assertFalse(savedJogo.isAtivo(), "O campo 'ativo' deve ser false após soft delete");
//         verify(jogoRepository, times(1)).findByIdAndAtivoTrue(1L);
//         verify(jogoRepository, times(1)).save(savedJogo);
//         verify(jogoRepository, times(0)).deleteById(anyLong());
//     }

//     @Test
//     void testCreateJogo_EquipaVisitanteNula_ThrowsIllegalArgumentException() {
//         jogoDTO.setEquipaJogoVisitante(null);
//         Exception exception = assertThrows(IllegalArgumentException.class, () -> jogoHandler.create(jogoDTO));
//         assertEquals("As equipas são obrigatórias", exception.getMessage());
//         verify(jogoRepository, times(0)).save(any(Jogo.class));
//     }

//     @Test
//     void testCreateJogo_DataNula_ThrowsIllegalArgumentException() {
//         jogoDTO.setData(null);
//         Exception exception = assertThrows(IllegalArgumentException.class, () -> jogoHandler.create(jogoDTO));
//         assertEquals("A data do jogo é obrigatória", exception.getMessage());
//         verify(jogoRepository, times(0)).save(any(Jogo.class));
//     }

//     @Test
//     void testFindAll_OrdemDosJogos() {
//         // Crie EquipaJogo para cada equipa
//         EquipaJogo equipaJogoCasa = new EquipaJogo(equipaCasa.getJogadores(), equipaCasa);
//         equipaJogoCasa.setEquipa(equipaCasa);

//         EquipaJogo equipaJogoVisitante = new EquipaJogo(equipaVisitante.getJogadores(), equipaVisitante);
//         equipaJogoVisitante.setEquipa(equipaVisitante);

//         Jogo jogo1 = createJogoComId(1L, LocalDate.parse("2025-05-01"), "10:00", new Localizacao("campo 1","lisboa"), equipaJogoCasa, equipaJogoVisitante, arbitros, arbitroPrincipal);
//         Jogo jogo2 = createJogoComId(2L, LocalDate.parse("2025-05-02"), "12:00", new Localizacao("campo 2","lisboa"), equipaJogoCasa, equipaJogoVisitante, arbitros, arbitroPrincipal);

//         when(jogoRepository.findAll()).thenReturn(Arrays.asList(jogo1, jogo2));
//         List<JogoDTO> results = jogoHandler.findAll();
//         assertEquals(1L, results.get(0).getId());
//         assertEquals(2L, results.get(1).getId());
//     }

//     @Test
//     void testUpdateResultado_Sucesso() {
//         // Mock do jogo existente
//         Jogo jogo = new Jogo();
//         Equipa equipaCasa = new Equipa("barcelona");
//         Equipa equipaVisitante = new Equipa("brasil");

//         Jogador jogadorCasa = new Jogador("Yamal", Posicao.GUARDA_REDES, "yamal", "password");
//         Jogador jogadorVisitante = new Jogador("Pele", Posicao.ALA, "pele", "password");
//         Jogador jogadorCartao = new Jogador("Vini",Posicao.ALA, "vini", "password");

//         equipaCasa.setJogadores(Arrays.asList(jogadorCasa));
//         equipaVisitante.setJogadores(Arrays.asList(jogadorVisitante, jogadorCartao));

//         EquipaJogo equipaJogoCasa = new EquipaJogo(Arrays.asList(jogadorCasa), equipaCasa);
//         equipaJogoCasa.setEquipa(equipaCasa);

//         EquipaJogo equipaJogoVisitante = new EquipaJogo(Arrays.asList(jogadorVisitante, jogadorCartao), equipaVisitante);
//         equipaJogoVisitante.setEquipa(equipaVisitante);

//         jogo.setEquipasJogo(Arrays.asList(equipaJogoCasa, equipaJogoVisitante));


//         lenient().when(jogoRepository.findById(1L)).thenReturn(Optional.of(jogo));
//         lenient().when(jogadorRepository.findByUsername("yamal")).thenReturn(jogadorCasa);
//         lenient().when(jogadorRepository.findByUsername("pele")).thenReturn(jogadorVisitante);
//         lenient().when(jogadorRepository.findByUsername("vini")).thenReturn(jogadorCartao);
//         lenient().when(equipaRepository.findByNome("brasil")).thenReturn(equipaVisitante);

//         // Mock do DTO de estatísticas
//         EstatisticasDTO estatisticasDTO = new EstatisticasDTO();
//         estatisticasDTO.setMarcadoresEquipaCasa(Arrays.asList("yamal"));
//         estatisticasDTO.setMarcadoresEquipaVisitante(Arrays.asList("pele"));
//         estatisticasDTO.setCartoes(Arrays.asList(
//             new CartaoDTO(null, "VERMELHO", "vini", "brasil")
//         ));
        
//         EstatisticasJogo estatisticas = new EstatisticasJogo();
//         when(estatisticasJogoRepository.save(any(EstatisticasJogo.class))).thenReturn(estatisticas);

//         // Mock do salvamento de cartões
//         Cartao cartao = new Cartao();
//         when(cartaoRepository.saveAll(anyList())).thenReturn(Arrays.asList(cartao));

//         // Executar o método
//         EstatisticasDTO resultado = jogoHandler.updateResultado(1L, estatisticasDTO);

//         // Verificar o resultado
//         assertNotNull(resultado);
//         assertEquals(1, resultado.getMarcadoresEquipaCasa().size());
//         assertEquals(1, resultado.getMarcadoresEquipaVisitante().size());
//         assertEquals(1, resultado.getCartoes().size());
//         assertEquals("VERMELHO", resultado.getCartoes().get(0).getCorCartao());
//         assertEquals("Vini", resultado.getCartoes().get(0).getJogador());

//         // Verificar interações com os repositórios
//         verify(jogoRepository).findById(1L);
//         verify(estatisticasJogoRepository).save(any(EstatisticasJogo.class));
//         verify(cartaoRepository).saveAll(anyList());
//         verify(jogoRepository).save(jogo);
//     }


//     // Auxiliar para criar Jogo com ID
//     private Jogo createJogoComId(Long id, LocalDate data, String hora, Localizacao local, EquipaJogo equipaCasa, EquipaJogo equipaVisitante, List<Arbitro> arbitros, Arbitro arbitroPrincipal) {
//         Jogo jogo = new JogoAmigavel();
//         jogo.setData(data);
//         jogo.setHora(hora);
//         jogo.setLocal(local);
//         jogo.setEquipasJogo(Arrays.asList(equipaCasa, equipaVisitante));
//         jogo.setArbitros(arbitros);
//         jogo.setArbitroPrincipal(arbitroPrincipal);

//         try {
//             Field idField = Jogo.class.getDeclaredField("id");
//             idField.setAccessible(true);
//             idField.set(jogo, id);
//         } catch (Exception e) {
//             fail("Falha ao definir ID");
//         }
//         return jogo;
//     }
// }