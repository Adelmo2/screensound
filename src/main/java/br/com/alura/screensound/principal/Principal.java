package br.com.alura.screensound.principal;

import br.com.alura.screensound.model.Artista;
import br.com.alura.screensound.model.Musica;
import br.com.alura.screensound.model.TipoArtista;
import br.com.alura.screensound.repository.ArtistaRepository;
import br.com.alura.screensound.repository.MusicaRepository;
import br.com.alura.screensound.service.ConsultaChatGPT;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class Principal {
    @Autowired
    private ArtistaRepository artistaRepository;

    @Autowired
    private MusicaRepository musicaRepository;

    public String nomeArtista;

    public Principal(){};

    public ArtistaRepository getArtistaRepository() {
        return artistaRepository;
    }
    public void setArtistaRepository(ArtistaRepository artistaRepository) {
        this.artistaRepository = artistaRepository;
    }

    public MusicaRepository getMusicaRepository() {
        return musicaRepository;
    }
    public void setMusicaRepository(MusicaRepository musicaRepository) {
        this.musicaRepository = musicaRepository;
    }

    private Scanner leitura = new Scanner(System.in);

    @Transactional
    public void exibeMenu() {

        var opcao = -1;

        while (opcao != 9) {
            var menu = """                    
                    
                    *** Screen Sound Músicas **
                    
                    1- Cadastrar artistas
                    2- Cadastrar músicas
                    3- Listar músicas
                    4- Buscar músicas por artistas
                    5- Pesquisar dados sobre um artista
                    
                    9 - Sair
                    """;
            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    cadastrarArtista();
                    pausaTela(1);
                    break;
                case 2:
                    cadastrarMsicas();
                    break;
                case 3:
                    listarMusicas();
                    break;
                case 4:
                    musicasPorArtistas();
                    break;
                case 5:
                    pesquisarDadosDoArtista();
                    break;
                case 9:
                    System.out.println("\n**** SAINDO DO MENU ****");
                    pausaTela(2);
                    break;
                default:
                    System.out.println("\nOpção inválida...");
                    pausaTela(1);
            }

        }
    }

    private void pausaTela(int qtdPausa) {
        System.out.println("\nPressione qualquer tecla para continuar...");
        var sVar2 = leitura.nextLine();
        if (qtdPausa == 2)  sVar2 = leitura.nextLine();
    }

    //1- Cadastrar artistas
    private void cadastrarArtista() {
        System.out.println("\nInforma o nome do artista:");
        nomeArtista = leitura.nextLine();
        nomeArtista = nomeArtista.toUpperCase();

        List<TipoArtista> listaArtistas = Arrays.asList(TipoArtista.values());
        listaArtistas.forEach(System.out::println);

        System.out.println("\nInforma o tipo do artista:");
        var sTipo = leitura.nextLine();
        String tipo = String.valueOf(sTipo);

        pausaTela(0);

        TipoArtista tipoArtista = TipoArtista.valueOf(tipo.toUpperCase());

        Optional<Artista> artistaConsultado = artistaRepository.findByNomeContainingIgnoreCase(nomeArtista);
        if (artistaConsultado.isPresent()) {
            var nomeArtistaPesquisado = artistaConsultado.get().getNome();
            System.out.println("\nArtista : " + nomeArtistaPesquisado + " já cadastrado." );
            pausaTela(1);
            return;
        }
        Artista artista = new Artista( nomeArtista, tipoArtista);
        artistaRepository.saveAll(List.of(artista));
        //artistaRepository.save(artista);

        System.out.println("\nArtista(s) cadastrado(s):");
        artistaRepository.findAll().forEach(a ->
                    System.out.println("Artista: " + a.getNome() + " Tipo: " + a.getTipo())
                );
    }

    //2- Cadastrar músicas
    private void cadastrarMsicas() {
        System.out.println("Informa o nome da musica:");
        var nomeMusica = leitura.nextLine();

        artistaRepository.findAll().forEach(a ->
                System.out.println("Artista: " + a.getNome() + " Tipo: " + a.getTipo())
        );

        Boolean achouArtista = false;
        while (!achouArtista) {
            System.out.println("\nInforma o nome do artista:");
            nomeArtista = leitura.nextLine();
            Optional<Artista> artistaInformado = artistaRepository.findByNomeContainingIgnoreCase(nomeArtista);
            if (artistaInformado.isPresent()) {
                achouArtista = true;
                Artista artista = new Artista();
                artista.setId(artistaInformado.get().getId());
                Musica musica = new Musica();
                musica.setTitulo(nomeMusica);
                musica.setArtista(artista);
                musicaRepository.save(musica);
            } else {
                System.out.println("\nArtista não cadastrado!");
            }
        }
        pausaTela(1);
    }

    //3- Listar músicas
    private void listarMusicas() {
        System.out.println("\nLista das musicas e artistas:");
        List<Musica> musicas = musicaRepository.findAll();
        musicas.forEach(m ->
                        System.out.println("Musica: " + m.getTitulo() + " Artista: " + m.getArtista().getNome())
                );
        pausaTela(1);
    }

    //4- Buscar músicas por artistas
    private void musicasPorArtistas() {
        System.out.println("\nInforme o nome do artista");
        var nomeArtista = leitura.nextLine();
        List<Musica> musicas = artistaRepository.buscaMusicasPorArtista(nomeArtista);
        musicas.forEach(System.out::println);
        pausaTela(1);
    }

    //5- Pesquisar dados sobre um artista
    private void pesquisarDadosDoArtista() {
        System.out.println("Pesquisar dados sobre qual artista? ");
        var nome = leitura.nextLine();
        var resposta = ConsultaChatGPT.obterInformacao(nome);
        System.out.println(resposta.trim());
    }

}
