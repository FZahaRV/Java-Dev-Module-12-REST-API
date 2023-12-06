package com.goit.users;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class UserService {
    public final UserRepository repository;
    public User findByUsername(String username) {
        Optional<User> user = repository.findById(username);
        if (user.isEmpty()) {
            return null;
        }
        return user.get();
    }
}
