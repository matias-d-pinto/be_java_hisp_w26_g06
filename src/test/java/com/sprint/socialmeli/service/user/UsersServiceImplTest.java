package com.sprint.socialmeli.service.user;

import com.sprint.socialmeli.dto.user.FollowedResponseDTO;
import com.sprint.socialmeli.dto.user.FollowersResponseDTO;
import com.sprint.socialmeli.dto.user.UserResponseDTO;
import com.sprint.socialmeli.entity.Customer;
import com.sprint.socialmeli.entity.Seller;
import com.sprint.socialmeli.entity.User;
import com.sprint.socialmeli.exception.BadRequestException;
import com.sprint.socialmeli.mappers.UserMapper;
import com.sprint.socialmeli.repository.user.IUsersRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    // Order by name unit tests - T-0004 -------------------START
    @Test
    @DisplayName("Order type 'name_desc' returns follower list ordered descending by name")
    public void testOrderFollowersDescending() {
        testFollowersAreCorrectlyOrdered("name_desc");
    }

    @Test
    @DisplayName("Order type 'name_desc' returns follower list ordered descending by name")
    public void testOrderFollowersAscending() {
        testFollowersAreCorrectlyOrdered("name_asc");
    }

    @Test
    @DisplayName("No order type returns follower list in default order")
    public void testOrderFollowersDefaultOrder() {
        testFollowersAreCorrectlyOrdered(null);
    }

    private void testFollowersAreCorrectlyOrdered(String order) {
        //Arrange
        Integer sellerId = 1;
        List<Customer> followersList = new ArrayList<>(List.of(
                new Customer(new User(101, "Matías")),
                new Customer(new User(102, "Nicolas")),
                new Customer(new User(103, "Alfredo"))
        ));

        if (order != null) {
            if (order.equals("name_asc")) {
                followersList.sort(Comparator.comparing(customer -> customer.getUser().getUserName()));
            } else if (order.equals("name_desc")) {
                followersList.sort(Comparator.comparing(customer -> customer.getUser().getUserName(), Comparator.reverseOrder()));
            }
        }

        List<UserResponseDTO> expected = followersList
                .stream()
                .map(UserMapper::mapCustomerToUserResponseDto)
                .toList();


        Seller seller = new Seller(new User(sellerId, "Seller Prueba"));

        Mockito.when(
                usersRepository.findCustomerByPredicate(ArgumentMatchers.any())
        ).thenReturn(followersList);

        Mockito.when(usersRepository.findSellerById(sellerId)).thenReturn(seller);

        //Act
        FollowersResponseDTO followersResponseDTO = usersService.getFollowers(sellerId, order);

        //Assert
        Assertions.assertEquals(expected, followersResponseDTO.getFollowers());
    }

    @Test
    @DisplayName("Order type 'name_desc' returns followed list ordered descending by name")
    public void testOrderFollowedDescending() {
        testFollowedAreCorrectlyOrdered("name_desc");
    }

    @Test
    @DisplayName("No order type returns followed list in default order")
    public void testNoOrderTypeDefaultOrder() {
        testFollowedAreCorrectlyOrdered(null);
    }

    private void testFollowedAreCorrectlyOrdered(String order) {
        //Arrange
        Integer customerId = 101;
        List<Seller> followedList = new ArrayList<>(List.of(
                new Seller(new User(1, "Walter")),
                new Seller(new User(2, "Amanda")),
                new Seller(new User(3, "Francisco"))
        ));

        if (order != null) {
            if (order.equals("name_asc")) {
                followedList.sort(Comparator.comparing(customer -> customer.getUser().getUserName()));
            } else if (order.equals("name_desc")) {
                followedList.sort(Comparator.comparing(customer -> customer.getUser().getUserName(), Comparator.reverseOrder()));
            }
        }

        List<UserResponseDTO> expected = followedList
                .stream()
                .map(UserMapper::mapSellerToUserResponseDto)
                .toList();


        Customer customer = new Customer(new User(customerId, "Customer Prueba"));

        Mockito.when(
                usersRepository.findSellerByPredicate(ArgumentMatchers.any())
        ).thenReturn(followedList);

        Mockito.when(usersRepository.findCustomerById(customerId)).thenReturn(customer);

        //Act
        FollowedResponseDTO followedResponseDTO = usersService.listFollowedUsers(customerId, order);

        //Assert
        Assertions.assertEquals(expected, followedResponseDTO.getFollowed());
    }

    @Test
    @DisplayName("Order type 'name_asc' returns followed list ordered descending by name")
    public void testOrderFollowedAscending() {
        testFollowedAreCorrectlyOrdered("name_asc");
    }
    // Order by name unit tests - T-0004 -------------------END
}