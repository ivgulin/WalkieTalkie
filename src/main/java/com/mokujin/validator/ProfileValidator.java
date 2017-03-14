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

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "NotEmpty");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "NotEmpty");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmedPassword", "NotEmpty");


        if (profile.getUsername().length() < 4 || profile.getUsername().length() > 32) {
            errors.rejectValue("username", "Size.profile.username");
        }

        if (service.findByUsername(profile.getUsername()) != null) {
            errors.rejectValue("username", "Duplicate.profile.username");
        }

        if (service.findByEmail(profile.getEmail()) != null) {
            errors.rejectValue("email", "Duplicate.profile.email");
        }

        if (profile.getPassword().length() < 8 || profile.getPassword().length() > 32) {
            errors.rejectValue("password", "Size.profile.password");
        }

        if (!profile.getConfirmedPassword().equals(profile.getPassword())) {
            errors.rejectValue("confirmedPassword", "Diff.profile.confirmedPassword");
        }
    }
}
