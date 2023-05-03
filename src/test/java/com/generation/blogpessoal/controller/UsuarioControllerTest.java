package com.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.repository.UsuarioRepository;
import com.generation.blogpessoal.service.UsuarioService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioControllerTest {

	@Autowired 
	private TestRestTemplate testRestTemplate;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@BeforeAll
	void start() {
		
		usuarioRepository.deleteAll();
		
		usuarioService.cadastrarUsuario(new Usuario(0L, "Root", "root@root.com", "rootroot", " "));
	}
	
	/* Método para criar um Usuário */
	
	@Test
	@DisplayName("Cadastrar Usuário")
	public void criarUsuario() {
		
		HttpEntity<Usuario> requisicaoUsuario = new HttpEntity<Usuario>
			(new Usuario(0L, "Beatriz", "beatriz@mail.com", "98765432", ""));
		
		ResponseEntity<Usuario> respostaUsuario = testRestTemplate
				.exchange("/usuarios/cadastrar", HttpMethod.POST, requisicaoUsuario, Usuario.class);
		
		assertEquals(HttpStatus.CREATED, respostaUsuario.getStatusCode());
	}
	
	/* Método para não haver duplicidade nos usuários */
	
	@Test
	@DisplayName("Não deve permitir duplicação do Usuário.")
	public void semDuplicidadeUsuario() {
		
		usuarioService.cadastrarUsuario(new Usuario(0L, "Whisky", "whisky@mail.com", "12345678", " - "));
		
		HttpEntity<Usuario> dupRequisicao = new HttpEntity<Usuario>(new Usuario(0L, "Whisky", "whisky@mail.com", "12345678", " - "));
		
		ResponseEntity<Usuario> dupResposta = testRestTemplate
				.exchange("/usuarios/cadastrar", HttpMethod.POST, dupRequisicao, Usuario.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, dupResposta.getStatusCode());
	}
	
	/* Método para atualizar usuário */
	
	@Test
	@DisplayName("Atualizar Usuário")
	public void atualizarUsuario() {
		
		Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(new Usuario(0L, "Paula", "paula@mail.com", "45612378", "-"));
		
		Usuario usuarioUpdate = new Usuario(usuarioCadastrado.get().getId(),
				"Paula Marcela", "paula.marcela@mail.com", "45612378", "-");
		
		HttpEntity<Usuario> atuRequisicao = new HttpEntity<Usuario>(usuarioUpdate);
		
		ResponseEntity<Usuario> atuResposta = testRestTemplate
				.withBasicAuth("root@root.com", "rootroot")
				.exchange("/usuarios/atualizar", HttpMethod.PUT, atuRequisicao, Usuario.class);
		
		assertEquals(HttpStatus.OK, atuResposta.getStatusCode());
		
	}
	
	/* Método para Lista todos os usuários */
	
	@Test
	@DisplayName("Lista todos os Usuários")
	public void ListarUsuarios() {
		
		usuarioService.cadastrarUsuario(new Usuario(0L, "Carlos", "carlos@mail.com", "95175312", "-"));
		
		usuarioService.cadastrarUsuario(new Usuario(0L, "Carol", "carol@mail.com", "85274136", "-"));
		
		ResponseEntity<String> resposta = testRestTemplate
				.withBasicAuth("root@root.com", "rootroot")
				.exchange("/usuarios/all", HttpMethod.GET, null, String.class);
		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
	}
	
}
