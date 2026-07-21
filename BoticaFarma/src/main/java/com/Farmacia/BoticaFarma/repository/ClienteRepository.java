package com.Farmacia.BoticaFarma.repository;

import com.Farmacia.BoticaFarma.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    // ============ AÑADIR: para buscar/crear el cliente genérico ============
    Optional<Cliente> findByDNI(int dni);
    // ============ FIN ============
}