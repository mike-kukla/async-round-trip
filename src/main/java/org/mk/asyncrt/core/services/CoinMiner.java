package org.mk.asyncrt.core.services;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import rx.Observable;

public interface CoinMiner {

    public void mine(Integer count, SseEmitter sseEmitter); 
}
