package com.alura.restapiforohubchallenge.domain.login.user;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.alura.restapiforohubchallenge.exceptions.exceptions.ValidationException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserEntity findById(Long idUser) {
        if (!userRepository.existsById(idUser)) {
            throw new ValidationException("This user does not exists.");
        }
        return userRepository.getReferenceById(idUser);
    }
}
