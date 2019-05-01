package ir.markazandroid.uinitializr.object;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.io.Serializable;

import ir.markazandroid.uinitializr.BR;
import ir.markazandroid.uinitializr.network.JSONParser.annotations.JSON;
import ir.markazandroid.uinitializr.network.formdata.Form;

/**
 * Coded by Ali on 01/02/2018.
 */

public class User extends BaseObservable implements Serializable {
    private int userId;
    @Form
    private String username;
    @Form
    private String password;
    @Form(name = "fcmToken")
    private String token;

    private Role role;
    private String name;


    @JSON
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @JSON
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @JSON(classType = JSON.CLASS_TYPE_OBJECT, clazz = Role.class)
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Bindable
    @JSON
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public static class Role implements Serializable {
        private String name;
        private String description;

        @JSON
        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @JSON
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
