// Repository --> consultas no banco de dados 

package com.generation.blogpessoal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.generation.blogpessoal.model.Postagem;
@Repository

// Para conseguir acessar a tabela Postagem
//SELECT * FROM tb_temass WHERE descricao LIKE "%descricao%";
public interface PostagemRepository extends JpaRepository<Postagem, Long>{
	
	public List <Postagem> findAllByTituloContainingIgnoreCase(@Param("titulo") String titulo);

}

