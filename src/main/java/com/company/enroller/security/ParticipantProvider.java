package com.company.enroller.security;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class ParticipantProvider implements UserDetailsService {

    @Autowired
    private ParticipantService participantService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Participant participant = participantService.findByLogin(username);
        if (participant == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return User.builder()
                .username(participant.getLogin())
                .password(participant.getPassword())
                .roles("USER")
                .build();
    }
}