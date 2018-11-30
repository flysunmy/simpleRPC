package test.anno;

import annotation.RPCService;

/**
 * Created by wuzichao on 2018/11/30.
 */
@RPCService(CommonService.class)
public class CommonService {
    public int sum(int a, int b) {
        return a + b;
    }
}
