package br.uniso.cesta_basica_sorocabana.presentation.controllers;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import br.uniso.cesta_basica_sorocabana.application.exceptions.BusinessRuleException;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ForbiddenActionException;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceAlreadyExistsException;
import br.uniso.cesta_basica_sorocabana.application.exceptions.ResourceNotFoundException;
import br.uniso.cesta_basica_sorocabana.application.exceptions.UnauthorizedException;
import br.uniso.cesta_basica_sorocabana.presentation.dto.exceptions.ApiErrorResponse;
import br.uniso.cesta_basica_sorocabana.presentation.enums.errors.AccessError;
import br.uniso.cesta_basica_sorocabana.presentation.enums.errors.RequestError;
import br.uniso.cesta_basica_sorocabana.presentation.enums.errors.SystemError;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public final class GlobalExceptionHandler {

    // 400

    @ExceptionHandler({BusinessRuleException.class, IllegalArgumentException.class})
    public ResponseEntity<ApiErrorResponse> handleBadRequest(RuntimeException e, HttpServletRequest request) {
        log.warn("Bad request: {}", e.getMessage());
        return build(HttpStatus.BAD_REQUEST, e.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidBody(MethodArgumentNotValidException e, HttpServletRequest request) {
        String fields = e.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + " " + error.getDefaultMessage())
            .collect(Collectors.joining("; "));

        log.warn("Validation failed: {}", fields);
        return build(HttpStatus.BAD_REQUEST, RequestError.VALIDATION_FAILED.message(fields), request);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidParameters(HandlerMethodValidationException e, HttpServletRequest request) {
        String parameters = e.getParameterValidationResults().stream()
            .map(result -> result.getMethodParameter().getParameterName() + " " + result.getResolvableErrors().getFirst().getDefaultMessage())
            .collect(Collectors.joining("; "));

        log.warn("Parameter validation failed: {}", parameters);
        return build(HttpStatus.BAD_REQUEST, RequestError.VALIDATION_FAILED.message(parameters), request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleUnreadableBody(HttpMessageNotReadableException e, HttpServletRequest request) {
        log.warn("Unreadable body: {}", e.getMessage());
        return build(HttpStatus.BAD_REQUEST, RequestError.INVALID_BODY.message(), request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        log.warn("Invalid parameter {}: {}", e.getName(), e.getValue());
        return build(HttpStatus.BAD_REQUEST, RequestError.INVALID_PARAMETER.message(e.getName()), request);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiErrorResponse> handleMissingParameter(MissingServletRequestParameterException e, HttpServletRequest request) {
        log.warn("Missing parameter: {}", e.getParameterName());
        return build(HttpStatus.BAD_REQUEST, RequestError.MISSING_PARAMETER.message(e.getParameterName()), request);
    }

    // Thrown when sortBy points to a field the listed entity does not have.
    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidSort(PropertyReferenceException e, HttpServletRequest request) {
        log.warn("Invalid sort: {}", e.getMessage());
        return build(HttpStatus.BAD_REQUEST, RequestError.INVALID_SORT.message(), request);
    }

    // 401

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiErrorResponse> handleUnauthorized(UnauthorizedException e, HttpServletRequest request) {
        log.warn("Unauthorized access: {}", e.getMessage());
        return build(HttpStatus.UNAUTHORIZED, e.getMessage(), request);
    }

    // Thrown by the login when the credentials are wrong or the user is deactivated.
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthentication(AuthenticationException e, HttpServletRequest request) {
        log.warn("Authentication failed: {}", e.getMessage());
        String message = e instanceof DisabledException
            ? AccessError.ACCOUNT_DISABLED.message()
            : AccessError.INVALID_CREDENTIALS.message();
        return build(HttpStatus.UNAUTHORIZED, message, request);
    }

    // 403

    @ExceptionHandler(ForbiddenActionException.class)
    public ResponseEntity<ApiErrorResponse> handleForbidden(ForbiddenActionException e, HttpServletRequest request) {
        log.warn("Forbidden action: {}", e.getMessage());
        return build(HttpStatus.FORBIDDEN, e.getMessage(), request);
    }

    // Thrown by @PreAuthorize when the user lacks the required permission.
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDenied(AccessDeniedException e, HttpServletRequest request) {
        log.warn("Permission denied: {}", e.getMessage());
        return build(HttpStatus.FORBIDDEN, AccessError.PERMISSION_DENIED.message(), request);
    }

    // 404

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        log.warn("Resource not found: {}", e.getMessage());
        return build(HttpStatus.NOT_FOUND, e.getMessage(), request);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleRouteNotFound(NoResourceFoundException e, HttpServletRequest request) {
        log.warn("Route not found: {}", request.getRequestURI());
        return build(HttpStatus.NOT_FOUND, RequestError.ROUTE_NOT_FOUND.message(), request);
    }

    // 405

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodNotAllowed(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.warn("Method not allowed: {} {}", e.getMethod(), request.getRequestURI());
        return build(HttpStatus.METHOD_NOT_ALLOWED, RequestError.METHOD_NOT_ALLOWED.message(e.getMethod()), request);
    }

    // 409

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleConflict(ResourceAlreadyExistsException e, HttpServletRequest request) {
        log.warn("Resource already exists: {}", e.getMessage());
        return build(HttpStatus.CONFLICT, e.getMessage(), request);
    }

    // Last line of defense for unique and foreign key constraints (e.g. two requests creating the same name at once).
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrity(DataIntegrityViolationException e, HttpServletRequest request) {
        log.warn("Data integrity violation: {}", e.getMostSpecificCause().getMessage());
        return build(HttpStatus.CONFLICT, RequestError.DATA_CONFLICT.message(), request);
    }

    // 500

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUncaughtException(Exception e, HttpServletRequest request) {
        log.error("Internal server error: {}", e.getMessage(), e);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, SystemError.UNEXPECTED_ERROR.message(), request);
    }

    private ResponseEntity<ApiErrorResponse> build(HttpStatus status, String message, HttpServletRequest request) {
        ApiErrorResponse error = new ApiErrorResponse(
            Instant.now(),
            status.value(),
            status.getReasonPhrase(),
            message,
            request.getRequestURI()
        );
        return ResponseEntity.status(status).body(error);
    }
}
