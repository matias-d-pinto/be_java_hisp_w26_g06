package com.sprint.socialmeli.service.post;

import com.sprint.socialmeli.dto.post.FollowedProductsResponseDTO;
import com.sprint.socialmeli.entity.Customer;
import com.sprint.socialmeli.entity.User;
import com.sprint.socialmeli.exception.BadRequestException;
import com.sprint.socialmeli.repository.post.IPostRepository;
import com.sprint.socialmeli.service.user.IUsersService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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
        Integer customerId = 101;
        String order = "asd";

        Customer customer = new Customer(new User(customerId, "Roberto"));

        Mockito.when(usersService.checkAndGetCustomer(customerId)).thenReturn(customer);

        // Act & assert
        assertThrows(BadRequestException.class, () -> this.postService.getFollowedProductsList(customerId, order));

    }

    // ---- Good case ------

    @Test
    @DisplayName("Date order type 'date_asc' exists")
    void dateOrderTypeAscExists (){
        verifyOrderTypeExists("date_asc");
    }

    @Test
    @DisplayName("Date order type 'date_desc' exists")
    void dateOrderTypeDescExists (){
        verifyOrderTypeExists("date_desc");
    }

    private void verifyOrderTypeExists(String order){
        // Arrange
        Integer customerId = 101;

        Customer customer = new Customer(new User(customerId, "Roberto"));

        Mockito.when(usersService.checkAndGetCustomer(customerId)).thenReturn(customer);

        // Act
        FollowedProductsResponseDTO response = this.postService.getFollowedProductsList(customerId, order);

        // Assert
        assertNotNull(response);
        assertDoesNotThrow(() -> BadRequestException.class);
    }

    // Order by date unit tests - T-0005 -------------------END
}