package com.api.Coau.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 
 * 
 * @author Warley
 */
@Entity
@Table(name = "tbusuarios")
@Data @NoArgsConstructor @AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iduser")
    private Long iduser;

    
    @NotBlank(message = "Nome de usuário é obrigatório")
    @Column(name = "usuario", nullable = false, unique = true, length = 50)
    private String usuario;

    @NotBlank(message = "Login é obrigatório")
    @Column(name = "login", nullable = false, unique = true, length = 100)
    private String login;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
    @Column(name = "senha", nullable = false, length = 255)
    private String senha;

    @NotNull(message = "Perfil é obrigatório")
    @Column(name = "perfil", nullable = false, length = 20)
    private String perfil;  // "ADMIN" ou "USER"
}
