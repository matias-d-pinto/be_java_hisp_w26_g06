package com.sprint.socialmeli.repository.post;

import com.sprint.socialmeli.entity.Post;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PostRepositoryImpl implements IPostRepository{
    Map<Integer, List<Post>> postMap = new HashMap<>();

    @Override
    public void save(Post post, Integer sellerId) {
    }

    @Override
    public List<Post> findBySellerId(Integer sellerId) {
        return postMap.get(sellerId);
    }
}
