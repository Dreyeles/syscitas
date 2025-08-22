package com.sisol.sys_citas.repository;

import com.sisol.sys_citas.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    // Métodos CRUD automáticos
} 