package com.example.application.backend.users.model.response.permissions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponsePermissionWrapper {
    private boolean success;

    private UserResponsePermissionData data;

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setData(UserResponsePermissionData data) {
        this.data = data;
    }
}
