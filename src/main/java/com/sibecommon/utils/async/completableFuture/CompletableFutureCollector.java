package com.sibecommon.utils.async.completableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.concurrent.Executors.newScheduledThreadPool;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class CompletableFutureCollector {

    /**
     * The Logger.
     */
    private static Logger LOGGER = LoggerFactory.getLogger(CompletableFutureCollector.class);

    private CompletableFutureCollector() {
    }

    /**
     * Transforms a <pre>{@code List<CompletableFuture<T>>}</pre> into a <pre>{@code CompletableFuture<List<T>>}</pre>
     *
     * @param <X> the computed result type
     * @param <T> some CompletableFuture
     * @return a CompletableFuture of <pre>{@code CompletableFuture<List<T>>}</pre> that is complete when all collected CompletableFutures are complete.
     */
    public static <X, T extends CompletableFuture<X>> Collector<T, ?, CompletableFuture<List<X>>> collectResult() {
        return Collectors.collectingAndThen(Collectors.toList(), joinResult());
    }

    /**
     * Transforms a <pre>{@code List<CompletableFuture<?>>}</pre> into a <pre>{@code CompletableFuture<Void>}</pre>
     * Use this function if you are not interested in the collected results or the collected CompletableFutures are of
     * type Void.
     *
     * @param <T> some CompletableFuture
     * @return a <pre>{@code CompletableFuture<Void>}</pre> that is complete when all collected CompletableFutures are complete.
     */
    public static <T extends CompletableFuture<?>> Collector<T, ?, CompletableFuture<Void>> allComplete() {
        return Collectors.collectingAndThen(Collectors.toList(), CompletableFutureCollector::allOf);
    }

    private static <X, T extends CompletableFuture<X>> Function<List<T>, CompletableFuture<List<X>>> joinResult() {
        return ls -> allOf(ls)
                .thenApply(v -> ls
                        .stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()));
    }

    private static <T extends CompletableFuture<?>> CompletableFuture<Void> allOf(List<T> ls) {
        return CompletableFuture.allOf(ls.toArray(new CompletableFuture[ls.size()]));
    }


    /**
     * With completable future.
     *
     * @param <T> the type parameter
     * @param t   the t
     * @param ms  the ms
     * @return the completable future
     */
    public static <T> CompletableFuture<T> with(T t, int ms) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(ms);
            } catch (InterruptedException e) {
            }
            return t;
        });
    }

    /**
     * Fail after completable future.
     *
     * @param <T>      the type parameter
     * @param duration the duration
     * @return the completable future
     */
    public static <T> CompletableFuture<T> failAfter(Duration duration) {
        final CompletableFuture<T> promise = new CompletableFuture<>();
        scheduler.schedule(() -> {
            final TimeoutException ex = new TimeoutException("超时 Timeout after " + duration);
            return promise.completeExceptionally(ex);
        }, duration.toMillis(), MILLISECONDS);

        return promise;
    }

    private static final ScheduledExecutorService scheduler = newScheduledThreadPool(
            50);

    /**
     * Within completable future.
     *
     * @param <T>      the type parameter
     * @param future   the future
     * @param duration the duration
     * @return the completable future
     */
    public static <T> CompletableFuture<T> within(CompletableFuture<T> future, Duration duration) {
        final CompletableFuture<T> timeout = failAfter(duration);
        return future.applyToEither(timeout, Function.identity());
    }

}

