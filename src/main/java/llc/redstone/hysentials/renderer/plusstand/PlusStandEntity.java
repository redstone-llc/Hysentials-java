package llc.redstone.hysentials.renderer.plusstand;

import net.minecraft.entity.EntityLiving;
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
