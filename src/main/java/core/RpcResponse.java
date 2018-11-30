package core;

import java.io.Serializable;

/**
 * Created by wuzichao on 2018/11/30.
 */
public class RpcResponse implements Serializable {
    private Throwable throwable;
    private Object result;

    public Throwable getThrowable() {
        return throwable;
    }

    public RpcResponse setThrowable(Throwable throwable) {
        this.throwable = throwable;
        return this;
    }

    public Object getResult() {
        return result;
    }

    public RpcResponse setResult(Object result) {
        this.result = result;
        return this;
    }
}
