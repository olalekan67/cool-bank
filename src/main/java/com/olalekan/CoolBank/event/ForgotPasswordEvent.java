package com.olalekan.CoolBank.event;

import com.olalekan.CoolBank.model.AppUser;

public record ForgotPasswordEvent(
        AppUser user,
        String token
) {
}
