package com.xfl.boot.filter;

import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import org.slf4j.MDC;

/**
 * Created by XFL
 * time on 2018/8/29 0:08
 * description:
 */
public class DubboFilter implements Filter {
    private static final String key = "trace_id";

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
            String id = MDC.get(key);
            RpcContext.getContext().setAttachment("TRACE_ID", id);
            return invoker.invoke(invocation);
        } finally {

        }
    }
}
