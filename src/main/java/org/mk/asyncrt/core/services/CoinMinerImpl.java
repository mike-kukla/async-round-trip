package org.mk.asyncrt.core.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.mk.asyncrt.pub.server.SseEmitterSubscriber;
import org.mk.asyncrt.sb.rx.DelayingCallable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import rx.Observable;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;

@Service
public class CoinMinerImpl implements CoinMiner {

    @Override
    @Async
    public void mine(final Integer count, final SseEmitter sseEmitter) {
        
        List<Observable<BigDecimal>> observables = new ArrayList<>(); 
        
        for (int i = 0; i < count ; i++) {

            Future<BigDecimal> future = 
               ((ThreadPoolTaskExecutor) this.taskExecutor).submit(new DelayingCallable());
            
            Observable<BigDecimal> observableForBigDecimal = Observable.from(future);
            observables.add(observableForBigDecimal);
        }

        Observable<BigDecimal> merged = Observable.merge(observables);
        merged.subscribe(new SseEmitterSubscriber(sseEmitter));
    }
    
    @Autowired
    private TaskExecutor taskExecutor;
}
