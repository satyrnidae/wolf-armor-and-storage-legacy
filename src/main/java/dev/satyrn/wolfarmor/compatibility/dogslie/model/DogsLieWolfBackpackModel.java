package dev.satyrn.wolfarmor.compatibility.dogslie.model;

import dev.satyrn.wolfarmor.client.model.ModelWolfBackpack;
import me.ichun.letsleepingdogslie.common.LetSleepingDogsLie;
import me.ichun.letsleepingdogslie.common.core.TickHandlerClient;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * Model for wolf backpacks
 */
@SideOnly(Side.CLIENT)
public class DogsLieWolfBackpackModel extends ModelWolfBackpack {
    /**
     * Constructs a new wolf backpack model
     *
     * @param scale The scale
     */
    public DogsLieWolfBackpackModel(float scale) {
        super(scale);
    }

    /**
     * Sets up animations for the entity
     *
     * @param entity          the entity
     * @param limbSwing       The entity's limb swing progress
     * @param limbSwingAmount The entity's limb swing progress amount
     * @param partialTickTime The world's partial ticks
     */
    @Override
    public void setLivingAnimations(@Nonnull EntityLivingBase entity,
                                    float limbSwing,
                                    float limbSwingAmount,
                                    float partialTickTime) {
        super.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTickTime);
        if (entity instanceof EntityWolf) {
            EntityWolf wolf = (EntityWolf) entity;

            if(wolf.isSitting()) {
                TickHandlerClient.WolfInfo wolfInfo = LetSleepingDogsLie.tickHandlerClient.getWolfInfo(wolf);
                if (wolfInfo.isLying()) {
                    this.backpackLeftTop.rotateAngleX = this.backpackLeftBottom.rotateAngleX = 0;
                    this.backpackRightTop.rotateAngleX = this.backpackRightBottom.rotateAngleX = 0;
                    this.backpackRightTop.setRotationPoint(0, 20.9f, 2);
                    this.backpackRightBottom.setRotationPoint(0, 20.9f, 2);
                    this.backpackLeftTop.setRotationPoint(0, 20.9f, 2);
                    this.backpackLeftBottom.setRotationPoint(0, 20.9f, 2);

                    // Handle iChun's silliness
                    if ("iChun".equals(wolf.getName())) {
                        this.backpackLeftBottom.rotateAngleY = this.backpackLeftTop.rotateAngleY = ((wolf.ticksExisted + partialTickTime) / 3.5F);
                        this.backpackLeftBottom.rotateAngleZ = this.backpackLeftTop.rotateAngleZ = ((wolf.ticksExisted + partialTickTime) / 3.5F);
                        this.backpackRightBottom.rotateAngleY = this.backpackRightTop.rotateAngleY = ((wolf.ticksExisted + partialTickTime) / 3.5F);
                        this.backpackRightBottom.rotateAngleZ = this.backpackRightTop.rotateAngleZ = ((wolf.ticksExisted + partialTickTime) / 3.5F);
                    }

                    String[] poses = wolfInfo.getCompatiblePoses(wolf);

                    switch(poses[1]) {
                        case "hindlegSideL":
                            this.backpackRightTop.rotateAngleZ = this.backpackRightBottom.rotateAngleZ = -0.610865F;
                            this.backpackLeftTop.rotateAngleZ = this.backpackLeftBottom.rotateAngleZ = -0.610865F;
                            break;
                        case "hindlegSideR":
                            this.backpackRightTop.rotateAngleZ = this.backpackRightBottom.rotateAngleZ = 0.610865F;
                            this.backpackLeftTop.rotateAngleZ = this.backpackLeftBottom.rotateAngleZ = 0.610865F;
                            break;
                    }
                }
            }
        }
    }

    //endregion Public / Protected Methods
}
