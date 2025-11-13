package com.Farmacia.BoticaFarma.repository;

import com.Farmacia.BoticaFarma.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {

    List<Proveedor> findByNombreProveedorContainingIgnoreCase(String nombre);

    Optional<Proveedor> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdProveedorNot(String email, Integer idProveedor);
}