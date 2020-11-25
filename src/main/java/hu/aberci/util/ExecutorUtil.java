package hu.aberci.util;

import hu.aberci.controllers.ChessClockController;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ExecutorUtil {

    @Setter
    @Getter
    static ChessClockController chessClockController;

    static ScheduledExecutorService scheduledExecutorService;

    static ScheduledFuture<?> scheduledFuture;

    public static void init() {

        scheduledExecutorService = Executors.newScheduledThreadPool(1);

    }

    public static void start() {

        scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(chessClockController.getStepRunnable(), 1, 1, TimeUnit.SECONDS);

    }

    public static void stop() {

        scheduledFuture.cancel(true);

    }

    public static void destroy() {

        try {

            scheduledExecutorService.shutdownNow();
            scheduledExecutorService.awaitTermination(1, TimeUnit.SECONDS);

        } catch (Exception ignore) {

        }

    }

}
