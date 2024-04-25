package com.sprint.socialmeli.entity;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
public class Customer {
    private Map<Integer, LocalDate> followed = new HashMap<>();
    private User user;

    public Customer(User user) {
        this.user = user;
    }

    /**
     *
     * @param userIdToFollow seller id
     * add the seller id to the followed list
     */
    public void follow(Integer userIdToFollow, LocalDate date){
        followed.put(userIdToFollow, date);
    }

    /**
     *
     * @param userIdToFollow seller id
     * remove the seller id from the followed list
     */
    public void unfollow(Integer userIdToFollow){
        followed.remove(userIdToFollow);
    }
}
