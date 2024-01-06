package com.example.application.backend.users.domain;

import com.example.application.backend.car.domain.CarEntity;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "users")
public class UserEntity implements UserDetails, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String name;
    private String lastName;
    private String email;
    private String password;
    private long lastUpdateMileage;
    private long lastAskForUpdateMileage;

    public UserEntity(Long id, String code, String name, String lastName, String email, String password,
                      long lastUpdateMileage, long lastAskForUpdateMileage, Set<Preference> preferences, List<CarEntity> cars) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.lastUpdateMileage = lastUpdateMileage;
        this.lastAskForUpdateMileage = lastAskForUpdateMileage;
        this.preferences = preferences;
        this.cars = cars;
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "users_preferences", joinColumns = @JoinColumn(name = "user_id"))
    private Set<Preference> preferences = new HashSet<>();


    @ManyToMany
    @JoinTable(
            name = "users_cars",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "cars_id")
    )
    private List<CarEntity> cars;

    public UserEntity() {

    }

    @PrePersist
    private void setCode() {
        this.code = UUID.randomUUID().toString();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Transient
    public String getNomeCompleto() {
        return this.name.trim() + " " + this.lastName.trim();
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public Map<String, String> getPreferencesAsMap() {
        Map<String, String> map = new HashMap<>();
        preferences.forEach(p -> {
            map.put(p.getPreferenceKey(), p.getPreferenceValue());
        });
        return map;
    }

    public Map<String, String> getCarAsMap(){
        Map<String, String> map = new HashMap<>();
        cars.forEach(p -> {
            map.put(p.getCarModel(),p.getYear());
        });
        return map;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Preference> getPreferences() {
        return preferences;
    }

    public void setPreferences(Set<Preference> preferences) {
        this.preferences = preferences;
    }

    public List<CarEntity> getCars() {
        return cars;
    }

    public void setCars(List<CarEntity> cars) {
        this.cars = cars;
    }

    public long getLastUpdateMileage() {
        return lastUpdateMileage;
    }

    public void setLastUpdateMileage(long lastUpdateMileage) {
        this.lastUpdateMileage = lastUpdateMileage;
    }

    public long getLastAskForUpdateMileage() {
        return lastAskForUpdateMileage;
    }

    public void setLastAskForUpdateMileage(long lastAskForUpdateMileage) {
        this.lastAskForUpdateMileage = lastAskForUpdateMileage;
    }
}
