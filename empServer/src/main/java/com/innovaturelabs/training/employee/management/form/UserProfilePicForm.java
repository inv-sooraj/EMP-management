package com.innovaturelabs.training.employee.management.form;

import org.springframework.web.multipart.MultipartFile;

public class UserProfilePicForm {

    private MultipartFile profilePic;

    public MultipartFile getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(MultipartFile profilePic) {
        this.profilePic = profilePic;
    }

}
