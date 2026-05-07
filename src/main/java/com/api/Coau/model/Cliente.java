package com.api.Coau.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 *
 * @author Warley
 */

@Entity
@Table(name = "tblclientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcli")
    private Long idCliente;

    @NotBlank
    @Column(name = "nomecli", nullable = false)
    private String nomeCliente;

    @NotBlank
    @Column(name = "emailcli", nullable = false, unique = true)
    private String emailCliente;
    
    

    @Column(name = "telefonecli")
    private String telefoneCliente;

   
}
