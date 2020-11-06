package br.com.posgraduacao.programacaoservidor.repository;

import br.com.posgraduacao.programacaoservidor.entity.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

    Usuario findByUsername(String username);

}