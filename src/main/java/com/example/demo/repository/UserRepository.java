package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //User findUserByUsername(String username);
    @Query(value = "SELECT * FROM user WHERE username = :username", nativeQuery = true)
    User findUserByUsername(@Param("username") String username);

    @Query(value = "SELECT * FROM user WHERE email = :?1", nativeQuery = true)
    User findUserByEmail(@Param("email") String email);

    User findUserById(Long id);
    User findById(int id);

    void deleteById(Long aLong);


}
