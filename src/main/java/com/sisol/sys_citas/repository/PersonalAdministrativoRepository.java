package com.sisol.sys_citas.repository;

import com.sisol.sys_citas.model.PersonalAdministrativo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalAdministrativoRepository extends JpaRepository<PersonalAdministrativo, Long> {
    // Métodos CRUD automáticos
}