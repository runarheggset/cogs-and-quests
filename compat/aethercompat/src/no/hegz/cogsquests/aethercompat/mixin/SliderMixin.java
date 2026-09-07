package no.hegz.cogsquests.aethercompat.mixin;

import com.aetherteam.aether.entity.monster.dungeon.boss.Slider;
import net.minecraft.network.chat.Component;
import no.hegz.cogsquests.aethercompat.AetherCompat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Slider.class)
public abstract class SliderMixin {
    @Inject(method = "setBossName", at = @At("HEAD"), cancellable = true, remap = false)
    private void cogsquests$ignoreNullBossName(Component component, CallbackInfo ci) {
        if (component == null) {
            AetherCompat.LOGGER.warn("Blocked a null boss name for {} (MobStacker boss-name reset); keeping the current name", "Slider");
            ci.cancel();
        }
    }
}
