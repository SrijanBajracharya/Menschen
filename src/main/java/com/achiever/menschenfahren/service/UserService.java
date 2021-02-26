package com.achiever.menschenfahren.service;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.springframework.lang.NonNull;

import com.achiever.menschenfahren.entities.users.User;
import com.achiever.menschenfahren.exception.InvalidUserException;

/**
 * Interface handling all functionality for user service.
 *
 * @author Srijan Bajracharya
 *
 */
public interface UserService {

    /**
     * Returns all users based on also voided.
     *
     * @param alsoVoided
     * @return
     */
    List<User> getUsers(final boolean alsoVoided);

    /**
     * Returns event based on event id.
     *
     * @param eventId
     *            The id of the event.
     * @return
     */
    Optional<User> findByIdAndVoided(@Nonnull final String id, final boolean alsoVoided);

    /**
     * Adds new User.
     *
     * @param user
     * @return
     * @throws InvalidUserException
     */
    User addUser(@NonNull final User user) throws InvalidUserException;

    User updateUser(@Nonnull final User user);

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

    /**
     * Finds user by id.
     *
     * @param uuid
     *            The id of user.
     * @return The optional user by id.
     */
    Optional<User> findById(@Nonnull final String uuid);

    /**
     * Finds user by email.
     *
     * @param emailId
     *            The id of email.
     * @return The optional User.
     */
    User findByEmail(@Nonnull final String emailId);

}
