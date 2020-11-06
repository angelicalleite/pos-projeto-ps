package br.com.posgraduacao.programacaoservidor.service;

import br.com.posgraduacao.programacaoservidor.dto.UserDTO;
import br.com.posgraduacao.programacaoservidor.entity.Usuario;
import br.com.posgraduacao.programacaoservidor.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario user = usuarioRepository.findByUsername(username);

        if (user == null)
            throw new UsernameNotFoundException("Usuário não encontrado com username: " + username);

        return new User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }

    public Usuario save(UserDTO user) {
        Usuario novo = new Usuario();

        novo.setUsername(user.getUsername());
        novo.setPassword(bcryptEncoder.encode(user.getPassword()));

        return usuarioRepository.save(novo);
    }

    public Usuario getByUsername(String username) {
        return this.usuarioRepository.findByUsername(username);
    }
}
