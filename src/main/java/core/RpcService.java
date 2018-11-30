package core;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wuzichao on 2018/11/30.
 */
public class RpcService implements Runnable {
    private Socket socket;
    private Map<String, Object> services = new HashMap<>();

    public RpcService(Socket socket, Map<String, Object> service) {
        this.socket = socket;
        this.services = service;
    }

    @Override
    public void run() {
        InputStream in = null;
        ObjectInputStream oin =  null;
        OutputStream out = null;
        ObjectOutputStream oout = null;

        try {
            in = socket.getInputStream();
            oin = new ObjectInputStream(in);
            out = socket.getOutputStream();
            oout = new ObjectOutputStream(out);
            Object param = oin.readObject();
            RpcRequest request = null;
            RpcResponse response = null;
            if (!(param instanceof RpcRequest)) {
                response.setThrowable(new Exception("params is not correct"));
                oout.writeObject(response);
                oout.flush();
                return;
            } else {
                request = (RpcRequest) param;
            }
            Object service = services.get(request.getClassName());
            Class<?> aClass = service.getClass();
            Method method = aClass.getMethod(request.getMethodName(), request.getParamTypes());
            Object result = method.invoke(service, request.getParams());
            response.setResult(result);
            oout.writeObject(response);
            oout.flush();
            return;

        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) in.close();
                if (oin != null) oin.close();
                if (out != null) out.close();
                if (oout != null) oout.close();
                if (socket != null) socket.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

    }
}
