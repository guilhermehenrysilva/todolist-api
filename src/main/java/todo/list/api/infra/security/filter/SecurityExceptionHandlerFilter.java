package todo.list.api.infra.security.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import todo.list.api.infra.security.ErrorMessageResponse;

import java.io.IOException;

@Slf4j
@Component
public class SecurityExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        try {
            filterChain.doFilter(request, response);
        } catch (AccessDeniedException e) {
            log.error("[SecurityExceptionHandlerFilter] [403] Forbidden Access. " + e.getMessage() + " | Request URI: " + method + " - " + requestURI);
            ErrorMessageResponse errorResponse = buildErrorMessage(e.getMessage(), HttpStatus.FORBIDDEN);
            this.sendResponse(response, errorResponse);
        } catch (Exception e) {
            log.error("[SecurityExceptionHandlerFilter] [500] Internal Server Error. " + e.getMessage() + " | Request URI: " + method + " - " + requestURI);
            ErrorMessageResponse errorResponse = buildErrorMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            this.sendResponse(response, errorResponse);
        }
    }

    private static ErrorMessageResponse buildErrorMessage(String errorMessage, HttpStatus httpStatus) {
        return ErrorMessageResponse.builder()
                .message(errorMessage)
                .status(httpStatus.value())
                .build();
    }

    private void sendResponse(HttpServletResponse response, ErrorMessageResponse errorResponse) throws IOException {
        response.setStatus(errorResponse.status());
        response.getWriter().write(convertObjectToJson(errorResponse));
    }

    private String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}