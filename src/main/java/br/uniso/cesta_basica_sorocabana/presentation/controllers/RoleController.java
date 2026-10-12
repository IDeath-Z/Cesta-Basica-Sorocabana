package br.uniso.cesta_basica_sorocabana.presentation.controllers;

import java.util.List;
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

import br.uniso.cesta_basica_sorocabana.application.usecases.role.UpdateRoleStatusUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.role.CreateRoleUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.role.FetchRoleUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.role.FetchPermissionsUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.role.FetchRolesUseCase;
import br.uniso.cesta_basica_sorocabana.application.usecases.role.UpdateRoleUseCase;
import br.uniso.cesta_basica_sorocabana.domain.enums.Permission;
import br.uniso.cesta_basica_sorocabana.domain.enums.SortBy;
import br.uniso.cesta_basica_sorocabana.domain.shared.DomainPageRequest;
import br.uniso.cesta_basica_sorocabana.infrastructure.persistence.shared.TransactionalProxy;
import br.uniso.cesta_basica_sorocabana.infrastructure.security.RequiresPermission;
import br.uniso.cesta_basica_sorocabana.infrastructure.security.SecurityUserDetails;
import br.uniso.cesta_basica_sorocabana.presentation.dto.role.PermissionResponse;
import br.uniso.cesta_basica_sorocabana.presentation.dto.role.RoleResponse;
import br.uniso.cesta_basica_sorocabana.presentation.dto.role.CreateRoleRequest;
import br.uniso.cesta_basica_sorocabana.presentation.dto.role.UpdateRoleRequest;
import br.uniso.cesta_basica_sorocabana.presentation.dto.shared.PageResponse;

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
@RequestMapping("/roles")
@RequiresPermission(Permission.ADMIN)
@Tag(name = "Roles", description = "Roles group permissions (e.g. ADMIN, PROFESSOR, ALUNO). Access control never checks the role name, only the permissions it grants. Every route requires permission ADMIN.")
public class RoleController {

    private final CreateRoleUseCase createRoleUseCase;
    private final UpdateRoleUseCase updateRoleUseCase;
    private final FetchRoleUseCase fetchRoleUseCase;
    private final FetchRolesUseCase fetchRolesUseCase;
    private final UpdateRoleStatusUseCase updateRoleStatusUseCase;
    private final FetchPermissionsUseCase fetchPermissionsUseCase;
    private final TransactionalProxy transactionalProxy;

