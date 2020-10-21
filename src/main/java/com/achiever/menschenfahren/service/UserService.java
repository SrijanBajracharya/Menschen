package com.achiever.menschenfahren.service;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.springframework.lang.NonNull;

import com.achiever.menschenfahren.entities.users.User;
import com.achiever.menschenfahren.entities.users.UserProfile;

public interface UserService {

	List<User> getUsers(final boolean alsoVoided);

	/**
	 * Returns event based on event id.
	 *
	 * @param eventId The id of the event.
	 * @return
	 */
	Optional<User> findByIdAndVoided(@Nonnull final String id, final boolean alsoVoided);

	/**
	 * Adds new User.
	 *
	 * @param user
	 * @return
	 */
	User addUser(@NonNull final User user);

	/**
	 *
	 * @param userProfile
	 * @param alsoVoided
	 * @return
	 */
	UserProfile addProfile(@Nonnull final UserProfile userProfile, final boolean alsoVoided);

	/**
	 * De-activates user.
	 *
	 * @param uuid
	 */
	void deActivateUser(@NonNull final String uuid);

	/**
	 * Changes User Password.
	 *
	 * @param uuid
	 * @param password
	 * @return
	 */
	User changePassword(@NonNull final String uuid, @NonNull final String password);

	Optional<User> findById(@Nonnull final String uuid);

}
