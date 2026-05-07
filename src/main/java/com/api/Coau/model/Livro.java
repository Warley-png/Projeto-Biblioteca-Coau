  package com.api.Coau.model;

  import jakarta.persistence.*;
  import jakarta.validation.constraints.NotBlank;
  import jakarta.validation.constraints.NotNull;
  import lombok.Data;
  import lombok.NoArgsConstructor;
  import lombok.AllArgsConstructor;

  @Entity
  @Table(name = "tblivros")
  @Data  // Lombok: Gera getters/setters/toString automaticamente
  @NoArgsConstructor
  @AllArgsConstructor
  public class Livro {

      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      @Column(name = "idliv")
      private Long idliv;

      @NotBlank(message = "Título é obrigatório")
      @Column(name = "nomeliv", nullable = false)
      private String nomeLivro;

      @NotBlank(message = "Autor é obrigatório")
      @Column(name = "autoliv", nullable = false)  
      private String autorLivro;

      @Column(name = "assuntoliv")
      private String assuntoLivro;

      @NotBlank(message = "Editora é obrigatória")
      @Column(name = "editoraliv", nullable = false)
      private String editoraLivro;

      @NotNull(message = "Ano é obrigatório")  
      @Column(name = "ano")
      private Integer ano;  
      
      @Column(name = "volume")
      private String volume;

      @NotBlank(message = "Tipo é obrigatório")
      @Column(name = "tipo", nullable = false)
      private String tipo;

      @NotBlank(message = "Prateleira é obrigatória")
      @Column(name = "prateleiraliv", nullable = false)
      private String prateleiraLivro;

     
      @Column(name = "disponivelliv", nullable = false)  
      private boolean disponivel = true;  

      
  }
  