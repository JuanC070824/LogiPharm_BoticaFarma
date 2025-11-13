package com.Farmacia.BoticaFarma.repository;

import com.Farmacia.BoticaFarma.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
}