package org.mk.asyncrt.sb.rx;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.Callable;

public class DelayingCallable implements Callable<BigDecimal> {

    @Override
    public BigDecimal call() throws Exception {
        
       Random r = new Random();
        
        BigDecimal d = new BigDecimal(r.nextDouble() * 2_000_000_000L);
        
        int c = 0;
        
        while (c < 2_000_000_000L) {
            c++;
        }
        
        System.out.printf("Callable returning %f from thread: %d: %s.\n", d,
           Thread.currentThread().getId(), Thread.currentThread().getName());
        
        return d;
    }
}
