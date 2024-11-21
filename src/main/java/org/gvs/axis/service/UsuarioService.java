package org.gvs.axis.service;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.gvs.axis.dto.UsuarioDTO;
import org.gvs.axis.model.Usuario;
import org.gvs.axis.repository.UsuarioRepository;
import org.gvs.axis.util.CPFValidator;
import org.gvs.axis.util.EmailValidator;
import org.gvs.axis.util.NomeValidator;
import org.gvs.axis.util.PasswordEncryption;
import org.gvs.axis.util.TelefoneValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author vitor
 */
@Service
@RequiredArgsConstructor
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CPFValidator cpfValidator;

    @Autowired
    private EmailValidator emailValidator;

    @Transactional
    public Usuario cadastrar(UsuarioDTO dto) {

        NomeValidator.validar(dto.getNome());
        emailValidator.validar(dto.getEmail());
        cpfValidator.validar(dto.getCpf());
        TelefoneValidator.validar(dto.getTelefone());

        Usuario usuario = Usuario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail().toLowerCase())
                .senha(PasswordEncryption.criptografarSenha(dto.getSenha()))
                .cpf(cpfValidator.limpar(dto.getCpf()))
                .telefone(TelefoneValidator.limpar(dto.getTelefone()))
                .ativo(true)
                .dataCriacao(LocalDateTime.now())
                .dataAtualizacao(LocalDateTime.now())
                .build();

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario autenticar(String email, String senha) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("E-mail não encontrado"));
        if (!PasswordEncryption.verificarSenha(senha, usuario.getSenha())) {
            throw new RuntimeException("Senha inválida");
        }
        return usuario;
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios;
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorCpf(String cpf) {
        return usuarioRepository.findByCpf(cpf);
    }

    @Transactional
    public Usuario atualizar(Long id, UsuarioDTO dto) {
        Usuario usuario = buscarPorId(id);

        usuarioRepository.findByEmail(dto.getEmail())
                .ifPresent(u -> {
                    if (!u.getId().equals(id)) {
                        throw new IllegalArgumentException("Email já está em uso");
                    }
                });

        usuarioRepository.findByCpf(dto.getCpf())
                .ifPresent(u -> {
                    if (!u.getId().equals(id)) {
                        throw new IllegalArgumentException("CPF já está em uso");
                    }
                });

        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setCpf(dto.getCpf());
        usuario.setTelefone(dto.getTelefone());
        usuario.setDataAtualizacao(LocalDateTime.now());

        if (dto.getSenha() != null && !dto.getSenha().trim().isEmpty()) {
            usuario.setSenha(dto.getSenha());
        }

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void ativarDesativar(Long id) {
        Usuario usuario = buscarPorId(id);
        usuario.setAtivo(!usuario.isAtivo());
        usuario.setDataAtualizacao(LocalDateTime.now());
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void excluir(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuário não encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean emailExiste(String email) {
        return usuarioRepository.findByEmail(email).isPresent();
    }

    @Transactional(readOnly = true)
    public boolean cpfExiste(String cpf) {
        return usuarioRepository.findByCpf(cpf).isPresent();
    }
}
