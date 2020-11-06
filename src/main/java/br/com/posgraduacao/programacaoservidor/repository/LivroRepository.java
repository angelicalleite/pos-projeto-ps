package br.com.posgraduacao.programacaoservidor.repository;

import br.com.posgraduacao.programacaoservidor.entity.Livro;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LivroRepository extends CrudRepository<Livro, Long> {

    Optional<Livro> findByTitulo(String titulo);

    List<Livro> findAllByAutor(String autor);

}