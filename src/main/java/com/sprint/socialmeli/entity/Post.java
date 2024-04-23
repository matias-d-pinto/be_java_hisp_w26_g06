package com.sprint.socialmeli.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class Post {
    private static Integer id;
    private Product product;
    private LocalDate postDate;
    private Integer category;
    private double price;
}
