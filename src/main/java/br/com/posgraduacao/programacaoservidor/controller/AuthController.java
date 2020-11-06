package br.com.posgraduacao.programacaoservidor.controller;

import br.com.posgraduacao.programacaoservidor.auth.AuthToken;
import br.com.posgraduacao.programacaoservidor.dto.TokenDTO;
import br.com.posgraduacao.programacaoservidor.dto.UserDTO;
import br.com.posgraduacao.programacaoservidor.exception.RestResponse;
import br.com.posgraduacao.programacaoservidor.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class AuthController implements RestResponse {

    @Autowired
    private AuthToken authToken;
    @Autowired
    private UsuarioService userDetailsServiceJWT;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody UserDTO userDTO) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword()));

            final String token =
                    authToken.generateToken(userDetailsServiceJWT.loadUserByUsername(userDTO.getUsername()));

            return ok(new TokenDTO(token));
        } catch (DisabledException e) {
            return noContent("Usuário desabilitado: " + e);
        } catch (BadCredentialsException e) {
            return badRequest("Credenciais inválidas: " + e);
        }
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> saveUser(@RequestBody UserDTO user) {
        try {
            this.userDetailsServiceJWT.save(user);
            return ok("Usuário " + user.getUsername() + " cadastrado com sucesso");
        } catch (Exception e) {
            return internalServerError("Erro ao tentar cadastrar novo usuário: " + e);
        }
    }
}