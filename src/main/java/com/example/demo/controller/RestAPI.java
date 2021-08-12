package com.example.demo.controller;

import com.example.demo.entity.UserList;


import com.example.demo.entity.User;
import com.example.demo.entity.UserPost;

import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class RestAPI {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final com.example.demo.service.UserDetailServiceImpl userDetailServiceImpl;
    private final com.example.demo.security.jwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;
    private final WebClient.Builder webClientBuilder;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        System.out.println("ok");

        final UserDetails userDetails = userDetailServiceImpl
                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtUtils.generateToken(userDetails);

        return ResponseEntity.ok(token);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            System.out.println(new UsernamePasswordAuthenticationToken(username, password));

        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

//    @GetMapping(value = "/users")
//    public List<User> getAllUser() {
//        return userRepository.findAll();
//    }

    @GetMapping(value = "/users",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserList getAllUser() {
        UserList userList = new UserList();
        userList.setUserList(userRepository.findAll());
        return userList;
    }

    @GetMapping(value = "/users/{id}")
    public Optional<User> getUser(@PathVariable(name = "id") Long id) {
        return userRepository.findById(id);
    }

    @PostMapping(value = "/users")
    public User addUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @PutMapping(value = "/users/{id}")
    public User updateUser(@PathVariable(name = "id") Long id, @RequestBody User userDetails) {
        User user = userRepository.findUserById(id);

        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        user.setRole(userDetails.getRole());
        user.setCredit(userDetails.getCredit());

        return userRepository.save(user);
    }

    @DeleteMapping(value = "/users/{id}")
    public void deleteUser(@PathVariable(name = "id") Long id) {
        userRepository.deleteById(id);
    }

    @GetMapping(value = "/userPost")
    public List<UserPost> getAllUserPost() {
        return postRepository.getAllPostAndUser();
    }

    @GetMapping(value = "/restTemplate")
    public List<User> restTemplate(HttpServletRequest request) {
        String requestTokenHeader = request.getHeader("Authorization");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", requestTokenHeader);
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        String url = "http://localhost:8080/api/users";
//        UserList users = restTemplate.getForObject(url, UserList.class);
        ResponseEntity<UserList> users = restTemplate.exchange(url, HttpMethod.GET, entity, UserList.class);


        return users.getBody().getUserList();
    }

    @GetMapping(value = "/webClient")
    public List<User> webClient(HttpServletRequest request) {
        String requestTokenHeader = request.getHeader("Authorization");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", requestTokenHeader);
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        String url = "http://localhost:8080/api/users";
//        UserList users = restTemplate.getForObject(url, UserList.class);
        UserList users = webClientBuilder.defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json").build()
                .get()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, requestTokenHeader)
                .retrieve()
                .bodyToMono(UserList.class)
                .block();

        return users.getUserList();
    }


}
