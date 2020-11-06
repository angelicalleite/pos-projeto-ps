package br.com.posgraduacao.programacaoservidor.service;

import br.com.posgraduacao.programacaoservidor.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    public LivroRepository getLivroRepository() {
        return livroRepository;
    }

}
