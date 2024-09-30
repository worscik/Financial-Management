package pl.financemanagement.ApplicationConfig;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void handleMethodArgumentNotValid() {

    }

    @Test
    void handleBindException() {
    }

    @Test
    void handleHttpMessageNotReadableExceptionException() {
    }

    @Test
    void handleMissingServletRequestParameterException() {
    }

    @Test
    void handleEntityNotFoundException() {
    }

    @Test
    void handleDataIntegrityViolationException() {
    }

    @Test
    void handleIllegalArgumentException() {
    }
}