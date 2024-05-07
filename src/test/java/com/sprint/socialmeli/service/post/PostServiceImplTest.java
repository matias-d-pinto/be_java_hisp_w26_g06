package com.sprint.socialmeli.service.post;

import com.sprint.socialmeli.dto.post.FollowedProductsResponseDTO;
import com.sprint.socialmeli.dto.post.PostResponseDTO;
import com.sprint.socialmeli.dto.post.ProductDTO;
import com.sprint.socialmeli.entity.Customer;
import com.sprint.socialmeli.entity.Post;
import com.sprint.socialmeli.entity.Product;
import com.sprint.socialmeli.entity.User;
import com.sprint.socialmeli.entity.*;

import com.sprint.socialmeli.exception.BadRequestException;
import com.sprint.socialmeli.repository.post.IPostRepository;
import com.sprint.socialmeli.service.user.IUsersService;
import com.sprint.socialmeli.utils.DateOrderType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.ArrayList;
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

    Seller seller;
    Customer customer;
    List<Customer> customerList;
    List<Seller> sellerList;

    PostResponseDTO postResponseDTO_1;
    PostResponseDTO postResponseDTO_2;
    static List<Post> posts;
    static List<Post> postsLastTwoWeeks;

    @BeforeEach
    void setUp() {
        Integer sellerId = 1;
        Integer customerId = 101;
        seller = new Seller(new User(sellerId, "Martin"));
        customer = new Customer(new User(customerId, "Antonio"));

        customerList = new ArrayList<>(List.of(
                customer,
                new Customer(new User(102, "Nicolas"))
        ));
        sellerList = new ArrayList<>(List.of(
                seller,
                new Seller(new User(2, "Amanda")),
                new Seller(new User(3, "Francisco"))
        ));

        postResponseDTO_1 = new PostResponseDTO(101, 1, "8-05-2024",
                new ProductDTO(
                        2,
                        "Silla Interior",
                        "Exterior",
                        "Racer",
                        "Red and Black",
                        ""
                ), 1, 220.8);
        postResponseDTO_2 = new PostResponseDTO(101, 0, "10-05-2024",
                        new ProductDTO(
                                1,
                                "Silla exterior",
                                "Exterior",
                                "Racer",
                                "Red and Black",
                                ""
                        ), 1, 120.1);
    }

    @BeforeAll
    static void setUpBeforeClass() {
        posts = List.of(
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
        postsLastTwoWeeks = List.of(
                new Post(
                        new Product(
                                1,
                                "Silla exterior",
                                "Exterior",
                                "Racer",
                                "Red and Black",
                                ""
                        ), LocalDate.now().minusWeeks(1), 1, 120.1
                )
        );
    }

    // Order by date unit tests - T-0005 -----------------START
    // ---- Bad case ------
    @Test
    @DisplayName("Date order type does not exist")
    void orderTypeDoesNotExists() {
        // Arrange
        String order = "asd"; // Random string

        // Act & assert
        Mockito.when(usersService.checkAndGetCustomer(customer.getUser().getUserId())).thenReturn(customer);

        assertThrows(BadRequestException.class, () -> this.postService.getFollowedProductsList(customer.getUser().getUserId(), order));
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

        // Act
        Mockito.when(usersService.checkAndGetCustomer(customer.getUser().getUserId())).thenReturn(customer);

        FollowedProductsResponseDTO response = this.postService.getFollowedProductsList(customer.getUser().getUserId(), order.toString());

        // Assert
        assertNotNull(response);
    }

    // Order by date unit tests - T-0005 -------------------END

    // -------------------------- T-0006 -----------------START
    @Test
    @DisplayName("Verify correct order by asc date")
    void verifyCorrectOrderAscByDate() {
        // Arrange
        // Creating the possible response
        List<PostResponseDTO> postsResponse = List.of(postResponseDTO_1, postResponseDTO_2);

        // Assert
        this.verifyCorrectOrderByDate(postsResponse, DateOrderType.DATE_ASC);
    }

    @Test
    @DisplayName("Verify correct order by desc date")
    void verifyCorrectOrderDescByDate() {
        // Arrange
        // Creating the possible response
        List<PostResponseDTO> postsResponse = List.of(postResponseDTO_2, postResponseDTO_1);

        // Assert
        this.verifyCorrectOrderByDate(postsResponse, DateOrderType.DATE_DESC);
    }

    // Reusable method for T-0006 good case
    private void verifyCorrectOrderByDate(List<PostResponseDTO> postsResponse, DateOrderType order) {
        // Arrange
        // Customer
        customer.setFollowed(List.of(1));

        // Act
        Mockito.when(usersService.checkAndGetCustomer(customer.getUser().getUserId())).thenReturn(customer);
        Mockito.when(postRepository.findBySellerId(seller.getUser().getUserId())).thenReturn(posts);

        FollowedProductsResponseDTO response = this.postService.getFollowedProductsList(customer.getUser().getUserId(), order.toString());

        // Assertion
        assertEquals(posts.size(), response.getPosts().size());
        assertEquals(postsResponse, response.getPosts());
    }
    // -------------------------- T-0006 -------------------END

    // Last two weeks posts unit tests - T-0008 -----------------START

    @Test
    @DisplayName("Get posts from last two weeks")
    void getPostsFromLastTwoWeeks() {
        String order = "date_desc";

        Mockito.when(usersService.checkAndGetCustomer(customer.getUser().getUserId())).thenReturn(customer);

        customer.setFollowed(List.of(1));

        Mockito.when(postRepository.findBySellerId(seller.getUser().getUserId())).thenReturn(postsLastTwoWeeks);

        // Act
        FollowedProductsResponseDTO response = this.postService.getFollowedProductsList(customer.getUser().getUserId(), order);

        // Assert
        assertEquals(postsLastTwoWeeks.size(), response.getPosts().size());

    }

    @Test
    @DisplayName("NOT Get posts from beyond two weeks")
    void notGetPostsFromBeyondTwoWeeks() {

        String order = "date_desc";

        List<Post> posts = List.of(
                new Post(
                        new Product(
                                1,
                                "Silla exterior",
                                "Exterior",
                                "Racer",
                                "Red and Black",
                                ""
                        ), LocalDate.now().minusWeeks(3), 1, 120.1
                )
        );

        Mockito.when(usersService.checkAndGetCustomer(customer.getUser().getUserId())).thenReturn(customer);

        customer.setFollowed(List.of(1));

        Mockito.when(postRepository.findBySellerId(seller.getUser().getUserId())).thenReturn(posts);

        // Act
        FollowedProductsResponseDTO response = this.postService.getFollowedProductsList(customer.getUser().getUserId(), order);


        // Assert
        assertEquals(0, response.getPosts().size());

    }
    // Last two weeks posts unit tests - T-0008 -----------------END

}