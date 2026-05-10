
package com.api.Coau.model;

import com.api.Coau.model.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Warley
 */
public interface usuarioRepository extends JpaRepository<Usuario, Long> {
    
    Optional<Usuario> findByLogin(String login);  // Método para buscar por login (único)
}
