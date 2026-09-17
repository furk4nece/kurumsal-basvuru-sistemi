package com.sirket.basvuru.specification;

import com.sirket.basvuru.entity.ApplicationForm;
import com.sirket.basvuru.enums.ApplicationStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public final class ApplicationFormSpecification {

    private ApplicationFormSpecification() {
    }

    public static Specification<ApplicationForm> hasStatus(ApplicationStatus status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<ApplicationForm> hasFormType(Long formTypeId) {
        return (root, query, cb) ->
                formTypeId == null ? null : cb.equal(root.get("formType").get("id"), formTypeId);
    }

    public static Specification<ApplicationForm> belongsTo(String applicantEmail) {
        return (root, query, cb) ->
                applicantEmail == null ? null : cb.equal(root.get("applicant").get("email"), applicantEmail);
    }

    public static Specification<ApplicationForm> keywordContains(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) {
                return null;
            }
            String pattern = "%" + keyword.toLowerCase() + "%";
            Predicate onTitle = cb.like(cb.lower(root.get("title")), pattern);
            Predicate onDescription = cb.like(cb.lower(cb.coalesce(root.get("description"), "")), pattern);
            return cb.or(onTitle, onDescription);
        };
    }

    public static Specification<ApplicationForm> createdAfter(LocalDateTime start) {
        return (root, query, cb) ->
                start == null ? null : cb.greaterThanOrEqualTo(root.get("createdDate"), start);
    }

    public static Specification<ApplicationForm> createdBefore(LocalDateTime end) {
        return (root, query, cb) ->
                end == null ? null : cb.lessThanOrEqualTo(root.get("createdDate"), end);
    }
}