package com.sprint.socialmeli.service.post;

import com.sprint.socialmeli.dto.post.PostDTO;
import com.sprint.socialmeli.entity.Post;
import com.sprint.socialmeli.entity.Product;
import com.sprint.socialmeli.exception.BadRequestException;
import com.sprint.socialmeli.repository.post.IPostRepository;
import com.sprint.socialmeli.repository.user.IUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class PostServiceImpl implements IPostService {

    @Autowired
    IPostRepository postRepository;

    @Autowired
    IUsersRepository usersRepository;

    @Override
    public void createPost(PostDTO postDTO) {
        boolean userNotFound = this.usersRepository
                .findSellerByPredicate(c -> c.getUser().getUserId().equals(postDTO.getUser_id()))
                .isEmpty();
        if(userNotFound){
            throw new BadRequestException("Seller with id: "+ postDTO.getUser_id() +" does not exist");
        } else{
            Post newPost = parsePostDTO(postDTO);
            this.postRepository.save(newPost, postDTO.getUser_id());
        }
    }

    private static Post parsePostDTO(PostDTO postDTO) {
        try {
            Product product = new Product(
                    postDTO.getProduct().getProduct_id(),
                    postDTO.getProduct().getProduct_name(),
                    postDTO.getProduct().getType(),
                    postDTO.getProduct().getColor(),
                    postDTO.getProduct().getBrand(),
                    postDTO.getProduct().getNotes()
            );
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate date = LocalDate.parse(postDTO.getDate(), formatter);
            return new Post(product, date, postDTO.getCategory(), postDTO.getPrice());
        } catch (DateTimeException | IllegalArgumentException e) {
            throw new BadRequestException("Formato inválido " + e.getMessage());
        }
    }
}
