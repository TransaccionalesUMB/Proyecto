package com.example.transactional.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.springframework.security.core.userdetails.User;
import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private DataSource dataSource;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT email, password_hash FROM Usuario WHERE email = ?"
            );
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return User.builder()
                    .username(rs.getString("email"))
                    .password(rs.getString("password_hash"))
                    .authorities(new ArrayList<>())
                    .build();
            }

            throw new UsernameNotFoundException("Usuario no encontrado: " + email);
        } catch (Exception e) {
            throw new UsernameNotFoundException("Error al buscar usuario: " + e.getMessage());
        }
    }
}
