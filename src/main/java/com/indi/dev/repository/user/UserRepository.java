package com.indi.dev.repository.user;

import com.indi.dev.entity.SocialType;
import com.indi.dev.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySocialIdAndSocialType(String socialId, SocialType socialType);

    Optional<User> findByRefreshToken(String refreshToken);

    Optional<User> findBySocialTypeAndEmail(SocialType socialType, String email);

    Optional<User> findByNickName(String nickName);
}
