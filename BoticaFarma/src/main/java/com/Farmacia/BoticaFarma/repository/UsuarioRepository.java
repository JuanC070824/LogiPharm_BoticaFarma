package com.Farmacia.BoticaFarma.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.Farmacia.BoticaFarma.model.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Integer> {
    Optional<Usuario> findByUsername(String username) ;
}
