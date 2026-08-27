package com.sirket.basvuru.repository;

import com.sirket.basvuru.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}