package llc.redstone.hysentials.renderer.plusStand;

import llc.redstone.hysentials.util.C;
import llc.redstone.hysentials.util.Renderer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class PlusStandEntity extends EntityLiving {
    public String ownerName;

    public PlusStandEntity(World worldIn) {
        super(worldIn);
        setSize(5F, 5F);
        preventEntitySpawning = false;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        updateEntityActionState();
    }

    @Override
    public void moveEntityWithHeading(float strafe, float forward) {
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
    }
}
