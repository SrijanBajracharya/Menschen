package com.achiever.menschenfahren.security.jwt;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.achiever.menschenfahren.dao.UserDaoInterface;
import com.achiever.menschenfahren.entities.users.User;
import com.achiever.menschenfahren.exception.EmailNotFoundException;
import com.achiever.menschenfahren.security.model.ExtendedUserDetails;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDaoInterface userDao;

    @Override
    public UserDetails loadUserByUsername(@Nonnull final String username) throws UsernameNotFoundException {
        User user = userDao.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }

    /**
     * Loads user by email.
     *
     * @param user
     * @return
     * @throws EmailNotFoundException
     */
    public ExtendedUserDetails loadByEmail(@Nonnull final User user) throws EmailNotFoundException {
        User daoUser = userDao.findByEmail(user.getEmail());
        if (daoUser == null) {
            throw new EmailNotFoundException("User not found with email: " + user.getEmail());
        }
        return new ExtendedUserDetails(daoUser.getUsername(), daoUser.getEmail(), daoUser.getPassword(), new ArrayList<>());
    }

}