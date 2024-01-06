package com.example.application.backend.autoMaker;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "auto_maker")
public class AutoMakerEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String code;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    @PrePersist
    private void setCode() {
        this.code = UUID.randomUUID().toString();
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
