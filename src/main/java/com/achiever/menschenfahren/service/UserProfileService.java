package com.achiever.menschenfahren.service;

import java.util.Optional;

import javax.annotation.Nonnull;

import com.achiever.menschenfahren.base.exception.MultipleResourceFoundException;
import com.achiever.menschenfahren.base.exception.ResourceNotFoundException;
import com.achiever.menschenfahren.entities.users.User;
import com.achiever.menschenfahren.entities.users.UserProfile;

public interface UserProfileService {

    /**
     *
     * @param userProfile
     * @param alsoVoided
     * @return
     */
    UserProfile addProfile(@Nonnull final UserProfile userProfile, final boolean alsoVoided);

    Optional<UserProfile> findById(@Nonnull final String id);

    UserProfile findByUser(@Nonnull final User user) throws MultipleResourceFoundException, ResourceNotFoundException;

}
