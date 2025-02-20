package dev.mehdizebhi.web3.controllers.advice;

import dev.mehdizebhi.web3.controllers.response.ApiResponse;
import dev.mehdizebhi.web3.exceptions.ApiBasicException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiBasicException.class)
    public ResponseEntity<ApiResponse<Void>> handleApiException(HttpServletRequest request, ApiBasicException ex) {
        final ProblemDetail error = ex.getBody();
        error.setType(URI.create(request.getRequestURI()));
        error.setProperties(Map.of("message", ex.getKind().getMessage(), "kind", ex.getKind().name()));
        return ResponseEntity.status(ex.getKind().getStatus()).body(ApiResponse.<Void>builder().error(error).build());
    }
}
