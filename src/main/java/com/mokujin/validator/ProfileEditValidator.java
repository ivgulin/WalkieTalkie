package com.mokujin.validator;

import com.mokujin.domain.Profile;
import com.mokujin.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ProfileEditValidator implements Validator {
    @Autowired
    private ProfileService service;

    @Override
    public boolean supports(Class<?> aClass) {
        return Profile.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Profile profile = (Profile) o;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Profile currentProfile = service.findByUsername(authentication.getName());


        if (profile.getUsername().length() > 0) {
            if ((profile.getUsername().length() < 4 || profile.getUsername().length() > 32) && errors.getFieldError("username") == null) {
                errors.rejectValue("username", "Size", "Username is too short.");
            }
            if (profile.getUsername().contains(" ")) {
                errors.rejectValue("username", "Spaces", "Unacceptable symbol detected(space).");
            }
            if (service.findByUsername(profile.getUsername()) != null && !profile.getUsername().equals(currentProfile.getUsername())) {
                errors.rejectValue("username", "Duplicate", "Someone already has that username.");
            }
        }
        if (profile.getFirstName().length() > 0) {
            if (profile.getFirstName().contains(" ")) {
                errors.rejectValue("firstName", "Spaces", "Unacceptable symbol detected(space).");
            }
        }

        if (profile.getLastName().length() > 0) {

            if (profile.getLastName().contains(" ")) {
                errors.rejectValue("lastName", "Spaces", "Unacceptable symbol detected(space).");
            }

        }


        if (profile.getEmail().length() > 0) {
            if (service.findByEmail(profile.getEmail()) != null && !profile.getEmail().equals(currentProfile.getEmail())) {
                errors.rejectValue("email", "Duplicate", "An account with this email already exists");
            }
            //will be commented until end of development
      /*  if (!isAddressValid(profile.getEmail())) {
            errors.rejectValue("email", "Existence", "This mail doesn't exists.");
        }*/

        }
       /* if (profile.getPassword().equals(profile.getConfirmedPassword())) {
            if ((profile.getPassword().length() < 8 || profile.getPassword().length() > 32) && errors.getFieldError("password") == null) {
                errors.rejectValue("password", "Size", "Password is too short.");
            }
            if (!profile.getConfirmedPassword().equals(profile.getPassword())) {
                errors.rejectValue("confirmedPassword", "Difference", "Passwords didn't match.");
            }
            if (profile.getPassword().contains(" ")) {
                errors.rejectValue("password", "Spaces", "Unacceptable symbol detected(space).");
            }

        }*/

    }
}
