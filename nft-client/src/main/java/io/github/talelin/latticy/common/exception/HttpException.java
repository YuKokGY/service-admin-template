package io.github.talelin.latticy.common.exception;

import io.github.talelin.latticy.common.constant.BaseResponse;
import io.github.talelin.latticy.common.constant.Code;
import org.springframework.http.HttpStatus;

public class HttpException extends RuntimeException implements BaseResponse {
    private static final long serialVersionUID = 2359767895161832954L;
    protected int httpCode;
    protected int code;
    protected boolean messageOnly;

    public HttpException() {
        super(Code.INTERNAL_SERVER_ERROR.getDescription());
        this.httpCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.code = Code.INTERNAL_SERVER_ERROR.getCode();
        this.messageOnly = false;
    }

    public HttpException(String message) {
        super(message);
        this.httpCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.code = Code.INTERNAL_SERVER_ERROR.getCode();
        this.messageOnly = false;
        this.messageOnly = true;
    }

    public HttpException(int code) {
        super(Code.INTERNAL_SERVER_ERROR.getDescription());
        this.httpCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.code = Code.INTERNAL_SERVER_ERROR.getCode();
        this.messageOnly = false;
        this.code = code;
    }

    public HttpException(int code, int httpCode) {
        super(Code.INTERNAL_SERVER_ERROR.getDescription());
        this.httpCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.code = Code.INTERNAL_SERVER_ERROR.getCode();
        this.messageOnly = false;
        this.httpCode = httpCode;
        this.code = code;
    }

    public HttpException(String message, int code) {
        super(message);
        this.httpCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.code = Code.INTERNAL_SERVER_ERROR.getCode();
        this.messageOnly = false;
        this.code = code;
    }

    public HttpException(String message, int code, int httpCode) {
        super(message);
        this.httpCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.code = Code.INTERNAL_SERVER_ERROR.getCode();
        this.messageOnly = false;
        this.httpCode = httpCode;
        this.code = code;
    }

    public HttpException(Throwable cause, int code) {
        super(cause);
        this.httpCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.code = Code.INTERNAL_SERVER_ERROR.getCode();
        this.messageOnly = false;
        this.code = code;
    }

    public HttpException(Throwable cause, int code, int httpCode) {
        super(cause);
        this.httpCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.code = Code.INTERNAL_SERVER_ERROR.getCode();
        this.messageOnly = false;
        this.code = code;
        this.httpCode = httpCode;
    }

    public HttpException(String message, Throwable cause) {
        super(message, cause);
        this.httpCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.code = Code.INTERNAL_SERVER_ERROR.getCode();
        this.messageOnly = false;
    }

    public Throwable doFillInStackTrace() {
        return super.fillInStackTrace();
    }

    public int getHttpCode() {
        return this.httpCode;
    }

    public int getCode() {
        return this.code;
    }

    public boolean isMessageOnly() {
        return this.messageOnly;
    }
}