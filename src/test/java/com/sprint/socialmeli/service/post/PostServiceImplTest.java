package com.sprint.socialmeli.service.post;

import com.sprint.socialmeli.dto.post.FollowedProductsResponseDTO;
import com.sprint.socialmeli.dto.post.PostResponseDTO;
import com.sprint.socialmeli.dto.post.ProductDTO;
import com.sprint.socialmeli.entity.Customer;
import com.sprint.socialmeli.entity.Post;
import com.sprint.socialmeli.entity.Product;
import com.sprint.socialmeli.entity.User;
import com.sprint.socialmeli.exception.BadRequestException;
import com.sprint.socialmeli.repository.post.IPostRepository;
import com.sprint.socialmeli.service.user.IUsersService;
import com.sprint.socialmeli.utils.DateOrderType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock
    IUsersService usersService;

    @Mock
    IPostRepository postRepository;

    @InjectMocks
    PostServiceImpl postService;

    // Order by date unit tests - T-0005 -----------------START

    // ---- Bad case ------
    @Test
    @DisplayName("Date order type does not exist")
    void orderTypeDoesNotExists() {
        // Arrange
        Integer customerId = 101;
        String order = "asd"; // Random string

        Customer customer = new Customer(new User(customerId, "Roberto"));

        // Act & assert
        Mockito.when(usersService.checkAndGetCustomer(customerId)).thenReturn(customer);

        assertThrows(BadRequestException.class, () -> this.postService.getFollowedProductsList(customerId, order));
    }

    // ---- Good case ------
    @Test
    @DisplayName("Date order type 'date_asc' exists")
    void dateOrderTypeAscExists() {
        verifyOrderTypeExists(DateOrderType.DATE_ASC);
    }

    @Test
    @DisplayName("Date order type 'date_desc' exists")
    void dateOrderTypeDescExists() {
        verifyOrderTypeExists(DateOrderType.DATE_DESC);
    }

    // Reusable method for T-0005 good case
    private void verifyOrderTypeExists(DateOrderType order) {
        // Arrange
        Integer customerId = 101;

        Customer customer = new Customer(new User(customerId, "Roberto"));

        // Act
        Mockito.when(usersService.checkAndGetCustomer(customerId)).thenReturn(customer);

        FollowedProductsResponseDTO response = this.postService.getFollowedProductsList(customerId, order.toString());

        // Assert
        assertNotNull(response);
        assertDoesNotThrow(() -> BadRequestException.class);
    }

    // Order by date unit tests - T-0005 -------------------END

    // -------------------------- T-0006 -----------------START
    @Test
    @DisplayName("Verify correct order by asc date")
    void verifyCorrectOrderAscByDate() {
        // Arrange
        // Creating the possible response
        List<PostResponseDTO> postsResponse = List.of(
                new PostResponseDTO(101, 1, "8-05-2024",
                        new ProductDTO(
                                2,
                                "Silla Interior",
                                "Exterior",
                                "Racer",
                                "Red and Black",
                                ""
                        ), 1, 220.8),
                new PostResponseDTO(101, 0, "10-05-2024",
                        new ProductDTO(
                                1,
                                "Silla exterior",
                                "Exterior",
                                "Racer",
                                "Red and Black",
                                ""
                        ), 1, 120.1)
        );

        // Assert
        this.verifyCorrectOrderByDate(postsResponse, DateOrderType.DATE_ASC);
    }

    @Test
    @DisplayName("Verify correct order by desc date")
    void verifyCorrectOrderDescByDate() {
        // Arrange
        // Creating the possible response
        List<PostResponseDTO> postsResponse = List.of(
        new PostResponseDTO(101, 0, "10-05-2024",
            new ProductDTO(
                    1,
                    "Silla exterior",
                    "Exterior",
                    "Racer",
                    "Red and Black",
                    ""
            ), 1, 120.1),
            new PostResponseDTO(101, 1, "8-05-2024",
                    new ProductDTO(
                            2,
                            "Silla Interior",
                            "Exterior",
                            "Racer",
                            "Red and Black",
                            ""
                    ), 1, 220.8)
        );

        // Assert
        this.verifyCorrectOrderByDate(postsResponse, DateOrderType.DATE_DESC);
    }

    // Reusable method for T-0003 good case
    private void verifyCorrectOrderByDate(List<PostResponseDTO> postsResponse, DateOrderType order) {
        // Arrange
        // Customer
        Integer customerId = 101;
        Customer customer = new Customer(new User(customerId, "Roberto"));
        customer.setFollowed(List.of(1));

        // Seller
        Integer sellerId = 1;
        List<Post> posts = List.of(
            new Post(
                new Product(
                        1,
                        "Silla exterior",
                        "Exterior",
                        "Racer",
                        "Red and Black",
                        ""
                ), LocalDate.of(2024, 5,10), 1, 120.1
            ),
            new Post(
                new Product(
                        2,
                        "Silla Interior",
                        "Exterior",
                        "Racer",
                        "Red and Black",
                        ""
                ), LocalDate.of(2024, 5, 8), 1, 220.8
            )
        );

        // Act
        Mockito.when(usersService.checkAndGetCustomer(customerId)).thenReturn(customer);
        Mockito.when(postRepository.findBySellerId(sellerId)).thenReturn(posts);

        FollowedProductsResponseDTO response = this.postService.getFollowedProductsList(customerId, order.toString());

        // Assertion
        System.out.println(response.getPosts());
        assertEquals(2, response.getPosts().size());
        System.out.println(postsResponse.get(0).getPost_id() + " " + response.getPosts().get(0).getPost_id());
        assertEquals(postsResponse, response.getPosts());
    }
    // -------------------------- T-0006 -------------------END

}