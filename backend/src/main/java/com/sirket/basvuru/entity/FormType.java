package com.sirket.basvuru.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "form_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;
}