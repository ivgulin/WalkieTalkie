package com.mokujin.validator;


import com.mokujin.domain.Profile;
import com.mokujin.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ProfileValidator implements Validator{
    @Autowired
    private ProfileService service;

    @Override
    public boolean supports(Class<?> aClass) {
        return Profile.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Profile profile = (Profile) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty");

        if (!profile.getEmail().contains("@") || !profile.getEmail().contains(".")){
            errors.rejectValue("mail","not a mail");
        }

        if (profile.getEmail().length() < 6 || profile.getEmail().length() > 32) {
            errors.rejectValue("username", "Size.userForm.email");
        }

        if (profile.getFirstName().length() < 1) {
            errors.rejectValue("username", "Size.userForm.username");
        }

        if (profile.getFirstName().length() < 1) {
            errors.rejectValue("username", "Size.userForm.username");
        }

        if (profile.getUserName().length() < 6 || profile.getUserName().length() > 32) {
            errors.rejectValue("username", "Size.userForm.username");
        }
        if (service.findByUsername(profile.getUserName()) != null) {
            errors.rejectValue("username", "Duplicate.userForm.username");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");

        if (profile.getPassword().length() < 8 || profile.getPassword().length() > 32) {
            errors.rejectValue("password", "Size.userForm.password");
        }

        if (!profile.getConfirmPassword().equals(profile.getPassword())) {
            errors.rejectValue("passwordConfirm", "Diff.userForm.passwordConfirm");
        }
    }
}
