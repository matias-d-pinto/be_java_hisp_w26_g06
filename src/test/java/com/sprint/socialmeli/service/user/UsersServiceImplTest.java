package com.sprint.socialmeli.service.user;

import com.sprint.socialmeli.dto.user.FollowersResponseDTO;
import com.sprint.socialmeli.dto.user.UserResponseDTO;
import com.sprint.socialmeli.entity.Customer;
import com.sprint.socialmeli.entity.Seller;
import com.sprint.socialmeli.entity.User;
import com.sprint.socialmeli.exception.BadRequestException;
import com.sprint.socialmeli.exception.NotFoundException;
import com.sprint.socialmeli.mappers.UserMapper;
import com.sprint.socialmeli.repository.user.IUsersRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UsersServiceImplTest {

    @Mock
    IUsersRepository usersRepository;

    @InjectMocks
    UsersServiceImpl usersService;

    // Order by name unit tests - T-0003 -----------------START

    // ---- Bad case ------
    @Test
    @DisplayName("Order type name does not exist")
    void orderTypeDoesNotExists() {
        // Arrange
        Integer sellerId = 1;
        String order = "asd";

        List<Customer> customerList = List.of(
                new Customer(new User(101, "Matias")),
                new Customer(new User(102, "Nicolas"))
        );

        List<UserResponseDTO> usersDto = customerList
                .stream()
                .map(UserMapper::mapCustomerToUserResponseDto)
                .toList();

        Seller seller = new Seller(new User(1, "Tesla X"));

        Mockito.when(usersRepository.findSellerById(sellerId)).thenReturn(seller);

        // Act & assert
        assertThrows(BadRequestException.class, () -> usersService.getFollowers(sellerId, order));

    }

    // ---- Good case ------

    @Test
    @DisplayName("Order type 'name_asc' exists")
    void testOrderTypeNameAscExists() {
        this.orderTypeExists("name_asc");
    }

    @Test
    @DisplayName("Order type 'name_desc' exists")
    void testOrderTypeNameDescExists() {
        this.orderTypeExists("name_desc");
    }

    private void orderTypeExists(String order) {
        // Arrange
        Integer sellerId = 1;

        List<Customer> customerList = List.of(
                new Customer(new User(101, "Matias")),
                new Customer(new User(102, "Nicolas"))
        );

        List<UserResponseDTO> usersDto = customerList
                .stream()
                .map(UserMapper::mapCustomerToUserResponseDto)
                .toList();

        Seller seller = new Seller(new User(1, "Tesla X"));

        Mockito.when(
                usersRepository.findCustomerByPredicate(ArgumentMatchers.any())
        ).thenReturn(customerList);

        Mockito.when(usersRepository.findSellerById(sellerId)).thenReturn(seller);

        // Act
        FollowersResponseDTO response = usersService.getFollowers(sellerId, order);

        // Assert
        assertDoesNotThrow(() -> new BadRequestException("Invalid order type: " + order));
    }
    // Order by name unit tests - T-0003 -------------------END


    // User to unfollow should exists - T-0002 -----------------START

    // Good case - Normal flow
    @Test
    @DisplayName("User to unfollow should exists")
    public void userToUnfollowExist(){
        // Arrange
        int sellerId = 1;
        int customerId = 101;

        // Mock of the seller
        Seller existingSeller = new Seller(new User(sellerId, "Roman"));

        // Mock of the customer who already follows the seller
        Customer existingCustomer = new Customer(new User(101, "Damian"));
        existingCustomer.setFollowed(new ArrayList<>(Arrays.asList(sellerId)));

        // Act & Assert
        Mockito.when(usersRepository.findSellerById(sellerId)).thenReturn(existingSeller);
        Mockito.when(usersRepository.findCustomerById(customerId)).thenReturn(existingCustomer);

        assertDoesNotThrow(() -> usersService.unfollow(101, 1));
    }

    // Bad case - Follower does not exist
    @Test
    @DisplayName("User to unfollow should exists")
    public void userToUnfollowDoesNotExist(){
        // Arrange
        int sellerId = 1;
        int customerId = 101;

        // Mock of the seller
        Seller existingSeller = new Seller(new User(sellerId, "Roman"));
        Mockito.when(usersRepository.findSellerById(sellerId)).thenReturn(null);

        // Mock of the customer who already follows the seller
        Customer existingCustomer = new Customer(new User(101, "Damian"));
        existingCustomer.setFollowed(new ArrayList<>(Arrays.asList(sellerId)));

        // Act & Assert
        Mockito.when(usersRepository.findCustomerById(customerId)).thenReturn(existingCustomer);

        assertThrows(NotFoundException.class, () -> usersService.unfollow(101, 1));
    }
}