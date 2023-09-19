package com.indi.dev.repository.follow;

import com.indi.dev.entity.Follow;
import com.indi.dev.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<User> findByFollowingUser(User user);

    Optional<Follow> findByFollowingUserAndFollowedUser(User followingUser, User followedUser);

}
