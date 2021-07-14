package com.balv.imdb.domain.models;

public class ApiResult<T> {
    private boolean     success;
    private T           result;
    private ErrorResult error;

    public ApiResult(T result) {
        this.success = true;
        this.result = result;
    }

    public ApiResult(ErrorResult error) {
        this.success = false;
        this.error = error;
    }

    public ErrorResult getError() {
        if (!success) {
            return error;
        } else {
            throw new IllegalStateException("Called getError on a valid ApiResult");
        }
    }
    public boolean isSuccess(){
        return success;
    }
}
