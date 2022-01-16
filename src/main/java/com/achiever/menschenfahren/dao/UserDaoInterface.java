package com.achiever.menschenfahren.dao;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.achiever.menschenfahren.entities.users.User;

/**
 * Interface which contains methods for User operation.
 *
 * @author Srijan Bajracharya
 *
 */
@Repository
public interface UserDaoInterface extends JpaRepository<User, String> {

    /**
     * Checks if user already exists with the same email id.
     *
     * @param email:
     *            The email id of user.
     * @return True if user exists, False if user doesn't exist.
     */
    // boolean findEmailExists(@NonNull final String email);

    /**
     * /** Checks if the email exists already or not, if the email exists throws exception, if no result found then null is returned.
     *
     * @param email:
     *            The email id of user.
     * @return The user which matches email.
     */
    User findByEmail(@NonNull final String email);
    
    
    /**
     * /** Checks if the username already already or not, if the email exists throws exception, if no result found then null is returned.
     *
     * @param username:
     *            The username of the user.
     * @return The user which matches email.
     */
    User findByUsername(@NonNull final String username);

    /**
     * Deactivates user. Find {@link User} by uuid, if user exists then deactivates user.
     *
     * @param uuid
     *            The id of user.
     */
    // void deActivateUser(@NonNull final String uuid);

    /**
     * Finds Users using voided filter
     *
     * @param alsoVoided
     * @return
     */
    List<User> findByVoided(final boolean alsoVoided);

    Optional<User> findByIdAndVoided(@Nonnull final String userId, final boolean alsoVoided);

    // boolean findUserProfileExistsByUserId(@Nonnull final Long userId);

}
