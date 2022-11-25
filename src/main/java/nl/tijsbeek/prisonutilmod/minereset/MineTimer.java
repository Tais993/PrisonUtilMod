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

    private long seconds = 0L;


    public MineTimer() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        executorService.scheduleAtFixedRate(() -> {
            seconds++;
            logger.warn("Seconds: {}", seconds);

            if (seconds == Config.MINE_RESET_COOLDOWN.get()) {
                resetTimeTo(0);
            }

        }, 0, 1, TimeUnit.SECONDS);

        executorService.scheduleAtFixedRate(() -> {
            resetTimerFromInstant(Instant.ofEpochMilli(Config.LAST_TIME_MINE_RESET.get()));
        }, 0, 10, TimeUnit.SECONDS);
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

            int displayMinutes = 0;
            long displaySeconds = seconds;
            while (SECONDS_IN_MINUTE <= displaySeconds) {
                displayMinutes++;
                displaySeconds -= SECONDS_IN_MINUTE;
            }

            String format = "%02d:%02d".formatted(displayMinutes, displaySeconds);

            Minecraft.getInstance().font.drawShadow(new PoseStack(), format, 0, 0, Color.ORANGE.getRGB(), true);
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
