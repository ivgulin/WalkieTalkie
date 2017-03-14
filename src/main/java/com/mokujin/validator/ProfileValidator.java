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

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "Empty");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "Empty");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "Empty");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "Empty");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "Empty");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmedPassword", "Empty");


        if (!profile.getEmail().contains("@") || !profile.getEmail().contains(".")){
            errors.rejectValue("email","Mistake.mail");
        }


        if (profile.getUsername().length() < 6 || profile.getUsername().length() > 32) {
            errors.rejectValue("username", "Length.username");
        }

        if (service.findByUsername(profile.getUsername()) != null) {
            errors.rejectValue("username", "Duplicate.username");
        }

        if (service.findByEmail(profile.getEmail()) != null) {
            errors.rejectValue("username", "Duplicate.email");
        }

        if (profile.getPassword().length() < 8 || profile.getPassword().length() > 32) {
            errors.rejectValue("password", "Length.password");
        }

        if (!profile.getConfirmedPassword().equals(profile.getPassword())) {
            errors.rejectValue("passwordConfirm", "Mistake.confirmedPassword");
        }
    }
}
