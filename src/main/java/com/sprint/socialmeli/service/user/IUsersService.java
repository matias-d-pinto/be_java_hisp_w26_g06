package com.sprint.socialmeli.service.user;

public interface IUsersService {

    void follow(Integer customerId, Integer sellerId);

    void unfollow(Integer userId, Integer userIdToUnfollow);
}
