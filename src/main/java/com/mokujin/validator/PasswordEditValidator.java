package com.mokujin.validator;

import com.mokujin.domain.Profile;
import com.mokujin.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PasswordEditValidator implements Validator {

    @Autowired
    private ProfileService service;

    @Override
    public boolean supports(Class<?> aClass) {
        return Profile.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Profile profile = (Profile) o;

        if (profile.getPassword().equals(profile.getConfirmedPassword())) {
            if ((profile.getPassword().length() < 8 || profile.getPassword().length() > 32) && errors.getFieldError("password") == null) {
                errors.rejectValue("confirmedPassword", "Size", "Password is too short.");
            }
            if (!profile.getConfirmedPassword().equals(profile.getPassword())) {
                errors.rejectValue("confirmedPassword", "Difference", "Passwords didn't match.");
            }
            if (profile.getPassword().contains(" ")) {
                errors.rejectValue("confirmedPassword", "Spaces", "Unacceptable symbol detected(space).");
            }

        }
    }
}
