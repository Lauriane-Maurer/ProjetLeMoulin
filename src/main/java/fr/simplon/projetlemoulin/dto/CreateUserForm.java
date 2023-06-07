package fr.simplon.projetlemoulin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Represents a form for creating a new user.
 * This class is used to capture and validate the input data for creating a new user.
 */
public class CreateUserForm {

    @NotBlank
    @Size(min=1,max=255)
    private String login;
    @NotBlank
    @Size(min=1,max=255)
    private String password;
    @NotBlank
    @Size(min=1,max=255)
    private String confirmPassword;

    public CreateUserForm() {
    }

    public CreateUserForm(String login, String password, String confirmPassword) {
        this.login = login;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
