package nl.tijsbeek.prisonutilmod.minereset;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nl.tijsbeek.prisonutilmod.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class MineTimer {
    private static final Logger logger = LoggerFactory.getLogger(MineTimer.class);

    private static final long SECONDS_IN_MINUTE = 60L;
    private String timeString;

    private long seconds = 0L;


    public MineTimer() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        executorService.scheduleAtFixedRate(() -> {
            seconds++;
            logger.info("Seconds: {}", seconds);

            if (seconds == Config.MINE_RESET_COOLDOWN.get()) {
                resetTimeTo(0);
            }

            timeString = generateTimeString(seconds);

        }, 0, 1, TimeUnit.SECONDS);


        executorService.scheduleAtFixedRate(() -> {
            resetTimerFromInstant(Instant.ofEpochMilli(Config.LAST_TIME_MINE_RESET.get()));
        }, 0, 10, TimeUnit.SECONDS);
    }


    /**
     * Generates a String whcih represents the given seconds based on the following format: {@code %02d:%02d}
     * <p>
     * Example, for 71 seconds this would return `01:11`.
     *
     * @param seconds amount of seconds
     * @return the given time in a formatted String
     */
    private static String generateTimeString(long seconds) {
        int displayMinutes = 0;
        long displaySeconds = seconds;
        while (SECONDS_IN_MINUTE <= displaySeconds) {
            displayMinutes++;
            displaySeconds -= SECONDS_IN_MINUTE;
        }

        return "%02d:%02d".formatted(displayMinutes, displaySeconds);
    }


    /**
     * Resets the time to the given seconds.
     * <p>
     * The timer resets automatically once it surpasses the cooldown assigned in the config.
     *
     * @param resetToSeconds the
     */
    public void resetTimeTo(final long resetToSeconds) {
        if (resetToSeconds > Config.MINE_RESET_COOLDOWN.get()) {
            throw new IllegalArgumentException("The time cannot be above the cooldown");
        }

        logger.info("Resetted the time to {}", resetToSeconds);
        seconds = resetToSeconds;
    }

    /**
     * Resets the time based on the given instant.
     * <p>
     * The timer resets automatically once it surpasses the cooldown assigned in the config.
     */
    public void resetTimerFromInstant(final Instant instant) {
        long difference = Instant.now().getEpochSecond() - instant.getEpochSecond();

        long mineResetCooldown = Config.MINE_RESET_COOLDOWN.get();

        while (difference >= mineResetCooldown) {
            difference -= mineResetCooldown;
        }

        resetTimeTo(difference);
    }


    @SubscribeEvent
    public void onRenderGui(RenderGameOverlayEvent event) {
        if (!event.isCancelable() && RenderGameOverlayEvent.ElementType.TEXT == event.getType()) {
            Minecraft.getInstance().font.drawShadow(new PoseStack(), timeString, 0, 0, Color.ORANGE.getRGB(), true);
        }
    }

    @SubscribeEvent
    public void onClientChatEvent(ClientChatEvent event) {
        String message = event.getMessage();

        if (message.equals("!reset-mine")) {
            resetTimeTo(0);
            Config.LAST_TIME_MINE_RESET.set(Instant.now().toEpochMilli());

            event.setCanceled(true);
        }
    }
}
