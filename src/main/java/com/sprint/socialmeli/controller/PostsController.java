package com.sprint.socialmeli.controller;

import com.sprint.socialmeli.dto.post.PostDTO;
import com.sprint.socialmeli.repository.post.IPostRepository;
import com.sprint.socialmeli.service.post.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class PostsController {

    @Autowired
    IPostService postService;

    // 5.
    @PostMapping("/post")
    public ResponseEntity<?> createPost(@RequestBody PostDTO post){
        this.postService.createPost(post);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 6.
    @GetMapping("/followed/{userId}/list")
    public ResponseEntity<?> getFollowedPosts(@PathVariable Integer userId){
        return new ResponseEntity<>("OK POST 6", HttpStatus.OK);
    }

}
