package com.sprint.socialmeli.service.user;

import org.springframework.stereotype.Service;

@Service
public class UsersServiceImpl implements IUsersService{

    @Override
    public void follow(Integer customerId, Integer sellerId) {
        System.out.println("Hello world");
    }

}
