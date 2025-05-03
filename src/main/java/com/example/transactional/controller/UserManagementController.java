package com.example.transactional.controller;

import com.example.transactional.model.Usuario;
import com.example.transactional.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class UserManagementController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/manage-users")
    @PreAuthorize("hasRole('ADMIN')")
    public String manageUsers() {
        return "admin/users";
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public List<Usuario> getAllUsers() {
        return usuarioRepository.findAll();
    }

    @PostMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public Usuario createUser(@RequestBody Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @DeleteMapping("/users/{id}")
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

    @GetMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public ResponseEntity<Usuario> getUserById(@PathVariable Integer id) {
        return usuarioRepository.findById(id)
            .map(usuario -> ResponseEntity.ok(usuario))
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/users/{id}")
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

    @GetMapping("/roles")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public ResponseEntity<String[]> getRoles() {
        String[] roles = {"ADMIN", "OPERADOR", "CLIENTE", "AUDITOR"};
        return ResponseEntity.ok(roles);
    }
}
