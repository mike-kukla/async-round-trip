package org.mk.asycnrt.sb.rx;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.mk.asyncrt.sb.rx.DelayingCallable;

import rx.Observable;
import rx.Observer;
import rx.observables.ConnectableObservable;
public class ObservableOne {

  public  void init() throws InterruptedException {  
      
      /*
       * static <T> Observable<T>   from(java.util.concurrent.Future<? extends T> future)
         Converts a Future into an Observable.

        static <T,R> Observable<R>  combineLatest(java.util.List<? extends Observable<? extends T>> sources, FuncN<? extends R> combineFunction)
        Combines a list of source Observables by emitting an item that aggregates the latest values of 
        each of the source Observables each time an item is received from any of the source Observables, 
        where this aggregation is defined by a specified function.
        
        static <T> Observable<T>    merge(java.lang.Iterable<? extends Observable<? extends T>> sequences)
Flattens an Iterable of Observables into one Observable, without any transformation.


public String downloadContents(URL url) throws IOException {
    try(InputStream input = url.openStream()) {
        return IOUtils.toString(input, StandardCharsets.UTF_8);
    }
}

//...

final String contents = downloadContents(new URL("http://www.example.com"));


public static Future<String> startDownloading(URL url) {
    //...
}

final Future<String> contentsFuture = startDownloading(new URL("http://www.example.com"));
//other computation
final String contents = contentsFuture.get();

We will implement startDownloading() soon. For now it's important that you understand 
the principles. startDownloading() does not block, waiting for external website. 
Instead it returns immediately, returning a lightweight Future<String> object. This 
object is a promise that String will be available in the future. Don't know when, but keep 
this reference and once it's there, you'll be able to retrieve it using Future.get(). In 
other words Future is a proxy or a wrapper around an object that is not yet there. Once the 
asynchronous computation is done, you can extract it. So what API does Future provide?

Future.get() is the most important method. It blocks and waits until promised result is 
available (resolved). So if we really need that String, just call get() and wait. There 
is an overloaded version that accepts timeout so you won't wait forever if something 
goes wild. TimeoutException is thrown if waiting for too long.

In some use cases you might want to peek on the Future and continue if result is not yet 
available. This is possible with isDone(). Imagine a situation where your user waits for 
some asynchronous computation and you'd like to let him know that we are still waiting and 
do some computation in the meantime:

final Future<String> contentsFuture = startDownloading(new URL("http://www.example.com"));
while (!contentsFuture.isDone()) {
    askUserToWait();
    doSomeComputationInTheMeantime();
}
contentsFuture.get();

The last call to contentsFuture.get() is guaranteed to return immediately and not 
block because Future.isDone() returned true. If you follow the pattern above make 
sure you are not busy waiting, calling isDone() millions of time per second.

Cancelling futures is the last aspect we have not covered yet. Imagine you started some 
asynchronous job and you can only wait for it given amount of time. If it's not there after, 
say, 2 seconds, we give up and either propagate error or work around it. However if you are 
a good citizen, you should somehow tell this future object: I no longer need you, forget about
it. You save processing resources by not running obsolete tasks. The syntax is simple:

contentsFuture.cancel(true);    //meh...
We all love cryptic, boolean parameters, aren't we? Cancelling comes in two flavours. 
By passing false to mayInterruptIfRunning parameter we only cancel tasks that didn't yet 
started, when the Future represents results of computation that did not even began. 
But if our Callable.call() is already in the middle, we let it finish. However if we 
pass true, Future.cancel() will be more aggressive, trying to interrupt already running 
jobs as well. How? Think about all these methods that throw infamous InterruptedException, 
namely Thread.sleep(), Object.wait(), Condition.await(), and many others (including Future.get()). 
If you are blocking on any of such methods and someone decided to cancel your Callable, 
they will actually throw InterruptedException, signalling that someone is trying to interrupt 
currently running task.

So we now understand what Future<T> is - a place-holder for something, that you will get in 
the future. It's like keys to a car that was not yet manufactured. But how do you actually 
obtain an instance of Future<T> in your application? Two most common sources are thread pools 
and asynchronous methods (backed by thread pools for you). Thus our startDownloading() method 
can be rewritten to:

private final ExecutorService pool = Executors.newFixedThreadPool(10);

public Future<String> startDownloading(final URL url) throws IOException {
    return pool.submit(new Callable<String>() {
        @Override
        public String call() throws Exception {
            try (InputStream input = url.openStream()) {
                return IOUtils.toString(input, StandardCharsets.UTF_8);
            }
        }
    });
}
A lot of syntax boilerplate, but the basic idea is simple: wrap long-running computations in 
Callable<String> and submit() them to a thread pool of 10 threads. Submitting returns some 
implementation of Future<String>, most likely somehow linked to your task and thread pool. 
Obviously your task is not executed immediately. Instead it is placed in a queue which is 
later (maybe even much later) polled by thread from a pool. Now it should be clear what these 
two flavours of cancel() mean - you can always cancel task that still resides in that queue. 
But cancelling already running task is a bit more complex.

Another place where you can meet Future is Spring and EJB. For example in Spring framework 
you can simply annotate your method with @Async:

@Async
public Future<String> startDownloading(final URL url) throws IOException {
    try (InputStream input = url.openStream()) {
        return new AsyncResult<>(
                IOUtils.toString(input, StandardCharsets.UTF_8)
        );
    }
}
N

       */

   
   Observer<BigDecimal>  observerD =  new Observer<BigDecimal>(){

    @Override
    public void onCompleted() {
        
        System.out.printf("Done, just done");
        //then shut down the thread pool perhaps?
    }

    @Override
    public void onError(Throwable arg0) {
        
        System.err.print(arg0);
    }

    @Override
    public void onNext(BigDecimal arg0) {
                
        System.out.printf("Observer here with the next value: %f\n", arg0.doubleValue());
    }
   };
   
   final ExecutorService pool = Executors.newFixedThreadPool(5);
   
   List<Observable<BigDecimal>> observables = new ArrayList<>(); 
   
   for (int i = 0; i < 20; i++) {
       
       Future<BigDecimal> future = pool.submit(new DelayingCallable());
       Observable<BigDecimal> observableForBigDecimal = Observable.from(future);
       observables.add(observableForBigDecimal);
   }

   Observable<BigDecimal> merged = Observable.merge(observables);
   merged.subscribe(observerD);

   //do other stuff
   
  }
}
