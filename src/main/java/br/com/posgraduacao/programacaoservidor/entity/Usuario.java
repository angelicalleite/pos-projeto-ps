package br.com.posgraduacao.programacaoservidor.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String username;

    private String password;

}
