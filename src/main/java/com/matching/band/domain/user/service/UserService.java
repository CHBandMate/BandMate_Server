package com.matching.band.domain.user.service;

import com.matching.band.domain.user.dto.SignUpRequest;
import com.matching.band.domain.user.entity.UserEntity;
import com.matching.band.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void signUp(SignUpRequest signUpRequest) {
//        userRepository.signUp();
    }


}
