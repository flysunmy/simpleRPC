package test;

import core.RPC;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by wuzichao on 2018/11/30.
 */
public class Main {
    public static void main(String[] args) {
        RPC.Server build = new RPC.Builder()
                .setProtocol(HashMap.class)
                .setBindAddress("100.0.0.1")
                .setInstance(new Object())
                .setPort(2000)
                .build();
        build.start("test");
    }


    @Test
    public void test() throws IOException {
        Socket socket;
        socket = new Socket();

    }
}
