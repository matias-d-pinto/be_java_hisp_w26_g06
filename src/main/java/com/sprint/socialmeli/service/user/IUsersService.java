package com.sprint.socialmeli.service.user;

<<<<<<< Updated upstream
=======

import com.sprint.socialmeli.dto.user.FollowersResponseDTO;
>>>>>>> Stashed changes
import com.sprint.socialmeli.dto.user.FollowerCountResponseDTO;

public interface IUsersService {

    void follow(Integer customerId, Integer sellerId);
    FollowerCountResponseDTO getFollowersCount(Integer sellerId);

}
