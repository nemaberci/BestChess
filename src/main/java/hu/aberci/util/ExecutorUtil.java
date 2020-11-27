package hu.aberci.util;

import hu.aberci.controllers.ChessClockController;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Util class responsible for the chess clock's thread.
 * */
public class ExecutorUtil {

    /**
     * Chess clock controller whose step runnable is to be called
     * */
    @Setter
    @Getter
    static ChessClockController chessClockController;

    /**
     * The service that houses the thread pool.
     * */
    static ScheduledExecutorService scheduledExecutorService;

    /**
     * The future that will be calling the stepRunnable every second.
     * */
    static ScheduledFuture<?> scheduledFuture;

    /**
     * Initiates the scheduled executor service.
     * */
    public static void init() {

        scheduledExecutorService = Executors.newScheduledThreadPool(1);

    }

    /**
     * Starts the clock by setting the step runnable to be called every second.
     * */
    public static void start() {

        scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(chessClockController.getStepRunnable(), 1, 1, TimeUnit.SECONDS);

    }

    /**
     * Stops the step runnable from running.
     * */
    public static void stop() {

        scheduledFuture.cancel(true);

    }

    /**
     * Stops the opened threads.
     * */
    public static void destroy() {

        try {

            scheduledExecutorService.shutdownNow();
            scheduledExecutorService.awaitTermination(1, TimeUnit.SECONDS);

        } catch (Exception ignore) {

        }

    }

}
