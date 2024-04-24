package com.sprint.socialmeli.controller;

import com.sprint.socialmeli.dto.user.FollowersResponseDTO;
import com.sprint.socialmeli.service.user.IUsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UsersController {


    private IUsersService _usersService;

    public UsersController(IUsersService usersService){
        this._usersService = usersService;
    }

    // 1.
    @PostMapping("/{userId}/follow/{userIdToFollow}")
    public ResponseEntity<?> follow(
            @PathVariable("userId") Integer userId,
            @PathVariable("userIdToFollow") Integer userIdToFollow) {

        _usersService.follow(userId, userIdToFollow);

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    // 2.
    @GetMapping("/{userId}/followers/count")
    public ResponseEntity<?> countFollowers(@PathVariable("userId") Integer userId) {
        return new ResponseEntity<>("OK2", HttpStatus.OK);
    }


    // 3.
    @GetMapping("/{userId}/followers/list")
    public ResponseEntity<?> listFollowers(@PathVariable("userId") Integer userId) {

        FollowersResponseDTO followers = _usersService.getfollowers(userId );

        return new ResponseEntity<>(followers, HttpStatus.OK);
    }

    // 4.
    @GetMapping("/{userId}/followed/list")
    public ResponseEntity<?> listFollowedUsers(@PathVariable("userId") Integer userId) {
        return new ResponseEntity<>("OK4", HttpStatus.OK);
    }

    // 7.
    @PostMapping("/{userId}/unfollow/{userIdToUnfollow}")
    public ResponseEntity<?> unfollow(@PathVariable Integer userId, @PathVariable Integer userIdToUnfollow) {
        return new ResponseEntity<>("OK7", HttpStatus.OK);
    }

}
