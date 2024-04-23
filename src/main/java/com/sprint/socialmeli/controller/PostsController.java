package com.sprint.socialmeli.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class PostsController {

    // 5.
    @PostMapping("/post")
    public ResponseEntity<?> createPost(@RequestBody String asd){
        return new ResponseEntity<>("OK POST 5", HttpStatus.OK);
    }

    // 6.
    @GetMapping("/followed/{userId}/list")
    public ResponseEntity<?> getFollowedPosts(@PathVariable Integer userId){
        return new ResponseEntity<>("OK POST 6", HttpStatus.OK);
    }

}
