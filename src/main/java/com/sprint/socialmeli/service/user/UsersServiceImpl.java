package com.sprint.socialmeli.service.user;

import com.sprint.socialmeli.dto.user.FollowersResponseDTO;
import com.sprint.socialmeli.dto.user.UserResponseDTO;
import com.sprint.socialmeli.dto.user.FollowedResponseDTO;
import com.sprint.socialmeli.entity.Customer;
import com.sprint.socialmeli.entity.Seller;
import com.sprint.socialmeli.repository.user.IUsersRepository;
import com.sprint.socialmeli.exception.*;
import com.sprint.socialmeli.dto.user.FollowerCountResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class UsersServiceImpl implements IUsersService{

    private IUsersRepository _usersRepository;

    public UsersServiceImpl(IUsersRepository usersRepository) {
        this._usersRepository = usersRepository;
    }

    @Override
    public void follow(Integer customerId, Integer sellerId) {
        Customer customer = checkAndGetUser(customerId, sellerId);

        if (customer.getFollowed().stream().anyMatch(f -> f.equals(sellerId))){
            throw new ConflictException("The user already follows the seller: " + sellerId);
        }

        customer.follow(sellerId);
    }

    private Customer checkAndGetUser(Integer customerId, Integer sellerId) {
        List<Customer> customer = _usersRepository
                .findCustomerByPredicate(c -> c.getUser().getUserId().equals(customerId));

        List<Seller> seller = _usersRepository
                .findSellerByPredicate(s -> s.getUser().getUserId().equals(sellerId));

        if (customer.isEmpty()){
            throw new NotFoundException("Customer with ID: " + customerId + " not found");
        }

        if (seller.isEmpty()){
            throw new NotFoundException("Seller with ID: " + sellerId + " not found");
        }

        return customer.get(0);
    }

    @Override
    public void unfollow(Integer userId, Integer userIdToUnfollow) {
        Customer customer = checkAndGetUser(userId, userIdToUnfollow);

        if(customer.getFollowed().stream().noneMatch(f -> f.equals(userIdToUnfollow))){
            throw new BadRequestException("The user " + userId + " doesn't follow the seller: " + userIdToUnfollow);
        }


        customer.unfollow(userIdToUnfollow);
    }

    @Override
    public FollowedResponseDTO listFollowedUsers(Integer userId) {
        List<Customer> customers = _usersRepository
                .findCustomerByPredicate(c -> c.getUser().getUserId().equals(userId));

        if (customers.isEmpty()) {
            throw new NotFoundException("Customer with ID: " + userId + " not found");
        }

        Customer customer = customers.get(0);
        if(customer.getFollowed().isEmpty()) {
            throw new NotFoundException("Customer with ID: " + userId + " has not followed any seller");
        }
        List<Seller> followedSellers = _usersRepository
                .findSellerByPredicate(s -> customer.getFollowed().contains(s.getUser().getUserId()));

        List<UserResponseDTO> followed = followedSellers
                .stream()
                .map(s -> new UserResponseDTO(s.getUser().getUserId(), s.getUser().getUserName()))
                .collect(Collectors.toList());

        return new FollowedResponseDTO(customer.getUser().getUserId(), customer.getUser().getUserName(),followed);
    }

    @Override
    public FollowersResponseDTO getfollowers(Integer sellerId) {

        List<Seller> seller = _usersRepository
                .findSellerByPredicate(s -> s.getUser().getUserId().equals(sellerId));

        if (seller.isEmpty()){
            throw new NotFoundException("Seller with ID: " + sellerId + " not found");
        }

        List<Customer> followers = _usersRepository.findCustomerByPredicate( c -> c.getFollowed().contains(sellerId) );
        List<UserResponseDTO> usersDto = followers.stream().map( f -> new UserResponseDTO( f.getUser().getUserId(), f.getUser().getUserName()) ).toList();

        String sellerName = seller.get(0).getUser().getUserName();

        return new FollowersResponseDTO( sellerId, sellerName, usersDto );
    }


    public FollowerCountResponseDTO getFollowersCount(Integer sellerId) {
        Seller seller = _usersRepository
                .findSellerByPredicate(s -> s.getUser().getUserId().equals(sellerId))
                .stream().findFirst()
                .orElseThrow( () -> new NotFoundException("Seller with ID: " + sellerId + " not found"));

        Integer followersCount = (int) _usersRepository.findCustomerByPredicate(customer ->
                        customer.getFollowed()
                                .stream()
                                .anyMatch(s -> s.equals(sellerId)))
                        .stream().count();

        FollowerCountResponseDTO followerCount = new FollowerCountResponseDTO(
                sellerId,
                seller.getUser().getUserName(),
                followersCount
        );

        return followerCount;

    }
}
