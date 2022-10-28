
package com.innovaturelabs.training.employee.management.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class OAuth2UserForm {

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
