package llc.redstone.hysentials.handlers.npc.lost;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class LostRidable extends EntityTameable {
    public LostAdventure adventure;

    public LostRidable(World world, EntityPlayer owner, LostAdventure adventure) {
        super(world);
        setSize(0.1F, 0.1F);
        ((PathNavigateGround) getNavigator()).setAvoidsWater(true);
        tasks.addTask(1, new EntityAIFollowOwnerNoTP(this, 0.5, 10f, 2f));
        tasks.addTask(2, new EntityAIWander(this, 0.5));
        tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayerSP.class, 8f));
        tasks.addTask(3, new EntityAILookIdle(this));

        setTamed(true);
        setOwnerId(owner.getUniqueID().toString());
        setCustomNameTag("§a");
        preventEntitySpawning = false;
        this.adventure = adventure;
    }

    @Override
    public void moveEntityWithHeading(float strafe, float forward) {
        if (!isInWater()) {
            if (!isInLava()) {
                float f4 = 0.35F;

                if (onGround) {
                    f4 = worldObj.getBlockState(new BlockPos(MathHelper.floor_double(posX), MathHelper.floor_double(getEntityBoundingBox().minY) - 1,
                        MathHelper.floor_double(posZ))).getBlock().slipperiness * 0.91F;
                }

                float f = 0.16277136F / (f4 * f4 * f4);
                float f5 = onGround ? getAIMoveSpeed() * f : jumpMovementFactor;
                moveFlying(strafe, forward, f5);
                adventure.entity.moveFlying(strafe, forward, f5);
                f4 = 0.91F;

                if (onGround) {
                    f4 = worldObj.getBlockState(new BlockPos(MathHelper.floor_double(posX), MathHelper.floor_double(getEntityBoundingBox().minY) - 1,
                        MathHelper.floor_double(posZ))).getBlock().slipperiness * 0.91F;
                }

                if (isOnLadder()) {
                    float f6 = 0.15F;
                    motionX = MathHelper.clamp_double(motionX, -f6, f6);
                    motionZ = MathHelper.clamp_double(motionZ, -f6, f6);
                    fallDistance = 0.0F;
                    if (motionY < -0.15D) motionY = -0.15D;

                    adventure.entity.motionX = motionX;
                    adventure.entity.motionY = motionY;
                    adventure.entity.motionZ = motionZ;
                    adventure.entity.fallDistance = 0.0F;
                }

                moveEntity(motionX, motionY, motionZ);
                adventure.entity.moveFlying(strafe, forward, f5);

                if (isCollidedHorizontally && isOnLadder()) motionY = 0.2D;

                if (worldObj.isRemote && (!worldObj.isBlockLoaded(new BlockPos((int) posX, 0, (int) posZ)) ||
                    !worldObj.getChunkFromBlockCoords(new BlockPos((int) posX, 0, (int) posZ)).isLoaded())) {
                    motionY = posY > 0.0D ? -0.1D : 0.0D;
                    adventure.entity.motionY = motionY;
                } else {
                    motionY -= 0.08D;
                    adventure.entity.motionY = motionY;
                }

                motionY *= 0.9800000190734863D;
                motionX *= f4;
                motionZ *= f4;
                adventure.entity.motionX = motionX;
                adventure.entity.motionZ = motionZ;
                adventure.entity.motionY = motionY;
            } else {
                double d1 = posY;
                moveFlying(strafe, forward, 0.02F);
                moveEntity(motionX, motionY, motionZ);
                adventure.entity.moveFlying(strafe, forward, 0.02F);
                adventure.entity.moveEntity(motionX, motionY, motionZ);
                motionX *= 0.5D;
                motionY *= 0.5D;
                motionZ *= 0.5D;
                motionY -= 0.02D;
                adventure.entity.motionX = motionX;
                adventure.entity.motionY = motionY;
                adventure.entity.motionZ = motionZ;

                if (isCollidedHorizontally && isOffsetPositionInLiquid(motionX, motionY + 0.6000000238418579D - posY + d1, motionZ)) {
                    motionY = 0.30000001192092896D;
                }
                adventure.entity.motionY = motionY;
            }
        } else {
            double d0 = posY;
            float f1 = 0.8F;
            float f2 = 0.02F;
            float f3 = (float) EnchantmentHelper.getDepthStriderModifier(this);
            if (f3 > 3.0F) f3 = 3.0F;
            if (!onGround) f3 *= 0.5F;

            if (f3 > 0.0F) {
                f1 += (0.54600006F - f1) * f3 / 3.0F;
                f2 += (getAIMoveSpeed() * 1.0F - f2) * f3 / 3.0F;
            }

            moveFlying(strafe, forward, f2);
            moveEntity(motionX, motionY, motionZ);
            adventure.entity.moveFlying(strafe, forward, f2);
            adventure.entity.moveEntity(motionX, motionY, motionZ);
            motionX *= f1;
            motionY *= 0.800000011920929D;
            motionZ *= f1;
            motionY -= 0.02D;
            adventure.entity.motionX = motionX;
            adventure.entity.motionY = motionY;
            adventure.entity.motionZ = motionZ;

            if (isCollidedHorizontally && isOffsetPositionInLiquid(motionX, motionY + 0.6000000238418579D - posY + d0, motionZ)) {
                motionY = 0.30000001192092896D;
            }
            adventure.entity.motionY = motionY;
        }
//        armorStand.setPosition(posX, posY - 1.2, posZ);
        adventure.lastX = (int) posX;
        adventure.lastY = (int) posY;
        adventure.lastZ = (int) posZ;
        adventure.entity.moveEntityWithHeading(strafe, forward);
        adventure.entity.setPosition(posX, posY, posZ);

        super.moveEntityWithHeading(strafe, forward);
        adventure.entity.prevLimbSwingAmount = adventure.entity.limbSwingAmount;
        double d0 = posY;
        d0 = this.posX - this.prevPosX;
        double d3 = this.posZ - this.prevPosZ;
        double f3 = MathHelper.sqrt_double(d0 * d0 + d3 * d3) * 4.0F;
        if (f3 > 1.0F) {
            f3 = 1.0F;
        }

        adventure.entity.limbSwingAmount += (f3 - adventure.entity.limbSwingAmount) * 0.4F;
        adventure.entity.limbSwing += adventure.entity.limbSwingAmount;
        adventure.entity.cameraPitch = cameraPitch;
        adventure.entity.prevCameraPitch = prevCameraPitch;
        adventure.entity.rotationYawHead = rotationYawHead;
        adventure.entity.prevRotationYawHead = prevRotationYawHead;
        adventure.entity.renderYawOffset = renderYawOffset;
        adventure.entity.prevRenderYawOffset = prevRenderYawOffset;
        adventure.entity.rotationYaw = rotationYaw;
        adventure.entity.prevRotationYaw = prevRotationYaw;
        adventure.entity.rotationPitch = rotationPitch;
        adventure.entity.prevRotationPitch = prevRotationPitch;
        adventure.entity.prevPosX = adventure.entity.posX;
        adventure.entity.prevPosY = adventure.entity.posY;
        adventure.entity.prevPosZ = adventure.entity.posZ;

    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        updateEntityActionState();
    }

    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        return null;
    }

    @Override
    public boolean canMateWith(EntityAnimal otherAnimal) {
        return false;
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
