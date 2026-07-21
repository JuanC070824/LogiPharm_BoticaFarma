package com.Farmacia.BoticaFarma.repository;

import com.Farmacia.BoticaFarma.model.Botica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoticaRepository extends JpaRepository<Botica, Integer> {
}