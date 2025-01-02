package com.example.application.backend.autoModel.domain;

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
@Table(name = "auto_model")
public class AutoModelEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String code;
    private String autoModel;
    private long autoMaker;

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

    public void setAutoModel(String autoModel) {
        this.autoModel = autoModel;
    }

    public long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getAutoModel() {
        return autoModel;
    }

    public long getAutoMaker() {
        return autoMaker;
    }
}
