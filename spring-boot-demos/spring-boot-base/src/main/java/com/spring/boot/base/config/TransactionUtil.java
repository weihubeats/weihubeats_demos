package com.spring.boot.base.config;

import java.util.HashSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * @author : wh
 * @date : 2023/11/16 10:42
 * @description:
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionUtil {

    private final PlatformTransactionManager transactionManager;

    public boolean transact(Runnable runnable) {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            runnable.run();
            transactionManager.commit(status);
            return true;
        } catch (Exception e) {
            transactionManager.rollback(status);
            log.error("transcation commit error ", e);
            return false;
        }

    }

    

}
