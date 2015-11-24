package org.mk.asyncrt.pub.server;

import java.io.IOException;
import java.math.BigDecimal;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import rx.Observer;

public class SseEmitterSubscriber implements Observer<BigDecimal> {
    
    @Override
    public void onCompleted() {
        
        System.out.printf("Signal to Subscriber: the observables have been drained. "
           + "We're done on thread:  %d = %s.\n", Thread.currentThread().getId(), 
           Thread.currentThread().getName());
        
        this.sseEmitter.complete();
    }

    @Override
    public void onError(Throwable throwable) {
        this.sseEmitter.completeWithError(throwable); 
    }

    @Override
    public void onNext(BigDecimal bd) {
        
           System.out.printf("Got %s from thread; %d = %s\n", 
               bd.toPlainString(), Thread.currentThread().getId(), 
               Thread.currentThread().getName());

            try {
                
                this.sseEmitter.send(bd);
                
            } catch (IOException e) {
              
                throw new RuntimeException(e);
            }
    }

    public SseEmitterSubscriber(SseEmitter sseEmitter) {
        this.sseEmitter = sseEmitter;
    }
    
    final private SseEmitter sseEmitter;
}
