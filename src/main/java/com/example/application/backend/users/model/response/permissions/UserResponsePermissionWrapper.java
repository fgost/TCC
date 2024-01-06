package com.example.application.backend.users.model.response.permissions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserResponsePermissionWrapper {
    private boolean success;

    private UserResponsePermissionData data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public UserResponsePermissionData getData() {
        return data;
    }

    public void setData(UserResponsePermissionData data) {
        this.data = data;
    }
}
