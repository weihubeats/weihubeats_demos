package com.spring.boot.base.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * @author : wh
 * @date : 2023/12/11 19:34
 * @description:
 */
@RestController()
@RequestMapping("/long")
public class LongTrainingController {

    public static Map<HttpServletRequest, DeferredResult<String>> requestMap = new ConcurrentHashMap<>();

    @GetMapping("/handleReqDefResult")
    public DeferredResult<String> handleReqDefResult(HttpServletRequest req) {
        DeferredResult<String> deferredResult = new DeferredResult<>(5000L);
        deferredResult.onTimeout(() -> {
            //超时移除元素.
            requestMap.remove(req);
            System.out.println("当前map元素个数：" + requestMap.size());
        });
        requestMap.put(req, deferredResult);
        Function<Integer, Function<Integer, Function<Integer, Integer>>> currying = x -> y -> z -> (x + y) * z;
        System.out.println(currying.apply(4).apply(5).apply(6)); //54
        return deferredResult;
    }
}
