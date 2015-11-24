package org.mk.asyncrt.pub.server;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.Executors;

import org.mk.asyncrt.core.services.CoinMiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import rx.Observable;
import rx.schedulers.Schedulers;

@Controller
public class MiningController {

    @RequestMapping("/mine/{count}")
    public SseEmitter mine(@PathVariable Integer count) throws IOException {
        
        
       final SseEmitter sseEmitter = new SseEmitter();
        
       StopWatch stopWatch = new StopWatch("My Stop Watch");
       
       stopWatch.start();
       
          this.coinMiner.mine(count, sseEmitter);
       stopWatch.stop();
       
       /*
        * We should return from the request-response thread immediately, 
        * while sseEmitter continues to emit data bursts out from another 
        * thread managed provided by the asynchronous thread pool.
        */
       System.out.printf("Resuming Main Controller Method Thread: %d =  %s.  "
               + "Time elapsed: %s\n ", 
          Thread.currentThread().getId(), Thread.currentThread().getName(),
          stopWatch.shortSummary());
      
       return sseEmitter;
    }
    
    @Autowired
    private CoinMiner coinMiner;
}
