package com.xiaozou.liteflow.node;

import com.yomahub.liteflow.core.NodeComponent;
import org.springframework.stereotype.Component;

/**
 * @author : wh
 * @date : 2024/11/11 14:05
 * @description:
 */
@Component("a")
public class ACmp extends NodeComponent {

    @Override
    public void process() {
        //do your business
        System.out.println("a");
    }
}
