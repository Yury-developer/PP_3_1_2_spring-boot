package academy.kata.service;

import academy.kata.model.User;

import java.util.List;


public interface UserService {

    void saveUser(User user);

    User findById(Long id);

    List<User> findAll();

    void update(User user);

    void deleteById(Long id);


    void generateTestData();
}
