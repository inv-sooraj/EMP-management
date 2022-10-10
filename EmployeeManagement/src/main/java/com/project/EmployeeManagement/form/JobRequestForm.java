package com.project.EmployeeManagement.form;

import javax.validation.constraints.Size;

public class JobRequestForm {

    @Size(max = 225)
    private String remarks;
    private byte status;

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }
    

    

    
}
