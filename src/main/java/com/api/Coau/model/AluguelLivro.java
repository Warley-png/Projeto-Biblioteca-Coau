     package com.api.Coau.model;

     import jakarta.persistence.*;
     import lombok.Data;
     import lombok.NoArgsConstructor;
     import lombok.AllArgsConstructor;
     import java.time.LocalDate;

     @Entity
     @Table(name = "tbretiradalivro") 
     @Data @NoArgsConstructor @AllArgsConstructor
     public class AluguelLivro {
         @Id
         @GeneratedValue(strategy = GenerationType.IDENTITY)
         private Long idAluguel;

         @ManyToOne
         @JoinColumn(name = "idliv")  
         private Livro livro;

         @ManyToOne
         @JoinColumn(name = "idcli")  
         private Cliente cliente;

         @Column(name = "data_emprestimo")
         private LocalDate dataEmprestimo;

         @Column(name = "data_devolucao")
         private LocalDate dataDevolucao;

         @Column(name = "status")  // 
         private String status = "EMPRESTADO";

        
     }
     