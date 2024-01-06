package com.example.application.backend.users.model.response.permissions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

import java.util.List;


@AllArgsConstructor
public class UserResponsePermissionData {
    @JsonProperty(value = "itens")
    private List<UserResponsePermission> items;

    public List<UserResponsePermission> getItems() {
        return items;
    }

    public void setItems(List<UserResponsePermission> items) {
        this.items = items;
    }
}
