package com.api.Coau.model;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Warley
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

      Optional<Cliente> findByEmailCliente(String email);

  
}
