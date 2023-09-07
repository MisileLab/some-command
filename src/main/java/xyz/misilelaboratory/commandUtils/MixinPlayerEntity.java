package xyz.misilelaboratory.commandUtils;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.CommandBlockBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity {

    @Shadow @Final private static Logger LOGGER;
    @Unique
    private Vec3d lastPos = null;
    @Unique
    private final PlayerEntity p = (PlayerEntity)(Object)this;

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo info) {
        LOGGER.debug("idk what's going on");
        Vec3d currentPos = new Vec3d(p.getX(), p.getY(), p.getZ());
        if (lastPos != null && !lastPos.equals(currentPos)) {
            MinecraftClient c = MinecraftClient.getInstance();
            BlockPos bpos = new BlockPos(new Vec3i((int)Math.floor(currentPos.x), (int)Math.floor(currentPos.y), (int)Math.floor(currentPos.z)));
            if (c.world != null) {
                Block b = c.world.getBlockState(bpos).getBlock();
                if (b == Blocks.COMMAND_BLOCK || b == Blocks.CHAIN_COMMAND_BLOCK || b == Blocks.REPEATING_COMMAND_BLOCK) {
                    String command = ((CommandBlockBlockEntity) Objects.requireNonNull(p.getWorld().getBlockEntity(bpos))).getCommandExecutor().getCommand();
                    p.sendMessage(Text.literal(command), true);
                }
            }
        }
        lastPos = currentPos;
    }
}