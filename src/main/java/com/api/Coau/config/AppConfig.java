package com.api.Coau.config;  // Ensure this is correct (uppercase "Coau")

import com.api.Coau.model.Usuario;
import com.api.Coau.model.usuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;  // Novo import: Substitua o antigo
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity  // Habilita configuração de segurança web
@EnableMethodSecurity(prePostEnabled = true)  // Alterado: Use EnableMethodSecurity (não EnableGlobalMethodSecurity)
public class AppConfig {

    private final usuarioRepository usuarioRepository;

    public AppConfig(usuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response,
                    org.springframework.security.access.AccessDeniedException accessDeniedException)
                    throws IOException, jakarta.servlet.ServletException {
                // Redireciona para tela principal com mensagem de erro
                response.sendRedirect("/livros/telaprincipal?accessDenied=true");
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(authz -> authz
                // Público
                .requestMatchers("/livros/telaLogin", "/livros/cadastroUsuario", "/livros/salvarUsuario").permitAll()
                .requestMatchers("/js/**", "/css/**", "/images/**", "/imagens/**", "/estilo.css").permitAll() // Adicione /imagens/**

                // USER e ADMIN: Tela principal, cadastro, listas e edição de livros
                .requestMatchers("/livros/telaprincipal", "/livros/cadastro*").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/livros/lista*", "/livros/listaLivro*", "/livros/disponiveis", "/livros/editarLivro*").hasAnyRole("ADMIN", "USER")
                // ADMIN: Exclusão e tudo o resto
                .requestMatchers("/livros/excluirLivro*").hasRole("ADMIN")
                .anyRequest().hasRole("ADMIN")
                )
                .exceptionHandling(ex -> ex
                .accessDeniedHandler(accessDeniedHandler()) // Use o handler customizado
                )
                .formLogin(form -> form
                .loginPage("/livros/telaLogin")
                .loginProcessingUrl("/livros/telaLogin")
                .defaultSuccessUrl("/livros/telaprincipal", true)
                .failureUrl("/livros/telaLogin?error=true")
                .permitAll()
                )
                .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/livros/telaLogin")
                .permitAll()
                )
                .csrf(csrf -> csrf.disable());  // Desabilite em desenvolvimento; habilite em produção

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Usuario usuario = usuarioRepository.findByLogin(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
            String perfil = usuario.getPerfil().toUpperCase();  // Já está, mas confirme

            if (!"USER".equals(perfil) && !"ADMIN".equals(perfil)) {
                throw new UsernameNotFoundException("Perfil inválido: " + perfil);
            }

            return User.builder()
                    .username(usuario.getLogin())
                    .password(usuario.getSenha())
                    .roles(perfil.toUpperCase()) // O Spring vai colocar o ROLE_ sozinho aqui
                    .build();

        };
    }
}
