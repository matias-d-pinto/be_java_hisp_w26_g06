package com.sprint.socialmeli.repository.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.socialmeli.entity.Customer;
import com.sprint.socialmeli.entity.Seller;
import com.sprint.socialmeli.entity.User;
import com.sprint.socialmeli.exception.ConflictException;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Repository
public class UsersRepositoryImpl implements IUsersRepository{
    private List<Customer> customerList = new ArrayList<>();
    private List<Seller> sellerList = new ArrayList<>();

    public UsersRepositoryImpl() {
        loadDatabase("Customer");
        loadDatabase("Seller");
    }

    private void loadDatabase(String userType) {
        try{
            File file = ResourceUtils.getFile("classpath:static/" + userType + ".json");
            ObjectMapper objectMapper = new ObjectMapper();
            List<User> users = objectMapper.readValue(
                    file, new TypeReference<List<User>>(){}
            );
            switch (userType){
                case "Customer":
                    customerList = users.stream().map(Customer::new).toList();
                case "Seller":
                    sellerList = users.stream().map(Seller::new).toList();
            }
        } catch (FileNotFoundException e) {
            throw new ConflictException(e.getMessage());
            //System.out.println("Archivo no encontrado: " + e.getMessage());
        } catch (IOException e) {
            //throw new RuntimeException(e);
            throw new ConflictException(e.getMessage());
            //System.out.println("Error en el manejo del archivo: " + e.getMessage());
        }
    }


    @Override
    public List<Customer> findCustomerByPredicate(Predicate<Customer> predicate) {
        return customerList.stream().filter(predicate).toList();
    }

    @Override
    public List<Seller> findSellerByPredicate(Predicate<Seller> predicate) {
        return sellerList.stream().filter(predicate).toList();
    }
}