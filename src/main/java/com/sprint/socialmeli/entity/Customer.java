package com.sprint.socialmeli.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
public class Customer {
    private List<Integer> followed = new ArrayList<>();
    private User user;

    public Customer(User user) {
        this.user = user;
    }
}
