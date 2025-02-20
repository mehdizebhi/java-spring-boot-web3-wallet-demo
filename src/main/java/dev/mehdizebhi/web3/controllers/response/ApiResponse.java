package dev.mehdizebhi.web3.controllers.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.ProblemDetail;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private T data;
    private ProblemDetail error;
    private String message;
    private Pagination pagination;
}
