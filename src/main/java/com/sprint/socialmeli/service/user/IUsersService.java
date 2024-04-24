package com.sprint.socialmeli.service.user;

import com.sprint.socialmeli.dto.user.FollowersResponseDTO;
import com.sprint.socialmeli.dto.user.FollowersResponseDTO;
import com.sprint.socialmeli.dto.user.FollowerCountResponseDTO;


import com.sprint.socialmeli.dto.user.FollowedResponseDTO;

public interface IUsersService {

    void follow(Integer customerId, Integer sellerId);
    FollowerCountResponseDTO getFollowersCount(Integer sellerId);

    FollowersResponseDTO getfollowers(Integer sellerId );
    void unfollow(Integer userId, Integer userIdToUnfollow);
    FollowedResponseDTO listFollowedUsers(Integer userId);
}
