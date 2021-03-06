package nl.tijsbeek.prisonutilmod.minereset;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MineTimer {

    private int minute;
    private int seconds;

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    private ScheduledFuture<?> timer;

    public MineTimer() {
        resetTimer();
    }

    /**
     * Restarts the timer at 1.
     * <p>
     * This gets recursively called by the timer itself.
     * <p>
     * Before resetting, one should make sure the existing {@link #timer timer} is cancelled.
     */
    public void resetTimer() {
        seconds = 0;
        minute = 0;

        timer = executorService.scheduleAtFixedRate(() -> {
            seconds++;

            if (seconds == 60) {
                minute++;
                seconds = 0;

                if (minute == 5) {
                    executorService.schedule(this::resetTimer, 1, TimeUnit.SECONDS);
                    timer.cancel(false);
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    @SubscribeEvent
    public void onRenderGui(RenderGameOverlayEvent event) {
        if (!event.isCancelable() && event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            String format = "%02d:%02d".formatted(minute, seconds);

            Minecraft.getInstance().font.drawShadow(new PoseStack(), format, 0, 0, Color.ORANGE.getRGB(), true);
        }
    }

    @SubscribeEvent
    public void onClientChatEvent(ClientChatEvent event) {
        String message = event.getMessage();

        if (message.equals("!reset-mine")) {
            timer.cancel(true);
            resetTimer();

            event.setCanceled(true);
        }
    }
}
