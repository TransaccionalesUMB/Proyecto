package com.example.transactional.controller;

import com.example.transactional.model.Usuario;
import com.example.transactional.model.Role;
import com.example.transactional.repository.UsuarioRepository;
import com.example.transactional.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String showProfile(Model model, Authentication authentication) {
        String email = authentication.getName();
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        
        if (!usuarioOpt.isPresent()) {
            return "redirect:/login";
        }
        
        Usuario usuario = usuarioOpt.get();
        
        // Obtener el rol del usuario
        Role role = roleRepository.findById(usuario.getIdRol()).orElse(null);
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("role", role);
        
        // Añadir atributos para la navegación
        boolean isAdmin = authentication.getAuthorities()
            .stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isOperador = authentication.getAuthorities()
            .stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_OPERADOR"));
        boolean isAuditor = authentication.getAuthorities()
            .stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_AUDITOR"));
        boolean isCliente = authentication.getAuthorities()
            .stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_CLIENTE"));
        
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isOperador", isOperador);
        model.addAttribute("isAuditor", isAuditor);
        model.addAttribute("isCliente", isCliente);
        model.addAttribute("canViewInventory", isAdmin || isOperador);
        model.addAttribute("canViewReports", isAdmin || isOperador || isAuditor);
        model.addAttribute("canViewUsers", isAdmin);
        
        return "profile/edit";
    }

    @PostMapping("/update")
    public String updateProfile(@ModelAttribute Usuario updatedUsuario, RedirectAttributes redirectAttributes, Authentication authentication) {
        String email = authentication.getName();
        Optional<Usuario> currentUsuarioOpt = usuarioRepository.findByEmail(email);
        
        if (!currentUsuarioOpt.isPresent()) {
            return "redirect:/login";
        }
        
        Usuario currentUsuario = currentUsuarioOpt.get();
        
        try {
            // Actualizar solo los campos permitidos
            currentUsuario.setEmail(updatedUsuario.getEmail());
            currentUsuario.setNombre(updatedUsuario.getNombre());
            
            // Si se proporciona una nueva contraseña, actualizarla
            if (updatedUsuario.getPassword() != null && !updatedUsuario.getPassword().isEmpty()) {
                currentUsuario.setPassword(passwordEncoder.encode(updatedUsuario.getPassword()));
            }
            
            usuarioRepository.save(currentUsuario);
            redirectAttributes.addFlashAttribute("success", "Perfil actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el perfil: " + e.getMessage());
        }
        
        return "redirect:/profile";
    }
}
