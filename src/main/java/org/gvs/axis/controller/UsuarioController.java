package org.gvs.axis.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.gvs.axis.dto.UsuarioDTO;
import org.gvs.axis.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author vitor
 */
@Controller
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    @Autowired
    private final UsuarioService usuarioService;

    @GetMapping("/cadastro")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("usuarioDTO", new UsuarioDTO());
        return "usuario/cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastrarUsuario(@Valid UsuarioDTO usuarioDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "usuario/cadastro";
        }

        try {
            usuarioService.cadastrar(usuarioDTO);
            redirectAttributes.addFlashAttribute("mensagem", "Usuário cadastrado com sucesso!");
            return "redirect:/";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao cadastrar usuário: " + e.getMessage());
            return "redirect:/usuario/cadastro";
        }
    }

    @GetMapping("/login")
    public String mostrarPaginaLogin() {
        return "/usuario/login";
    }
}
