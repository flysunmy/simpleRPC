package core;

import annotation.RPCService;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wuzichao on 2018/11/30.
 */
public class RPC {

    private static Map<String, Object> service = new HashMap<>();



    public static class Builder {
        private Class<?> protocol = null;
        private Object instance = null;
        private String bindAddress = "0.0.0.0";
        private int port = 0;


        public Builder() {
        }

        public Class<?> getProtocol() {
            return protocol;
        }

        public Builder setProtocol(Class<?> protocol) {
            this.protocol = protocol;
            return this;
        }

        public Object getInstance() {
            return instance;
        }

        public Builder setInstance(Object instance) {
            this.instance = instance;
            return this;
        }

        public String getBindAddress() {
            return bindAddress;
        }

        public Builder setBindAddress(String bindAddress) {
            this.bindAddress = bindAddress;
            return this;
        }

        public int getPort() {
            return port;
        }

        public Builder setPort(int port) {
            this.port = port;
            return this;
        }


        public Server build() {
            return new Server(protocol, instance, bindAddress, port);
        }
    }

    public static class Server {
        private Class<?> protocol = null;
        private Object instance = null;
        private String bindAddress = "0.0.0.0";
        private int port = 0;

        public Server(Class<?> protocol, Object instance, String bindAddress, int port) {
            this.protocol = protocol;
            this.instance = instance;
            this.bindAddress = bindAddress;
            this.port = port;
        }

        public void start(String packageName) {
            Map<String, Object> services = getService(packageName);
            try (ServerSocket server = new ServerSocket(this.port)) {
                while (true) {
                    Socket socket = server.accept();
                    services.forEach((k, v) -> {
                        System.out.println(k + "," + v);
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public static Map<String,Object> getService(String packageName) {
        try {
            List<Class<?>> classes = getClasses(packageName);
            for (Class<?> clazz : classes) {
                Object object = clazz.newInstance();
                service.put(clazz.getAnnotation(RPCService.class).value().getName(), object);
            }
            return service;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    private static List<Class<?>> getClasses(String packageName) {
        List<Class<?>> classes = new ArrayList<>();
        File directory = null;
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader == null) {
                throw new ClassNotFoundException("can not get classLoader");
            }

            String path = packageName.replaceAll("\\.", "/");
            URL resource = classLoader.getResource(path);
            directory = new File(resource.getFile());
            if (directory.exists()) {
                String[] list = directory.list();
                File[] files = directory.listFiles();
                int count = files.length;
                for (int i = 0; i < count; i++) {
                    File file = files[i];
                    if (file.isFile() && file.getName().endsWith(".class")) {
                        String clsName = list[i].substring(0, list[i].length() - 6);
                        Class<?> aClass = Class.forName(packageName + "." + clsName);
                        if (aClass.getAnnotation(RPCService.class) != null) {
                            classes.add(aClass);
                        }
                    } else {
                        List<Class<?>> classesLi = getClasses(packageName + "." + file.getName());
                        classes.addAll(classesLi);
                    }

                }
            }

            return classes;

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }



}
