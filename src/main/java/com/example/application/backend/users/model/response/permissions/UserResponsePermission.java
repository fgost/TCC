package com.example.application.backend.users.model.response.permissions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponsePermission {
    @JsonProperty(value = "key")
    private String permissionKey;

    @JsonProperty(value = "values")
    private List<String> permissionValues;

    public void setPermissionKey(String permissionKey) {
        this.permissionKey = permissionKey;
    }

    public void setPermissionValues(List<String> permissionValues) {
        this.permissionValues = permissionValues;
    }
}
