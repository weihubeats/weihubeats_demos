package com.spring.boot.duubo.consumer;

import java.util.List;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.filter.ClusterFilter;

/**
 * @author : wh
 * @date : 2024/1/15 15:41
 * @description:
 */
@Activate(group = {CommonConstants.CONSUMER})
public class TagFilter implements ClusterFilter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String tag = RpcContext.getServiceContext().getAttachment("tag");
        List<Invoker<?>> invokers = RpcContext.getServiceContext().getInvokers();
        for (Invoker<?> provider : invokers) {
            String providerTag = provider.getUrl().getParameter("tag");
            if (tag != null && tag.equals(providerTag)) {
                return provider.invoke(invocation);
            }
        }
        return invoker.invoke(invocation);

    }
}
