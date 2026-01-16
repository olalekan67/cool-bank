package com.olalekan.CoolBank.event;

import com.olalekan.CoolBank.model.AppUser;

public record RegistrationCompleteEvent(
        AppUser user,
        String token
) {
}
