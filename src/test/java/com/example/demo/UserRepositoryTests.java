package com.example.demo;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;



@DataJpaTest
@ActiveProfiles("test")
//@TestPropertySource(locations = "classpath:application-test.properties")
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testCreateUser(){

        User user = new User();
        Role role = new Role();
        role.setId(1);
        role.setName("test");
        roleRepository.save(role);
        user.setUsername("test");
        user.setEmail("test@gmail.com");
        user.setPassword("123");
        user.setRole(role);

        userRepository.save(user);
        Assertions.assertThat(user).isNotNull();


    }
}
