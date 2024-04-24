package com.sprint.socialmeli.service.user;


import com.sprint.socialmeli.dto.user.FollowersResponseDTO;

public interface IUsersService {

    void follow(Integer customerId, Integer sellerId);

    FollowersResponseDTO getfollowers(Integer sellerId );
}
