package com.sprint.socialmeli.service.user;

import com.sprint.socialmeli.dto.user.FollowersResponseDTO;
import com.sprint.socialmeli.dto.user.FollowersResponseDTO;
import com.sprint.socialmeli.dto.user.FollowerCountResponseDTO;


public interface IUsersService {

    void follow(Integer customerId, Integer sellerId);
    FollowerCountResponseDTO getFollowersCount(Integer sellerId);

    FollowersResponseDTO getfollowers(Integer sellerId );
}
