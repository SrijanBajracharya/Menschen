package com.achiever.menschenfahren.service.impl;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.achiever.menschenfahren.dao.UserDaoInterface;
import com.achiever.menschenfahren.dao.UserProfileDaoInterface;
import com.achiever.menschenfahren.entities.users.User;
import com.achiever.menschenfahren.entities.users.UserProfile;
import com.achiever.menschenfahren.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDaoInterface userDao;

	@Autowired
	private UserProfileDaoInterface userProfileDao;

	@Override
	public List<User> getUsers(final boolean alsoVoided) {
		final List<User> users;
		if (alsoVoided) {
			users = userDao.findAll();
		} else {
			users = userDao.findByVoided(alsoVoided);
		}
		return users;
	}

	/**
	 * Returns event based on event id.
	 *
	 * @param eventId The id of the event.
	 * @return
	 */
	@Override
	public Optional<User> findById(@Nonnull final String userId, final boolean alsoVoided) {
		return this.userDao.findByIdAndVoided(userId, alsoVoided);
	}

	/**
	 * Adds new User.
	 *
	 * @param user
	 * @return
	 */
	@Override
	public User addUser(@NonNull final User user) {
		final User userExists = userDao.findByEmail(user.getEmail());
		if (userExists == null) {
			userDao.save(user);
		}
		return user;
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

	/**
	 * De-activates user.
	 *
	 * @param uuid
	 */
	@Override
	public void deActivateUser(@NonNull final String uuid) {
		// userDao.deActivateUser(uuid);
	}

	/**
	 * Changes User Password.
	 *
	 * @param uuid
	 * @param password
	 * @return
	 */
	@Override
	public User changePassword(@NonNull final String uuid, @NonNull final String password) {
		final Optional<User> user = userDao.findById(uuid);
		if (user.isPresent()) {
			final User u = user.get();
			u.setPassword(password);
			userDao.save(u);
			return u;
		} else {
			throw new IllegalArgumentException("No user found with userId:" + uuid);
		}

	}

}
