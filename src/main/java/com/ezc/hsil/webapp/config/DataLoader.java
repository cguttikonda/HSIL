package com.ezc.hsil.webapp.config;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ezc.hsil.webapp.model.Privilages;
import com.ezc.hsil.webapp.model.Roles;
import com.ezc.hsil.webapp.model.Users;
import com.ezc.hsil.webapp.persistance.dao.PrivilegeRepository;
import com.ezc.hsil.webapp.persistance.dao.RoleRepository;
import com.ezc.hsil.webapp.persistance.dao.UserRepository;

@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // API

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        // == create initial privileges
        final Privilages readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        final Privilages writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
        final Privilages passwordPrivilege = createPrivilegeIfNotFound("CHANGE_PASSWORD_PRIVILEGE");

        // == create initial roles
        final List<Privilages> adminPrivileges = new ArrayList<Privilages>(Arrays.asList(readPrivilege, writePrivilege, passwordPrivilege));
        final List<Privilages> userPrivileges = new ArrayList<Privilages>(Arrays.asList(readPrivilege, passwordPrivilege));
        final Roles adminRole = createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_USER", userPrivileges);

        // == create initial user
        createUserIfNotFound("Admin", "test@test.com","Admin", "User", "admin", new ArrayList<Roles>(Arrays.asList(adminRole)));

        alreadySetup = true;
    }

    @Transactional
    private final Privilages createPrivilegeIfNotFound(final String name) {
    	Privilages privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilages(name);
            privilege = privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    private final Roles createRoleIfNotFound(final String name, final Collection<Privilages> privileges) {
        Roles role = roleRepository.findByName(name);
        if (role == null) {
            role = new Roles(name);
        }
        role.setPrivileges(privileges);
        role = roleRepository.save(role);
        return role;
    }

    @Transactional
    private final Users createUserIfNotFound(final String userId,final String email, final String firstName, final String lastName, final String password, final Collection<Roles> roles) {
        Users user = userRepository.findByEmail(email);
        if (user == null) {
            user = new Users();
            user.setUserId(userId);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPassword(passwordEncoder.encode(password));
            user.setEmail(email);
            user.setEnabled(true);
        }
        user.setRoles(roles);
        user = userRepository.save(user);
        return user;
    }

}