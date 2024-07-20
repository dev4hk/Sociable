package com.example.user.repository;

import com.example.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u from User u WHERE :id != u.id AND (u.firstname like %:query% OR u.lastname like %:query% OR u.email like %:query%)")
    List<User> findOtherUsers(@Param("id") Integer id, @Param("query") String query);

    @Query("SELECT u from User u WHERE u.id in :followings")
    List<User> getFollowings(@Param("followings") Set<Integer> followings);

    @Query("SELECT u from User u WHERE u.id in :followers")
    List<User> getFollowers(@Param("followers") Set<Integer> followers);
}
