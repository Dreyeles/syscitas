package com.sisol.sys_citas.repository;

import com.sisol.sys_citas.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {
    // Puedes añadir métodos personalizados aquí, ej:
    // Optional<Medico> findByCmp(String cmp);
}
