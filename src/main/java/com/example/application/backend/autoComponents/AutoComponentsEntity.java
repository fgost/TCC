package com.example.application.backend.autoComponents;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "auto_components")
public class AutoComponentsEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String code;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String componentName;

    @PrePersist
    private void setCode() {
        this.code = UUID.randomUUID().toString();
    }

}
