package com.sd20.backend.repositories;

import com.sd20.backend.utils.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, String> {

    User findByUserAndPassword(String username, String password);

}
