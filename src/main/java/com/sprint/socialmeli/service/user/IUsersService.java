package com.sprint.socialmeli.service.user;

import com.sprint.socialmeli.dto.user.FollowedResponseDTO;

public interface IUsersService {

    void follow(Integer customerId, Integer sellerId);
    FollowedResponseDTO listFollowedUsers(Integer userId);
}
