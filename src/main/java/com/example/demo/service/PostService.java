package com.example.demo.service;

import com.example.demo.entity.UserPost;
import com.example.demo.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    @Autowired
    PostRepository postRepository;

    public List<UserPost> getPostAndUser(){
        return postRepository.getAllPostAndUser();
    };
}
