package com.sprint.socialmeli.service.user;

import com.sprint.socialmeli.entity.Customer;
import com.sprint.socialmeli.entity.Seller;
import com.sprint.socialmeli.repository.user.IUsersRepository;
import com.sprint.socialmeli.exception.*;
import com.sprint.socialmeli.dto.user.FollowerCountResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UsersServiceImpl implements IUsersService{

    private IUsersRepository _usersRepository;

    public UsersServiceImpl(IUsersRepository usersRepository) {
        this._usersRepository = usersRepository;
    }

    @Override
    public void follow(Integer customerId, Integer sellerId) {
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

        if (customer.get(0).getFollowed().stream().anyMatch(f -> f.equals(sellerId))){
            throw new ConflictException("The user already follows the seller: " + sellerId);
        }

        customer.get(0).follow(sellerId);
    }

    @Override
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