    @GetMapping
    @Operation(summary = "List roles", description = "Returns a page of roles with their permissions, active and inactive. Valid sortBy values: NAME_ASC, NAME_DESC. Requires permission ADMIN.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of roles"),
        @ApiResponse(responseCode = "400", description = "Invalid page, size or sortBy")
    })
    public ResponseEntity<PageResponse<RoleResponse>> list(
            @Parameter(description = "Page number, starting at 0", example = "0") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Items per page, from 1 to 100", example = "20") @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size,
            @Parameter(description = "Sort order", example = "NAME_ASC") @RequestParam(defaultValue = "NAME_ASC") SortBy sortBy) {

        var output = transactionalProxy.executeReadOnly(() -> fetchRolesUseCase.execute(
            new FetchRolesUseCase.Input(new DomainPageRequest(page, size, sortBy))
        ));

        return ResponseEntity.ok(PageResponse.from(output, RoleResponse::from));
    }

    @GetMapping("/permissions")
    @Operation(summary = "List available permissions", description = "Returns every permission defined in the code, to be used when creating or updating roles. Permissions cannot be created through the API. Requires permission ADMIN.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "All permissions")
    })
    public ResponseEntity<List<PermissionResponse>> listPermissions() {
        var output = fetchPermissionsUseCase.execute();

        return ResponseEntity.ok(output.stream().map(PermissionResponse::from).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a role", description = "Returns a single role with its permissions. Requires permission ADMIN.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Role found"),
        @ApiResponse(responseCode = "404", description = "Role not found")
    })
    public ResponseEntity<RoleResponse> get(
            @Parameter(description = "Role ID", example = "1c2d3e4f-5a6b-4c7d-8e9f-0a1b2c3d4e5f") @PathVariable UUID id) {

        var output = transactionalProxy.executeReadOnly(() -> fetchRoleUseCase.execute(new FetchRoleUseCase.Input(id)));

        return ResponseEntity.ok(RoleResponse.from(output));
    }

    @PostMapping
    @Operation(summary = "Create a role", description = "Registers a new active role with the given permissions. Requires permission ADMIN.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Role created"),
        @ApiResponse(responseCode = "400", description = "Invalid payload or unknown permission"),
        @ApiResponse(responseCode = "409", description = "A role with this name already exists")
    })
    public ResponseEntity<RoleResponse> create(@AuthenticationPrincipal SecurityUserDetails userDetails, @RequestBody @Valid CreateRoleRequest request) {
        UUID requesterId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> createRoleUseCase.execute(new CreateRoleUseCase.Input(requesterId, request.name(), request.permissions())));

        return ResponseEntity.status(HttpStatus.CREATED).body(RoleResponse.from(output));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update a role", description = "Changes the name and/or replaces the permissions of a role. Omitted fields keep their value. Users of this role get the new permissions on their next request. The admin role always keeps every permission and cannot be edited. Requires permission ADMIN.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Role updated"),
        @ApiResponse(responseCode = "400", description = "Invalid payload or unknown permission"),
        @ApiResponse(responseCode = "403", description = "User lacks permission ADMIN, or the role is the admin role, which cannot be changed"),
        @ApiResponse(responseCode = "404", description = "Role not found"),
        @ApiResponse(responseCode = "409", description = "Another role already has this name")
    })
    public ResponseEntity<RoleResponse> update(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @Parameter(description = "Role ID", example = "1c2d3e4f-5a6b-4c7d-8e9f-0a1b2c3d4e5f") @PathVariable UUID id,
            @RequestBody @Valid UpdateRoleRequest request) {
        UUID requesterId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> updateRoleUseCase.execute(new UpdateRoleUseCase.Input(requesterId, id, request.name(), request.permissions())));

        return ResponseEntity.ok(RoleResponse.from(output));
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate a role", description = "Marks the role as active again. Does nothing if it is already active. Requires permission ADMIN.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Role is active"),
        @ApiResponse(responseCode = "403", description = "User lacks permission ADMIN, or the role is the admin role, which cannot be changed"),
        @ApiResponse(responseCode = "404", description = "Role not found")
    })
    public ResponseEntity<RoleResponse> activate(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @Parameter(description = "Role ID", example = "1c2d3e4f-5a6b-4c7d-8e9f-0a1b2c3d4e5f") @PathVariable UUID id) {
        UUID requesterId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> updateRoleStatusUseCase.execute(new UpdateRoleStatusUseCase.Input(requesterId, id, true)));

        return ResponseEntity.ok(RoleResponse.from(output));
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate a role", description = "Marks the role as inactive so its users lose every permission until it is activated again, and it can no longer be given to users. The admin role cannot be deactivated. Requires permission ADMIN.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Role is inactive"),
        @ApiResponse(responseCode = "403", description = "User lacks permission ADMIN, or the role is the admin role, which cannot be changed"),
        @ApiResponse(responseCode = "404", description = "Role not found")
    })
    public ResponseEntity<RoleResponse> deactivate(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @Parameter(description = "Role ID", example = "1c2d3e4f-5a6b-4c7d-8e9f-0a1b2c3d4e5f") @PathVariable UUID id) {
        UUID requesterId = userDetails.getUser().getId();

        var output = transactionalProxy.execute(() -> updateRoleStatusUseCase.execute(new UpdateRoleStatusUseCase.Input(requesterId, id, false)));

        return ResponseEntity.ok(RoleResponse.from(output));
    }
}
