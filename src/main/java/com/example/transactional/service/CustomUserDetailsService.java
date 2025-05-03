package com.example.transactional.service;

import com.example.transactional.model.Usuario;
import com.example.transactional.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    // Mapeo de id_rol a nombre de rol
    private static final Map<Integer, String> ROLES_MAP = new HashMap<>();
    static {
        ROLES_MAP.put(1, "ADMIN");
        ROLES_MAP.put(2, "OPERADOR");
        ROLES_MAP.put(3, "CLIENTE");
        ROLES_MAP.put(4, "AUDITOR");
    }

    @PostConstruct
    public void init() {
        // Se ha eliminado la inicialización automática del usuario administrador
        // Los usuarios deben crearse manualmente a través de la interfaz de administración
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        // Obtener el nombre del rol según el id_rol
        String rolName = ROLES_MAP.getOrDefault(usuario.getIdRol(), "USER");
        
        return new User(
                usuario.getEmail(),
                usuario.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + rolName))
        );
    }
}
