package com.project.EmployeeManagement.form;

import org.springframework.web.multipart.MultipartFile;

public class userProfilePictureForm {
    private MultipartFile image;


    
    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }


    
}
