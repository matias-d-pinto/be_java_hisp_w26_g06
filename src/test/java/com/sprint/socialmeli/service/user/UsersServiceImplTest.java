package com.sprint.socialmeli.service.user;

import com.sprint.socialmeli.dto.user.FollowerCountResponseDTO;
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

import java.util.List;

import static com.sprint.socialmeli.mappers.UserMapper.mapUserToFollowerCountDto;
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

    @Test
    @DisplayName("Get correct followers count")
    public void geFollowersCountTest(){

        //Arrange
        //SETUP --> modularizar
        Integer sellerId = 1;
        Seller seller = new Seller(new User(sellerId, "Tesla X"));

        List<Customer> customerList = List.of(
                new Customer(new User(101, "Matias")),
                new Customer(new User(102, "Nicolas"))
        );

        List<UserResponseDTO> usersDto = customerList
                .stream()
                .map(UserMapper::mapCustomerToUserResponseDto)
                .toList();
        //END SETUP

        Mockito.when(usersRepository.findSellerById(sellerId)).thenReturn(seller);
        Mockito.when(
                usersRepository.findCustomerByPredicate(ArgumentMatchers.any())
        ).thenReturn(customerList);
        //Act
        FollowerCountResponseDTO countDTO = usersService.getFollowersCount(sellerId);

        FollowerCountResponseDTO expected = mapUserToFollowerCountDto(seller, 2);

        //Assert
        Assertions.assertEquals( expected, countDTO );
    }
}