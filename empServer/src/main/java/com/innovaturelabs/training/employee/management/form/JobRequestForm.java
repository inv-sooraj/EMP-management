
package com.innovaturelabs.training.employee.management.form;

import javax.validation.constraints.Size;

public class JobRequestForm {

    @Size(max = 100)
    private String remark;

    private Byte status;

    public String getRemark() {
        return remark;
    }

    public Byte getStatus() {
        return status;
    }

}
