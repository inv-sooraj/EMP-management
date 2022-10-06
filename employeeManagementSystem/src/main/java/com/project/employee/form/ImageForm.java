package com.project.employee.form;

import org.springframework.web.multipart.MultipartFile;

public class ImageForm {
	public MultipartFile profilePic;

    public MultipartFile getProfilePhoto() {
        return profilePic;
    }

    public void setProfilePhoto(MultipartFile profilePhoto) {
        this.profilePic = profilePhoto;
    }
}
