package com.olalekan.CoolBank.event;

import com.olalekan.CoolBank.model.AppUser;

public record EmailVerificationEvent(
        AppUser user,
        String token
) {
}
