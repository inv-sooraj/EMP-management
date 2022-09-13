
package com.innovaturelabs.training.employee.management.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class JobForm {

    @NotBlank
    @Size(max = 50)
    private String title;

    @Size(max = 50)
    private String description;

    private Byte qualification;

    private Integer openings;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Byte getQualification() {
        return qualification;
    }

    public Integer getOpenings() {
        return openings;
    }

}
