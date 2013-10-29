package com.baobab.android.storycheck.loader;

/**
 * Created with IntelliJ IDEA.
 * User: dirk
 * Date: 2013/09/06
 * Time: 1:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoaderResult<T> {

    T data;
    Exception exception;
    int errString;

    public LoaderResult(Exception e, int errMessage) {
        exception = e;
        errString = errMessage;
    }

    public LoaderResult(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public Exception getException() {
        return exception;
    }

    public int getErrString() {
        return errString;
    }
}

