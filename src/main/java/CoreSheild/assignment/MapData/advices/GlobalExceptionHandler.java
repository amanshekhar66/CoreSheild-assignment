package CoreSheild.assignment.MapData.advices;

import CoreSheild.assignment.MapData.exceptions.ResourceNotFound;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.logging.Logger;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFound.class)
    private ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFound e){
        ApiError apiError = ApiError.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .errMsg(e.getLocalizedMessage())
                .build();
        return new ResponseEntity<>(apiError,apiError.getHttpStatus());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<ApiError> handleInvalidMethodArgsException(MethodArgumentNotValidException e){
        ApiError apiError = ApiError.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .errMsg(e.getLocalizedMessage())
                .build();
        return new ResponseEntity<>(apiError,apiError.getHttpStatus());
    }
    @ExceptionHandler(Exception.class)
    private ResponseEntity<ApiError> handleAllOtherException(Exception e){
        ApiError apiError = ApiError.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .errMsg(e.getLocalizedMessage())
                .build();

        return new ResponseEntity<>(apiError,apiError.getHttpStatus());
    }
}
