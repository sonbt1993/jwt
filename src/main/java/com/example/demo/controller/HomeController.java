package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/about")
    public String home() {
        return "home";
    }

    @GetMapping("/book")
    public String book(Model model) {
        String[] bookCollection = {
                "Deep work", "Nha gia kim", "Cafe cung Tony"
        };
        model.addAttribute("books", bookCollection);
        return "book";
    }
}