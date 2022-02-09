package br.com.alura.forum.controller;

import br.com.alura.forum.config.security.TokenService;
import br.com.alura.forum.controller.dto.TokenDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

    // No Bean SecurityCOnfigurantions, subimos o AutenticacaoService
    // Na qual faço override no metodo configure, pra ensinar a acessar repo Usuario por Email, retornando obj Usuario

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<TokenDto> autenticar(@RequestBody @Valid LoginForm form) {
        // Recebe objeto LoginForm no body
        // Converte objeto (email, senha) em objeto UsernamePasswordAuthenticationToken
        UsernamePasswordAuthenticationToken dadosLogin = form.converter();

        try {
            // Cria objeto Authentication com email, senha e dados do objeto usuario configurado no bean
            Authentication authentication = authenticationManager.authenticate(dadosLogin);

            // Gera o token partindo principal do obj Authentication (usuario, email, senha) (environment ja em hs256)
            String token = tokenService.gerarToken(authentication);

//            System.out.println(token);

            return ResponseEntity.ok(new TokenDto(token, "Bearer"));
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().build();
        }

//        System.out.println(form.getEmail());
//        System.out.println(form.getSenha());

    }
}
