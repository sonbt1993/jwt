package com.example.demo;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class UserServiceTests {

    @MockBean
    private UserRepository  userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testDeduct(){
        String sender = "test";
        double amount = 1000.0;

        User user = new User();
        Role role = new Role();
        role.setId(1);
        role.setName("test");
        user.setUsername(sender);
        user.setEmail("test@gmail.com");
        user.setPassword("123");
        user.setRole(role);
        user.setCredit(5000.0);

        Mockito.when(userRepository.findUserByUsername(sender)).thenReturn(user);

        userService.deduct(sender,amount);

        Assertions.assertEquals(4000.0,user.getCredit());
        System.out.println(user.getCredit());

    }
}
