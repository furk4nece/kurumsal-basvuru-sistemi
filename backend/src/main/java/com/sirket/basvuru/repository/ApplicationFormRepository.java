package com.sirket.basvuru.repository;

import com.sirket.basvuru.entity.ApplicationForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ApplicationFormRepository extends JpaRepository<ApplicationForm, Long>,
        JpaSpecificationExecutor<ApplicationForm> {
}