  package com.api.Coau.model;

  import org.springframework.data.jpa.repository.JpaRepository;
  import org.springframework.stereotype.Repository;
  import java.util.List;

  @Repository
  public interface LivroRepository extends JpaRepository<Livro, Long> {
     
      List<Livro> findByDisponivelTrue();  

      
      List<Livro> findByNomeLivroContainingIgnoreCase(String titulo);

    public void save(Cliente cliente);
  }
  