package mate.academy.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.user.UserLoginRequestDto;
import mate.academy.bookstore.dto.user.UserLoginResponseDto;
import mate.academy.bookstore.dto.user.UserRegistrationRequest;
import mate.academy.bookstore.dto.user.UserResponseDto;
import mate.academy.bookstore.exception.RegistrationException;
import mate.academy.bookstore.security.AuthenticationService;
import mate.academy.bookstore.service.user.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication management", description = "Endpoints for managing authentication")
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Operation(summary = "New User Registration", description = "Adding a new user")
    @PostMapping("/register")
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequest request)
            throws RegistrationException {
        return userService.register(request);
    }

    @Operation(summary = "Authenticate user",
            description = "Authenticate user by JWT token validation")
    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto loginRequestDto) {
        return authenticationService.authenticate(loginRequestDto);
    }
}
