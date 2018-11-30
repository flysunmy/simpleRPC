package core;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by wuzichao on 2018/11/30.
 */
public class RpcClient {
    public Object start(RpcRequest request, String host, int port) {
        InputStream in = null;
        ObjectInputStream oin = null;
        OutputStream out = null;
        ObjectOutputStream oout =null;
        Socket server = null;
        try {
            server = new Socket(host, port);
            in = server.getInputStream();
            oin = new ObjectInputStream(in);
            out = server.getOutputStream();
            oout = new ObjectOutputStream(out);
            RpcResponse response = null;
            Object object = oin.readObject();
            if (!(object instanceof RpcResponse)) {
                throw new RuntimeException("response params is not correct");
            } else {
                response = (RpcResponse) object;
            }
            if (response.getThrowable() != null) {
                throw response.getThrowable();
            }
            return response.getResult();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (in != null) in.close();
                if (oin != null) oin.close();
                if (out != null) out.close();
                if (oout != null) oout.close();
                if (server != null) server.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return null;
    }
}
