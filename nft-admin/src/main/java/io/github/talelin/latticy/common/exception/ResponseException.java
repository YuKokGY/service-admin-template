package io.github.talelin.latticy.common.exception;

import io.github.talelin.autoconfigure.bean.Code;
import io.github.talelin.autoconfigure.exception.HttpException;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class ResponseException extends HttpException {

    private static final long serialVersionUID = 3437368397374839983L;

    protected int httpCode = HttpStatus.OK.value();

    protected int code = Code.PARAMETER_ERROR.getCode();

    private Map<String, Object> errors = new HashMap<>();

    public ResponseException() {
        super(Code.PARAMETER_ERROR.getDescription(), Code.PARAMETER_ERROR.getCode());
    }

    public ResponseException(String message) {
        super(message);
    }

    public ResponseException(String message, int code) {
        super(message, code);
        this.code = code;
    }

    public ResponseException(int code) {
        super(Code.PARAMETER_ERROR.getDescription(), code);
        this.code = code;
    }

    public ResponseException(Map<String, Object> errors) {
        this.errors = errors;
    }

    public ResponseException addError(String key, Object val) {
        this.errors.put(key, val);
        return this;
    }

    @Override
    public String getMessage() {
        if (errors.isEmpty()) {
            return super.getMessage();
        }
        return errors.toString();
    }

    @Override
    public int getHttpCode() {
        return httpCode;
    }

    @Override
    public int getCode() {
        return code;
    }
}
