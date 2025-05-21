package com.example.transactional.controller;

import com.example.transactional.model.Usuario;
import com.example.transactional.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class UserManagementController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public String manageUsers(Model model) {
        // Agregar variables de control de acceso para la barra de navegación
        model.addAttribute("isAdmin", true);
        model.addAttribute("isOperador", true);
        model.addAttribute("isAuditor", true);
        model.addAttribute("isCliente", false);
        // Agregar lista de usuarios al modelo
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "admin/users";
    }

    @GetMapping("/api/users")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public List<Usuario> getAllUsers() {
        return usuarioRepository.findAll();
    }

    @PostMapping("/api/users")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public Usuario createUser(@RequestBody Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @DeleteMapping("/api/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        return usuarioRepository.findById(id)
            .map(usuario -> {
                usuarioRepository.delete(usuario);
                return ResponseEntity.ok().build();
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/api/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public ResponseEntity<Usuario> getUserById(@PathVariable Integer id) {
        return usuarioRepository.findById(id)
            .map(usuario -> ResponseEntity.ok(usuario))
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/api/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public ResponseEntity<Usuario> updateUser(@PathVariable Integer id, @RequestBody Usuario usuarioDetails) {
        return usuarioRepository.findById(id)
            .map(usuario -> {
                usuario.setNombre(usuarioDetails.getNombre());
                usuario.setEmail(usuarioDetails.getEmail());
                // Solo actualizar la contraseña si se proporciona una nueva
                if (usuarioDetails.getPassword() != null && !usuarioDetails.getPassword().isEmpty()) {
                    usuario.setPassword(usuarioDetails.getPassword());
                }
                usuario.setIdRol(usuarioDetails.getIdRol());
                Usuario updatedUsuario = usuarioRepository.save(usuario);
                return ResponseEntity.ok(updatedUsuario);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/api/roles")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public ResponseEntity<String[]> getRoles() {
        String[] roles = {"ADMIN", "OPERADOR", "CLIENTE", "AUDITOR"};
        return ResponseEntity.ok(roles);
    }
    
    // Eliminado el método manageRoles para evitar conflicto con AdminController
}
