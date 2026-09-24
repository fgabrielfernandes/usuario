package com.javanauta.usuario.business;

import com.javanauta.usuario.business.converter.UsuarioConverter;
import com.javanauta.usuario.business.dto.UsuarioDTO;
import com.javanauta.usuario.infraesctructure.entity.Usuario;
import com.javanauta.usuario.infraesctructure.exceptions.ConflictException;
import com.javanauta.usuario.infraesctructure.exceptions.ResourceNotFountException;
import com.javanauta.usuario.infraesctructure.repository.UsuarioRepository;
import com.javanauta.usuario.infraesctructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        return usuarioConverter.paraUsuarioDTO(
                usuarioRepository.save(usuario)
        );
    }

    public void emailExiste(String email) {
        try {
            boolean existe = verificaEmailExistente(email);
            if (existe) {
                throw new ConflictException("Email já existente" + email);
            }
        } catch (Exception e) {
            throw new ConflictException("Email já cadastrado" + e.getCause());
        }
    }

    public boolean verificaEmailExistente(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public Usuario buscarUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFountException("Email não encontrado" + email));
    }

    public void deletarUsuarioPorEmail(String email) {
        usuarioRepository.deleteByEmail(email);
    }

    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO dto) {
        //Aqui busca o email do usuario pelo token -- tira obrigatoriedade de passar o email --
        String email = jwtUtil.extrairEmailToken(token.substring(7));

        //Criptografia de Senha
        dto.setSenha(dto.getSenha()!=null ? passwordEncoder.encode(dto.getSenha()) : null);

        //Aqui busca os dados do usuário
        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFountException("Email não localizado. " + email));

        //Aqui mescla os dados recebidos na requisicao DTO com o banco de dados
        Usuario usuario = usuarioConverter.updateUsuario(dto, usuarioEntity);

        // Dados dos usuario salvo e convertido e depois usou o retorno convertendo para UsuarioDTO
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));


    }
}

