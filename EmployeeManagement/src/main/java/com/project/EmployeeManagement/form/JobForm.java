package com.project.EmployeeManagement.form;
import javax.validation.constraints.Size;

public class JobForm {

    @Size(max = 50)
    private String jobTitle;

    @Size(max = 225)
    private String jobDescription;

    private Integer openings;

    private byte qualification;


    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public Integer getOpenings() {
        return openings;
    }

    public void setOpenings(Integer openings) {
        this.openings = openings;
    }

    public byte getQualification() {
        return qualification;
    }

    public void setQualification(byte qualification) {
        this.qualification = qualification;
    }

    
   

}
