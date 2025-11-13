package br.com.alura.screensound.principal;

import br.com.alura.screensound.model.Artista;
import br.com.alura.screensound.repository.ArtistaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class Principal {
    @Autowired
    private ArtistaRepository artistaRepository;

    public ArtistaRepository getArtistaRepository() {
        return artistaRepository;
    }
    public void setArtistaRepository(ArtistaRepository artistaRepository) {
        this.artistaRepository = artistaRepository;
    }

    private Scanner leitura = new Scanner(System.in);

    public Principal() {}

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
        var nomeArtista = leitura.nextLine();
        Optional<Artista> artistaConsultado = artistaRepository.findByNomeContainingIgnoreCase(nomeArtista);
        if (artistaConsultado.isPresent()) {
            var nomeArtistaPesquisado = artistaConsultado.get().getNome();
            System.out.println("\nArtista : " + nomeArtistaPesquisado + " já cadastrado." );
            pausaTela(1);
            return;
        }

        Artista artista = new Artista(nomeArtista);
        artistaRepository.saveAll(List.of(artista));

        System.out.println("\nArtista(s) cadastrado(s):");
        artistaRepository.findAll().forEach(a ->
                        System.out.println("Artista: " + a.getNome())
                );
    }
}
