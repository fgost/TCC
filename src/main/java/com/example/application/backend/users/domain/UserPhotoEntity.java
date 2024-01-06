package com.example.application.backend.users.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_photo")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserPhotoEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    @Id
    @Column(name = "user_id")
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private UserEntity user;
    private String fileName;
    private Long size;
    private String url;
    private String contentType;

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
