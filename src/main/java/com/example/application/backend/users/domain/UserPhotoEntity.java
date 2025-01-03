package com.example.application.backend.users.domain;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
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
}
