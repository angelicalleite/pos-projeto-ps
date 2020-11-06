package br.com.posgraduacao.programacaoservidor.controller;

import br.com.posgraduacao.programacaoservidor.entity.Livro;
import br.com.posgraduacao.programacaoservidor.exception.RestResponse;
import br.com.posgraduacao.programacaoservidor.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/livros")
public class LivroController implements RestResponse {

    @Autowired
    private LivroService livroService;

    @GetMapping
    public ResponseEntity<?> findAll() {
        try {
            List<Livro> livros = (List<Livro>) this.livroService.getLivroRepository().findAll();

            return livros.isEmpty()
                    ? notFound("Nenhum livro encontrado!")
                    : ok(livros);

        } catch (Exception e) {
            return internalServerError("Problema ao consultar livros: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
        Optional<Livro> livro = this.livroService.getLivroRepository().findById(id);

        return livro.isPresent()
                ? ok(livro.get())
                : notFound("Livro não encontrado!");
    }

    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<?> findByName(@PathVariable("titulo") String titulo) {
        try {
            Optional<Livro> livro = this.livroService.getLivroRepository().findByTitulo(titulo);

            return livro.isPresent()
                    ? ok(livro.get())
                    : notFound("Livro com o título: " + titulo + ", não encontrado!");
        } catch (Exception e) {
            return internalServerError("Problema ao consultar livro pelo título: " + e.getMessage());
        }
    }

    @GetMapping("/autor/{autor}")
    public ResponseEntity<?> findByAutor(@PathVariable("autor") String autor) {
        try {
            List<Livro> livros = this.livroService.getLivroRepository().findAllByAutor(autor);

            return livros == null || livros.isEmpty()
                    ? notFound("Livros com autor:" + autor + ", não encontrado!")
                    : ok(livros);
        } catch (Exception e) {
            return internalServerError("Problema ao consultar livro pelo autor: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createLivro(@RequestBody Livro livro) {
        try {
            if (livro.getTitulo() == null || livro.getTitulo().length() < 3)
                return created("Título do livro inválido, título deve conter mínimo de 3 caracteres");
            if (livro.getAutor() == null || livro.getAutor().length() < 3)
                return created("Nome autor inválido, nome  deve conter mínimo de 3 caracteres");

            Livro novoLivro = Livro.builder()
                    .titulo(livro.getTitulo())
                    .autor(livro.getAutor())
                    .data(LocalDate.now())
                    .build();

            this.livroService.getLivroRepository().save(novoLivro);

            return created(novoLivro);
        } catch (Exception e) {
            return internalServerError("Problema ao criar novo livro: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLivroById(@PathVariable("id") Long id, @RequestBody Livro livro) {
        try {
            Optional<Livro> atualizarLivro = this.livroService.getLivroRepository().findById(id);

            if (atualizarLivro.isPresent()) {
                atualizarLivro.get().setTitulo(livro.getTitulo());
                atualizarLivro.get().setAutor(livro.getAutor());
                atualizarLivro.get().setData(livro.getData());

                this.livroService.getLivroRepository().save(atualizarLivro.get());

                return ok(atualizarLivro);
            } else {
                return notFound("Livro não encontrado");
            }
        } catch (Exception e) {
            return internalServerError("Problema ao atualizar livro: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLivroById(@PathVariable("id") Long id) {
        try {
            this.livroService.getLivroRepository().deleteById(id);

            return noContent("Livro ID: " + id + " excluído com sucesso");
        } catch (Exception e) {
            return internalServerError("Problema ao deletar livro: " + e.getMessage());
        }
    }

}
