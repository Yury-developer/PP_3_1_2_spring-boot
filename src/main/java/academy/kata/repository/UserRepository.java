package academy.kata.repository;

import academy.kata.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> { // <(класс для кот. реализуем), (тип id)>
    //
}
