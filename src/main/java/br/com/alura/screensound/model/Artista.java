package br.com.alura.screensound.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Artista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    //@Enumerated(EnumType.STRING)
    //private TipoArtista tipo;

    @OneToMany(mappedBy = "artista", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Musica> musicas;
    //private List<Musica> musicas = new ArrayList<>();

    public Artista(){}

    public Artista(String nome) {
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

//    public TipoArtista getTipo() {
//        return tipo;
//    }
//    public void setTipo(TipoArtista tipo) {
//        this.tipo = tipo;
//    }

    public List<Musica> getMusicas() {
        return musicas;
    }

    public void setMusicas(List<Musica> musicas) {
        this.musicas = musicas;
    }

    @Override
    public String toString() {
        return "Artista{ " +
                "id = " + id +
                ", nome = ' " + nome + '\'' +
                ", musicas = " + musicas +
                '}';
    }
}

//", tipo = " + tipo +