package com.achiever.menschenfahren.service.impl;

import java.util.Optional;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.achiever.menschenfahren.dao.UserProfileDaoInterface;
import com.achiever.menschenfahren.entities.users.UserProfile;
import com.achiever.menschenfahren.service.UserProfileService;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    private UserProfileDaoInterface userProfileDao;

    /**
     *
     * @param userProfile
     * @param alsoVoided
     * @return
     */
    @Override
    public UserProfile addProfile(@Nonnull final UserProfile userProfile, final boolean alsoVoided) {
        return userProfileDao.save(userProfile);
    }

    @Override
    public Optional<UserProfile> findById(final String id) {
        return userProfileDao.findById(id);
    }

}
