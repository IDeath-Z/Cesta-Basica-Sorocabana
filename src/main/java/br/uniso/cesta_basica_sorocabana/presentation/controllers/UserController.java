package br.uniso.cesta_basica_sorocabana.presentation.controllers;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.uniso.cesta_basica_sorocabana.application.usecases.user.ChangePasswordUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.user.ChangeUserRoleUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.user.UpdateUserStatusUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.user.CreateUserUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.user.FetchCurrentUserUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.user.FetchUserUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.user.FetchUsersUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.user.ResetUserPasswordUseCase;
import br.uniso.cesta_basica_sorocabana.domain.enums.Permission;
import br.uniso.cesta_basica_sorocabana.domain.enums.SortBy;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageRequest;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.shared.TransactionalProxy;
import br.uniso.cesta_basica_sorocabana.infrastructure.security.RequiresPermission;
import br.uniso.cesta_basica_sorocabana.infrastructure.security.SecurityUserDetails;
import br.uniso.cesta_basica_sorocabana.presentation.dto.shared.PageResponse;
import br.uniso.cesta_basica_sorocabana.presentation.dto.user.ChangePasswordRequest;
import br.uniso.cesta_basica_sorocabana.presentation.dto.user.ChangeUserRoleRequest;
import br.uniso.cesta_basica_sorocabana.presentation.dto.user.CreateUserRequest;
import br.uniso.cesta_basica_sorocabana.presentation.dto.user.CurrentUserResponse;
import br.uniso.cesta_basica_sorocabana.presentation.dto.user.ResetPasswordRequest;
import br.uniso.cesta_basica_sorocabana.presentation.dto.user.UserResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "Users", description = "System users. The /me routes only need a valid token; every other route requires permission ADMIN.")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final FetchUserUseCase fetchUserUseCase;
    private final FetchUsersUseCase fetchUsersUseCase;
    private final FetchCurrentUserUseCase fetchCurrentUserUseCase;
    private final ChangeUserRoleUseCase changeUserRoleUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;
    private final ResetUserPasswordUseCase resetUserPasswordUseCase;
    private final UpdateUserStatusUseCase updateUserStatusUseCase;
    private final TransactionalProxy transactionalProxy;

    @GetMapping("/me")
    @Operation(summary = "Get the logged user", description = "Returns the logged user with the role name and the permissions currently in effect. Use it after login to decide what the UI shows.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Logged user")
    })
    public ResponseEntity<CurrentUserResponse> me(@AuthenticationPrincipal SecurityUserDetails userDetails) {
        UUID userId = userDetails.getUser().getId();

        var output = transactionalProxy.executeReadOnly(() -> fetchCurrentUserUseCase.execute(new FetchCurrentUserUseCase.Input(userId)));

        return ResponseEntity.ok(CurrentUserResponse.from(output));
    }

    @PatchMapping("/me/password")
    @Operation(summary = "Change own password", description = "Changes the logged user's password after checking the current one. The token in use stays valid until it expires.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Password changed"),
        @ApiResponse(responseCode = "400", description = "Invalid payload or wrong current password")
    })
    public ResponseEntity<Void> changeOwnPassword(@AuthenticationPrincipal SecurityUserDetails userDetails,
            @RequestBody @Valid ChangePasswordRequest request) {
        UUID userId = userDetails.getUser().getId();

        transactionalProxy.execute(() -> changePasswordUseCase.execute(
            new ChangePasswordUseCase.Input(userId, request.currentPassword(), request.newPassword())
        ));

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @RequiresPermission(Permission.ADMIN)
    @Operation(summary = "List users", description = "Returns a page of users, active and inactive. Valid sortBy values: EMAIL_ASC, EMAIL_DESC, CREATED_AT_ASC, CREATED_AT_DESC. Requires permission ADMIN.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of users"),
        @ApiResponse(responseCode = "400", description = "Invalid page, size or sortBy")
    })
    public ResponseEntity<PageResponse<UserResponse>> list(
            @Parameter(description = "Page number, starting at 0", example = "0") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Items per page, from 1 to 100", example = "20") @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size,
            @Parameter(description = "Sort order", example = "EMAIL_ASC") @RequestParam(defaultValue = "EMAIL_ASC") SortBy sortBy) {

        var output = transactionalProxy.executeReadOnly(() -> fetchUsersUseCase.execute(
            new FetchUsersUseCase.Input(new DomainPageRequest(page, size, sortBy))
        ));

        return ResponseEntity.ok(PageResponse.from(output, UserResponse::from));
    }

    @GetMapping("/{id}")
    @RequiresPermission(Permission.ADMIN)
    @Operation(summary = "Get a user", description = "Returns a single user by its ID. Requires permission ADMIN.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User found"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResponse> get(
            @Parameter(description = "User ID", example = "8f7e6d5c-4b3a-4291-8f7e-6d5c4b3a2918") @PathVariable UUID id) {

        var output = transactionalProxy.executeReadOnly(() -> fetchUserUseCase.execute(new FetchUserUseCase.Input(id)));

        return ResponseEntity.ok(UserResponse.from(output));
    }

    @PostMapping
    @RequiresPermission(Permission.ADMIN)
    @Operation(summary = "Create a user", description = "Registers a new active user with an initial password and an active role. Requires permission ADMIN.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "User created"),
        @ApiResponse(responseCode = "400", description = "Invalid payload or inactive role"),
        @ApiResponse(responseCode = "404", description = "Role not found"),
        @ApiResponse(responseCode = "409", description = "E-mail already registered")
    })
    public ResponseEntity<UserResponse> create(@AuthenticationPrincipal SecurityUserDetails userDetails, @RequestBody @Valid CreateUserRequest request) {
        UUID requesterId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> createUserUseCase.execute(
            new CreateUserUseCase.Input(requesterId, request.email(), request.password(), request.roleId())
        ));

        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(output));
    }

    @PatchMapping("/{id}/role")
    @RequiresPermission(Permission.ADMIN)
    @Operation(summary = "Change a user's role", description = "Moves the user to another active role. The new permissions apply on the user's next request. Requires permission ADMIN.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Role changed"),
        @ApiResponse(responseCode = "400", description = "Invalid payload or inactive role"),
        @ApiResponse(responseCode = "404", description = "User or role not found")
    })
    public ResponseEntity<UserResponse> changeRole(@AuthenticationPrincipal SecurityUserDetails userDetails,
            @Parameter(description = "User ID", example = "8f7e6d5c-4b3a-4291-8f7e-6d5c4b3a2918") @PathVariable UUID id,
            @RequestBody @Valid ChangeUserRoleRequest request) {
        UUID requesterId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> changeUserRoleUseCase.execute(
            new ChangeUserRoleUseCase.Input(requesterId, id, request.roleId())
        ));

        return ResponseEntity.ok(UserResponse.from(output));
    }

    @PatchMapping("/{id}/password")
    @RequiresPermission(Permission.ADMIN)
    @Operation(summary = "Reset a user's password", description = "Sets a new password for another user without asking for the current one, e.g. when it was forgotten. The audit keeps who reset it, never the password. Requires permission ADMIN.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Password reset"),
        @ApiResponse(responseCode = "400", description = "Invalid payload"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Void> resetPassword(@AuthenticationPrincipal SecurityUserDetails userDetails,
            @Parameter(description = "User ID", example = "8f7e6d5c-4b3a-4291-8f7e-6d5c4b3a2918") @PathVariable UUID id,
            @RequestBody @Valid ResetPasswordRequest request) {
        UUID requesterId = userDetails.getUser().getId();

        transactionalProxy.execute(() -> resetUserPasswordUseCase.execute(
            new ResetUserPasswordUseCase.Input(requesterId, id, request.newPassword())
        ));

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    @RequiresPermission(Permission.ADMIN)
    @Operation(summary = "Activate a user", description = "Lets the user log in again. Does nothing if the user is already active. Requires permission ADMIN.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User is active"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResponse> activate(@AuthenticationPrincipal SecurityUserDetails userDetails,
            @Parameter(description = "User ID", example = "8f7e6d5c-4b3a-4291-8f7e-6d5c4b3a2918") @PathVariable UUID id) {
        UUID requesterId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> updateUserStatusUseCase.execute(
            new UpdateUserStatusUseCase.Input(requesterId, id, true)
        ));

        return ResponseEntity.ok(UserResponse.from(output));
    }

    @PatchMapping("/{id}/deactivate")
    @RequiresPermission(Permission.ADMIN)
    @Operation(summary = "Deactivate a user", description = "Blocks the user from logging in and invalidates their current token on the next request. A user cannot deactivate themselves. Requires permission ADMIN.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User is inactive"),
        @ApiResponse(responseCode = "400", description = "Tried to deactivate the logged user"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResponse> deactivate(@AuthenticationPrincipal SecurityUserDetails userDetails,
            @Parameter(description = "User ID", example = "8f7e6d5c-4b3a-4291-8f7e-6d5c4b3a2918") @PathVariable UUID id) {
        UUID requesterId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> updateUserStatusUseCase.execute(
            new UpdateUserStatusUseCase.Input(requesterId, id, false)
        ));

        return ResponseEntity.ok(UserResponse.from(output));
    }
}
