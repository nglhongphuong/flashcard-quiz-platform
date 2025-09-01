package com.zotriverse.demo.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    CANNOT_FOUND(1009,"Cannot found your information", HttpStatus.NOT_FOUND),
    INVALID_FILE(1010,"Valid file upload",HttpStatus.NOT_FOUND),
    FILE_PROCESSING_ERROR(1011,"File processing error", HttpStatus.PROCESSING),
    INVALID_INPUT(1012,"Invalid input",HttpStatus.BAD_REQUEST),
    INVALID_PARAMS(1013,"Invalid params", HttpStatus.BAD_REQUEST),
    CANNOT_FOUND_FLASHCARD_STUDY(1014,"No flashcards were found in the specified lesson",HttpStatus.NOT_FOUND),
    INVALID_STUDY_MODE(1015, "Invalid study mode", HttpStatus.BAD_REQUEST),
    INVALID_ANSWER_TYPE(1016,"You must select at least one answer type.",  HttpStatus.BAD_REQUEST),
    CANNOT_SEND_MAIL(1017, "Cannot send email", HttpStatus.BAD_REQUEST),
    INVALID_NUM_QUIZ(1018, "The number of flashcards is not valid for creating a quiz",HttpStatus.BAD_REQUEST)

    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}