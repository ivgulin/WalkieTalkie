package com.mokujin.validator;


import com.mokujin.domain.Profile;
import com.mokujin.service.ProfileService;
import com.mokujin.util.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import static com.mokujin.util.MailUtil.isAddressValid;

@Component
public class ProfileValidator implements Validator {
    @Autowired
    private ProfileService service;

    @Override
    public boolean supports(Class<?> aClass) {
        return Profile.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Profile profile = (Profile) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty", "This field is required");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "NotEmpty", "This field is required");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "NotEmpty", "This field is required");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty", "This field is required");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty", "This field is required");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmedPassword", "NotEmpty", "This field is required");


        if ((profile.getUsername().length() < 4 || profile.getUsername().length() > 32) && errors.getFieldError("username") == null) {
            errors.rejectValue("username", "Size", "Username is too short.");
        }

        if (service.findByUsername(profile.getUsername()) != null) {
            errors.rejectValue("username", "Duplicate", "Someone already has that username.");
        }

        if (service.findByEmail(profile.getEmail()) != null) {
            errors.rejectValue("email", "Duplicate", "An account with this email already exists");
        }

        if ((profile.getPassword().length() < 8 || profile.getPassword().length() > 32) && errors.getFieldError("password") == null) {
            errors.rejectValue("password", "Size", "Password is too short.");
        }

        if (!profile.getConfirmedPassword().equals(profile.getPassword())) {
            errors.rejectValue("confirmedPassword", "Difference", "Passwords didn't match.");
        }

        if (profile.getUsername().contains(" ")) {
            errors.rejectValue("username", "Spaces", "Unacceptable symbol detected(space).");
        }

        if (profile.getFirstName().contains(" ")) {
            errors.rejectValue("firstName", "Spaces", "Unacceptable symbol detected(space).");
        }

        if (profile.getLastName().contains(" ")) {
            errors.rejectValue("lastName", "Spaces", "Unacceptable symbol detected(space).");
        }

        if (profile.getPassword().contains(" ")) {
            errors.rejectValue("password", "Spaces", "Unacceptable symbol detected(space).");
        }

        //will be commented until end of development
      /*  if (!isAddressValid(profile.getEmail())) {
            errors.rejectValue("email", "Existence", "This mail doesn't exists.");
        }*/

    }

}
