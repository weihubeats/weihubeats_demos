package com.xiaozou.liteflow.services;

import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @author : wh
 * @date : 2024/11/11 14:07
 * @description:
 */
@Component
public class XiaoZouService {


    @Resource
    private FlowExecutor flowExecutor;
    
    
    @PostConstruct
    public void testConfig()throws Exception{
        while (true) {
            TimeUnit.SECONDS.sleep(2);
            LiteflowResponse response = flowExecutor.execute2Resp("chain1", "arg");
            String code = response.getCode();
            System.out.println("response code is " + code);
        }
    }
}
