package com.server.contestControl.authServer.service.user;

import com.server.contestControl.authServer.entity.User;
import com.server.contestControl.authServer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

}
