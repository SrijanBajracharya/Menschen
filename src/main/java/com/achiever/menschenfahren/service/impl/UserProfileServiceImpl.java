package com.achiever.menschenfahren.service.impl;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.achiever.menschenfahren.base.exception.MultipleResourceFoundException;
import com.achiever.menschenfahren.base.exception.ResourceNotFoundException;
import com.achiever.menschenfahren.dao.UserProfileDaoInterface;
import com.achiever.menschenfahren.entities.users.User;
import com.achiever.menschenfahren.entities.users.UserProfile;
import com.achiever.menschenfahren.service.UserProfileService;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileDaoInterface userProfileDao;

    @Autowired
    public UserProfileServiceImpl(@Nonnull final UserProfileDaoInterface userProfileDao) {
        this.userProfileDao = userProfileDao;
    }

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

    @Override
    public UserProfile findByUser(@Nonnull final User user) throws MultipleResourceFoundException, ResourceNotFoundException {
        List<UserProfile> userList = userProfileDao.findByUser(user);
        if (userList != null && userList.size() == 1) {
            return userList.get(0);
        } else if (userList.size() == 0) {
            throw new ResourceNotFoundException("No Profile found for user.");
        } else {
            throw new MultipleResourceFoundException("Multiple User profile found for one user.");
        }
    }

}
