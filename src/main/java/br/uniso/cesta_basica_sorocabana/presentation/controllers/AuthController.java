package br.uniso.cesta_basica_sorocabana.presentation.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.uniso.cesta_basica_sorocabana.application.usecases.auth.ProcessUserLoginUseCase;
import br.uniso.cesta_basica_sorocabana.domain.shared.AuthToken;
import br.uniso.cesta_basica_sorocabana.infrastructure.security.SpringSecurityAuthentication;
import br.uniso.cesta_basica_sorocabana.presentation.dto.auth.LoginRequest;
import br.uniso.cesta_basica_sorocabana.presentation.dto.auth.LoginResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Login. The returned token goes in the Authorization header of every other route as 'Bearer <token>'.")
public final class AuthController {

    private final SpringSecurityAuthentication springSecurityAuthentication;
    private final ProcessUserLoginUseCase processUserLoginUseCase;

    @PostMapping("/login")
    @SecurityRequirements
    @Operation(summary = "Log in", description = "Checks the e-mail and password and returns a JWT access token. There is no refresh token: log in again when it expires. Public route.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Logged in"),
        @ApiResponse(responseCode = "400", description = "Invalid payload"),
        @ApiResponse(responseCode = "401", description = "Wrong e-mail or password, or the user is deactivated")
    })
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        var user = springSecurityAuthentication.authenticate(request.email(), request.password());

        AuthToken token = processUserLoginUseCase.execute(user);

        return ResponseEntity.ok(new LoginResponse(user.getId(), token.accessToken()));
    }
}
