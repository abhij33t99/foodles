package com.foodles.auth.controller;


import com.foodles.auth.dto.*;
import com.foodles.auth.model.RefreshToken;
import com.foodles.auth.model.User;
import com.foodles.auth.service.AuthService;
import com.foodles.auth.service.JwtService;
import com.foodles.auth.service.RefreshTokenService;
import com.foodles.auth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/v1")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody @Valid RegisterUserDto registerUser) {
        User user = authService.signup(registerUser);

        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginUserDto loginUser) {
        User authenticatedUser = authService.login(loginUser);
        String token = jwtService.generateToken(authenticatedUser);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(loginUser.getEmail());

        return ResponseEntity.ok(TokenDto.builder()
                .token(token)
                .expiresIn(jwtService.getExpirationTime())
                .dto(new RefreshTokenDto(refreshToken.getToken()))
                .build());
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<TokenDto> refreshToken(@RequestBody RefreshTokenRequestDto requestDto) {
        RefreshToken byToken = refreshTokenService.findByToken(requestDto.getToken());
        RefreshToken newRefreshToken = refreshTokenService.verifyToken(byToken);
        return ResponseEntity.ok(
                TokenDto.builder()
                        .token(jwtService.generateToken(newRefreshToken.getUser()))
                        .expiresIn(jwtService.getExpirationTime())
                        .dto(new RefreshTokenDto(newRefreshToken.getToken()))
                        .build()
        );
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.loadUserEntity(authentication.getName());
        refreshTokenService.deleteByUserId(user.getId());
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logged out");
    }
}
