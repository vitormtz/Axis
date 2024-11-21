package org.gvs.axis.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.gvs.axis.dto.UsuarioDTO;
import org.gvs.axis.model.Usuario;
import org.gvs.axis.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/teste")
    public String teste(Model model) {
        return "usuario/teste";
    }

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

    @GetMapping("/check-auth")
    public String checkAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Autenticado: " + auth.isAuthenticated());
        System.out.println("Principal: " + auth.getPrincipal());
        System.out.println("Authorities: " + auth.getAuthorities());
        return "redirect:/";
    }

    @GetMapping("/login")
    public String mostrarPaginaLogin() {
        return "/usuario/login";
    }

    @PostMapping("/login")
    public String fazerLogin(String email, String senha, HttpSession session) {
        try {
            UsernamePasswordAuthenticationToken token
                    = new UsernamePasswordAuthenticationToken(email, senha);

            Authentication authentication = null;
            authentication = authenticationManager.authenticate(token);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            Usuario usuario = usuarioService.autenticar(email, senha);
            session.setAttribute("usuarioLogado", usuario);

            return "redirect:/";
        } catch (Exception e) {
            return "redirect:/usuario/login?error=true";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = (Authentication) SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, (org.springframework.security.core.Authentication) auth);
        }
        return "redirect:/usuario/login?logout";
    }
}
