package com.sprint.socialmeli.service.user;

import com.sprint.socialmeli.dto.user.FollowerCountResponseDTO;

public interface IUsersService {

    void follow(Integer customerId, Integer sellerId);
    FollowerCountResponseDTO getFollowersCount(Integer sellerId);

}
