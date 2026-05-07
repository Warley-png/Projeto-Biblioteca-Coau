/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.Coau;

import com.api.Coau.model.Usuario;
import com.api.Coau.model.usuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author Warley
 */
@Configuration
public class DataInitializer {
     @Bean
    CommandLineRunner initDatabase(usuarioRepository repository, PasswordEncoder encoder) {
        return args -> {
            if (repository.findByLogin("admin").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setUsuario("Administrador");
                admin.setLogin("admin");
                admin.setPerfil("ADMIN");
                admin.setSenha(encoder.encode("admin123")); // Senha será admin123
                repository.save(admin);
                System.out.println(">>> USUARIO INICIAL CRIADO: admin / admin123");
            }
        };
    }
}
