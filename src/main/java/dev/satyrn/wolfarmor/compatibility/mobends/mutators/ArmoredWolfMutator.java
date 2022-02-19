package dev.satyrn.wolfarmor.compatibility.mobends.mutators;

import dev.satyrn.wolfarmor.client.model.ModelWolfBackpack;
import dev.satyrn.wolfarmor.client.renderer.entity.layer.LayerWolfArmor;
import dev.satyrn.wolfarmor.client.renderer.entity.layer.LayerWolfBackpack;
import dev.satyrn.wolfarmor.mixin.accessors.ModelWolfAccessor;
import goblinbob.mobends.core.client.model.*;
import goblinbob.mobends.core.data.IEntityDataFactory;
import goblinbob.mobends.standard.data.WolfData;
import goblinbob.mobends.standard.mutators.WolfMutator;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelWolf;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ArmoredWolfMutator extends WolfMutator {

    public ModelPart wolfHeadMainArmorOuter;
    public ModelPart wolfBodyArmorOuter;
    public ModelPartExtended wolfLeg1ArmorOuter;
    public ModelPartExtended wolfLeg2ArmorOuter;
    public ModelPartExtended wolfLeg3ArmorOuter;
    public ModelPartExtended wolfLeg4ArmorOuter;
    public ModelPart wolfTailArmorOuter;
    public ModelPart wolfManeArmorOuter;

    public ModelPart noseArmorOuter;
    public ModelPart mouthArmorOuter;
    public ModelPart leftEarArmorOuter;
    public ModelPart rightEarArmorOuter;
    public ModelPart foreLeg1ArmorOuter;
    public ModelPart foreLeg2ArmorOuter;
    public ModelPart foreLeg3ArmorOuter;
    public ModelPart foreLeg4ArmorOuter;

    public ModelPart wolfHeadMainArmorInner;
    public ModelPart wolfBodyArmorInner;
    public ModelPartExtended wolfLeg1ArmorInner;
    public ModelPartExtended wolfLeg2ArmorInner;
    public ModelPartExtended wolfLeg3ArmorInner;
    public ModelPartExtended wolfLeg4ArmorInner;
    public ModelPart wolfTailArmorInner;
    public ModelPart wolfManeArmorInner;

    public ModelPart noseArmorInner;
    public ModelPart mouthArmorInner;
    public ModelPart leftEarArmorInner;
    public ModelPart rightEarArmorInner;
    public ModelPart foreLeg1ArmorInner;
    public ModelPart foreLeg2ArmorInner;
    public ModelPart foreLeg3ArmorInner;
    public ModelPart foreLeg4ArmorInner;

    public ModelPart backpackLeftTop;
    public ModelPart backpackRightTop;
    public ModelPart backpackLeftBottom;
    public ModelPart backpackRightBottom;

    public ModelWolf vanillaOuterArmorModel;
    public ModelWolf vanillaInnerArmorModel;
    public ModelWolfBackpack vanillaWolfBackpackModel;

    private boolean isCompatibleWolf;

    /**
     * Initializes a new armored wolf mutator.
     * @param dataFactory The data factory.
     */
    public ArmoredWolfMutator(IEntityDataFactory<EntityWolf> dataFactory) {
        super(dataFactory);
    }

    @Override
    public void swapLayer(RenderLivingBase<? extends EntityWolf> renderer, int index, boolean isModelVanilla) {
        super.swapLayer(renderer, index, isModelVanilla);

        final LayerRenderer<?> layerRenderer = this.layerRenderers.get(index);
        if (layerRenderer instanceof LayerWolfArmor) {
            final LayerWolfArmor layerWolfArmor = (LayerWolfArmor) layerRenderer;
            final ModelBase innerArmorModel = layerWolfArmor.getArmorInnerLayer();
            final ModelBase outerArmorModel = layerWolfArmor.getArmorOuterLayer();
            if (innerArmorModel instanceof ModelWolf) {
                final ModelWolf innerArmorModelWolf = (ModelWolf) innerArmorModel;
                if (isModelVanilla(innerArmorModelWolf)) {
                    this.storeVanillaInnerArmorModel(innerArmorModelWolf);
                }

                this.createInnerArmorLayerParts(innerArmorModelWolf, 0.1F);
            }
            if (outerArmorModel instanceof ModelWolf) {
                final ModelWolf outerArmorModelWolf = (ModelWolf) outerArmorModel;
                if (isModelVanilla(outerArmorModelWolf)) {
                    this.storeVanillaOuterArmorModel(outerArmorModelWolf);
                }

                this.createOuterArmorLayerParts(outerArmorModelWolf, 0.2F);
            }
        } else if (layerRenderer instanceof LayerWolfBackpack) {
            final LayerWolfBackpack layerWolfBackpack = (LayerWolfBackpack) layerRenderer;
            final ModelBase modelBase = layerWolfBackpack.getModelWolfBackpack();
            if (modelBase instanceof ModelWolfBackpack) {
                final ModelWolfBackpack modelWolfBackpack = (ModelWolfBackpack) modelBase;
                if (isModelVanilla(modelWolfBackpack)) {
                    this.storeVanillaWolfBackpackModel(modelWolfBackpack);
                }

                this.createBackpackLayerParts(modelWolfBackpack, 0F);
            }
        }
    }

    @Override
    public void deswapLayer(RenderLivingBase<? extends EntityWolf> renderer, int index) {
        super.deswapLayer(renderer, index);
        final LayerRenderer<?> layerRenderer = this.layerRenderers.get(index);

        if (layerRenderer instanceof LayerWolfArmor) {
            final LayerWolfArmor layerWolfArmor = (LayerWolfArmor) layerRenderer;
            final ModelBase innerArmorModel = layerWolfArmor.getArmorInnerLayer();
            final ModelBase outerArmorModel = layerWolfArmor.getArmorOuterLayer();
            if (innerArmorModel instanceof ModelWolf) {
                final ModelWolf innerArmorModelWolf = (ModelWolf) innerArmorModel;
                if (!isModelVanilla(innerArmorModelWolf)) {
                    this.applyVanillaInnerArmorModel(innerArmorModelWolf);
                }
            }
            if (outerArmorModel instanceof ModelWolf) {
                final ModelWolf outerArmorModelWolf = (ModelWolf) outerArmorModel;
                if (!isModelVanilla(outerArmorModelWolf)) {
                    this.applyVanillaOuterArmorModel(outerArmorModelWolf);
                }
            }
        } else if (layerRenderer instanceof LayerWolfBackpack) {
            final LayerWolfBackpack layerWolfBackpack = (LayerWolfBackpack) layerRenderer;
            final ModelBase modelBase = layerWolfBackpack.getModelWolfBackpack();
            if (modelBase instanceof ModelWolfBackpack) {
                final ModelWolfBackpack modelWolfBackpack = (ModelWolfBackpack) modelBase;
                if (!isModelVanilla(modelWolfBackpack)) {
                    this.applyVanillaWolfBackpackModel(modelWolfBackpack);
                }
            }
        }
    }

    private void storeVanillaWolfBackpackModel(ModelWolfBackpack model) {
        this.vanillaWolfBackpackModel = new ModelWolfBackpack(0F);

        this.vanillaWolfBackpackModel.backpackLeftBottom = model.backpackLeftBottom;
        this.vanillaWolfBackpackModel.backpackLeftTop = model.backpackLeftTop;
        this.vanillaWolfBackpackModel.backpackRightBottom = model.backpackRightBottom;
        this.vanillaWolfBackpackModel.backpackRightTop = model.backpackRightTop;
    }

    private void storeVanillaInnerArmorModel(ModelWolf model) {
        this.vanillaInnerArmorModel = new ModelWolf();

        this.vanillaInnerArmorModel.wolfHeadMain = model.wolfHeadMain;
        this.vanillaInnerArmorModel.wolfBody = model.wolfBody;
        this.vanillaInnerArmorModel.wolfLeg1 = model.wolfLeg1;
        this.vanillaInnerArmorModel.wolfLeg2 = model.wolfLeg2;
        this.vanillaInnerArmorModel.wolfLeg3 = model.wolfLeg3;
        this.vanillaInnerArmorModel.wolfLeg4 = model.wolfLeg4;
        ((ModelWolfAccessor)this.vanillaInnerArmorModel).setWolfMane(((ModelWolfAccessor)model).getWolfMane());
        ((ModelWolfAccessor)this.vanillaInnerArmorModel).setWolfTail(((ModelWolfAccessor)model).getWolfTail());
    }

    private void storeVanillaOuterArmorModel(ModelWolf model) {
        this.vanillaOuterArmorModel = new ModelWolf();

        this.vanillaOuterArmorModel.wolfHeadMain = model.wolfHeadMain;
        this.vanillaOuterArmorModel.wolfBody = model.wolfBody;
        this.vanillaOuterArmorModel.wolfLeg1 = model.wolfLeg1;
        this.vanillaOuterArmorModel.wolfLeg2 = model.wolfLeg2;
        this.vanillaOuterArmorModel.wolfLeg3 = model.wolfLeg3;
        this.vanillaOuterArmorModel.wolfLeg4 = model.wolfLeg4;
        ((ModelWolfAccessor)this.vanillaOuterArmorModel).setWolfMane(((ModelWolfAccessor)model).getWolfMane());
        ((ModelWolfAccessor)this.vanillaOuterArmorModel).setWolfTail(((ModelWolfAccessor)model).getWolfTail());
    }

    private void applyVanillaWolfBackpackModel(ModelWolfBackpack model) {
        model.backpackLeftBottom = this.vanillaWolfBackpackModel.backpackLeftBottom;
        model.backpackLeftTop = this.vanillaWolfBackpackModel.backpackLeftTop;
        model.backpackRightBottom = this.vanillaWolfBackpackModel.backpackRightBottom;
        model.backpackRightTop = this.vanillaWolfBackpackModel.backpackRightTop;
    }

    private void applyVanillaInnerArmorModel(ModelWolf model) {
        model.wolfHeadMain = this.vanillaInnerArmorModel.wolfHeadMain;
        model.wolfBody = this.vanillaInnerArmorModel.wolfBody;
        model.wolfLeg1 = this.vanillaInnerArmorModel.wolfLeg1;
        model.wolfLeg2 = this.vanillaInnerArmorModel.wolfLeg2;
        model.wolfLeg3 = this.vanillaInnerArmorModel.wolfLeg3;
        model.wolfLeg4 = this.vanillaInnerArmorModel.wolfLeg4;
        ((ModelWolfAccessor)model).setWolfMane(((ModelWolfAccessor)this.vanillaInnerArmorModel).getWolfMane());
        ((ModelWolfAccessor)model).setWolfTail(((ModelWolfAccessor)this.vanillaInnerArmorModel).getWolfTail());
    }

    private void applyVanillaOuterArmorModel(ModelWolf model) {
        model.wolfHeadMain = this.vanillaOuterArmorModel.wolfHeadMain;
        model.wolfBody = this.vanillaOuterArmorModel.wolfBody;
        model.wolfLeg1 = this.vanillaOuterArmorModel.wolfLeg1;
        model.wolfLeg2 = this.vanillaOuterArmorModel.wolfLeg2;
        model.wolfLeg3 = this.vanillaOuterArmorModel.wolfLeg3;
        model.wolfLeg4 = this.vanillaOuterArmorModel.wolfLeg4;
        ((ModelWolfAccessor)model).setWolfMane(((ModelWolfAccessor)this.vanillaOuterArmorModel).getWolfMane());
        ((ModelWolfAccessor)model).setWolfTail(((ModelWolfAccessor)this.vanillaOuterArmorModel).getWolfTail());
    }

    public void createOuterArmorLayerParts(ModelWolf original, float scaleFactor) {
        original.wolfBody = wolfBodyArmorOuter = new ModelPart(original, 18, 14)
                .setPosition(0F, 13F, 8F);
        wolfBodyArmorOuter.developBox(-3F, -3F, -8F, 6, 6, 9, scaleFactor)
                .offsetTextureQuad(BoxSide.TOP, 9F, 6F)
                .rotateTextureQuad(BoxSide.TOP, FaceRotation.HALF_TURN)
                .offsetTextureQuad(BoxSide.BACK, -12F, -9F)
                .rotateTextureQuad(BoxSide.BOTTOM, FaceRotation.HALF_TURN)
                .offsetTextureQuad(BoxSide.BOTTOM, -8F, 6F)
                .rotateTextureQuad(BoxSide.LEFT, FaceRotation.CLOCKWISE)
                .offsetTextureQuad(BoxSide.LEFT, -3F, -3F)
                .rotateTextureQuad(BoxSide.RIGHT, FaceRotation.COUNTER_CLOCKWISE)
                .offsetTextureQuad(BoxSide.RIGHT, 0F, -3F)
                .create();

        original.wolfHeadMain = wolfHeadMainArmorOuter = new ModelPart(original, 0, 0)
                .setParent(wolfBodyArmorOuter)
                .setPosition(0F, 0F, -7F);
        wolfHeadMainArmorOuter.addBox(-3F, -3F, -4F, 6, 6, 4, scaleFactor);

        ((ModelWolfAccessor)original).setWolfMane(wolfManeArmorOuter = new ModelPart(original, 21, 0)
                .setParent(wolfBodyArmorOuter)
                .setPosition(0F, 0F, -7F));
        wolfManeArmorOuter.developBox(-4F, -3.5F, -2F, 8, 7, 6, scaleFactor)
                .offsetTextureQuad(BoxSide.TOP, 16F, 7F)
                .rotateTextureQuad(BoxSide.TOP, FaceRotation.HALF_TURN)
                .offsetTextureQuad(BoxSide.BACK, -5F, -6F)
                .offsetTextureQuad(BoxSide.BOTTOM, -7F, 7F)
                .rotateTextureQuad(BoxSide.BOTTOM, FaceRotation.HALF_TURN)
                .rotateTextureQuad(BoxSide.LEFT, FaceRotation.CLOCKWISE)
                .offsetTextureQuad(BoxSide.LEFT, 1F, 1F)
                .rotateTextureQuad(BoxSide.RIGHT, FaceRotation.COUNTER_CLOCKWISE)
                .offsetTextureQuad(BoxSide.RIGHT, 0F, 1F)
                .offsetTextureQuad(BoxSide.FRONT, 1F, -6F)
                .create();

        original.wolfLeg1 = wolfLeg1ArmorOuter = (ModelPartExtended) new ModelPartExtended(original, 0, 18)
                .setParent(wolfBodyArmorOuter)
                .setPosition(-2.5F, 16F, 7F);
        wolfLeg1ArmorOuter.addBox(-1F, 0F, -1F, 2, 4, 2, scaleFactor);

        original.wolfLeg2 = wolfLeg2ArmorOuter = (ModelPartExtended) new ModelPartExtended(original, 0, 18)
                .setParent(wolfBodyArmorOuter)
                .setPosition(0.5F, 16F, 7F);
        wolfLeg2ArmorOuter.addBox(-1F, 0F, -1F, 2, 4, 2, scaleFactor);

        original.wolfLeg3 = wolfLeg3ArmorOuter = (ModelPartExtended) new ModelPartExtended(original, 0, 18)
                .setParent(wolfBodyArmorOuter)
                .setPosition(-2.5F, 0F, -4F);
        wolfLeg3ArmorOuter.addBox(-1F, 0F, -1F, 2, 4, 2, scaleFactor);

        original.wolfLeg4 = wolfLeg4ArmorOuter = (ModelPartExtended) new ModelPartExtended(original, 0, 18)
                .setParent(wolfBodyArmorOuter)
                .setPosition(0.5F, 0F, -4F);
        wolfLeg4ArmorOuter.addBox(-1F, 0F, -1F, 2, 4, 2, scaleFactor);

        ((ModelWolfAccessor)original).setWolfTail(wolfTailArmorOuter = new ModelPart(original, 9, 18)
                .setParent(wolfBodyArmorOuter)
                .setPosition(-1F, 0F, 8F));
        wolfTailArmorOuter.addBox(-1F, 0F, -2F, 2, 8, 2, scaleFactor);

        noseArmorOuter = new ModelPart(original, 0, 10)
                .setPosition(0, 1F, -4F);
        noseArmorOuter.developBox(-1.5F, -1F, -4F, 3, 2, 4, scaleFactor)
                .hideFace(BoxSide.BOTTOM)
                .create();
        wolfHeadMainArmorOuter.addChild(noseArmorOuter);

        mouthArmorOuter = new ModelPart(original, 0, 12)
                .setPosition(0, 2F, -4F);
        mouthArmorOuter.developBox(-1.5F, 0F, -4F, 3, 1, 4, scaleFactor)
                .hideFace(BoxSide.TOP)
                .create();
        wolfHeadMainArmorOuter.addChild(mouthArmorOuter);

        leftEarArmorOuter = new ModelPart(original, 16, 14)
                .setPosition(0, 1F, -4F);
        leftEarArmorOuter.addBox(-1F, -2F, -1F, 2, 2, 1, scaleFactor);
        wolfHeadMainArmorOuter.addChild(leftEarArmorOuter);

        rightEarArmorOuter = new ModelPart(original, 16, 14)
                .setPosition(0, 1F, -4F);
        rightEarArmorOuter.addBox(-1F, -2F, -1F, 2, 2, 1, scaleFactor);
        wolfHeadMainArmorOuter.addChild(rightEarArmorOuter);

        foreLeg1ArmorOuter = new ModelPart(original, 0, 22)
                .setParent(wolfLeg1)
                .setPosition(0F, -4F, -1F);
        foreLeg1ArmorOuter.addBox(-1F, 0, 0, 2, 4, 2, scaleFactor);
        wolfLeg1ArmorOuter.setExtension(foreLeg1ArmorOuter);

        foreLeg2ArmorOuter = new ModelPart(original, 0, 22)
                .setParent(wolfLeg2)
                .setPosition(0F, -4F, -1F);
        foreLeg2ArmorOuter.addBox(-1F, 0, 0, 2, 4, 2, scaleFactor);
        wolfLeg2ArmorOuter.setExtension(foreLeg2ArmorOuter);

        foreLeg3ArmorOuter = new ModelPart(original, 0, 22)
                .setParent(wolfLeg3)
                .setPosition(0F, -4F, 1F);
        foreLeg3ArmorOuter.addBox(-1F, 0, -2, 2, 4, 2, scaleFactor);
        wolfLeg3ArmorOuter.setExtension(foreLeg3ArmorOuter);

        foreLeg4ArmorOuter = new ModelPart(original, 0, 22)
                .setParent(wolfLeg4)
                .setPosition(0F, -4F, 1F);
        foreLeg4ArmorOuter.addBox(-1F, 0, -2, 2, 4, 2, scaleFactor);
        wolfLeg4ArmorOuter.setExtension(foreLeg4ArmorOuter);

    }


    public void createInnerArmorLayerParts(ModelWolf original, float scaleFactor) {
        original.wolfBody = wolfBodyArmorInner = new ModelPart(original, 18, 14)
                .setPosition(0F, 13F, 8F);
        wolfBodyArmorInner.developBox(-3F, -3F, -8F, 6, 6, 9, scaleFactor)
                .offsetTextureQuad(BoxSide.TOP, 9F, 6F)
                .rotateTextureQuad(BoxSide.TOP, FaceRotation.HALF_TURN)
                .offsetTextureQuad(BoxSide.BACK, -12F, -9F)
                .rotateTextureQuad(BoxSide.BOTTOM, FaceRotation.HALF_TURN)
                .offsetTextureQuad(BoxSide.BOTTOM, -8F, 6F)
                .rotateTextureQuad(BoxSide.LEFT, FaceRotation.CLOCKWISE)
                .offsetTextureQuad(BoxSide.LEFT, -3F, -3F)
                .rotateTextureQuad(BoxSide.RIGHT, FaceRotation.COUNTER_CLOCKWISE)
                .offsetTextureQuad(BoxSide.RIGHT, 0F, -3F)
                .create();

        original.wolfHeadMain = wolfHeadMainArmorInner = new ModelPart(original, 0, 0)
                .setParent(wolfBodyArmorInner)
                .setPosition(0F, 0F, -7F);
        wolfHeadMainArmorInner.addBox(-3F, -3F, -4F, 6, 6, 4, scaleFactor);

        ((ModelWolfAccessor)original).setWolfMane(wolfManeArmorInner = new ModelPart(original, 21, 0)
                .setParent(wolfBodyArmorInner)
                .setPosition(0F, 0F, -7F));
        wolfManeArmorInner.developBox(-4F, -3.5F, -2F, 8, 7, 6, scaleFactor)
                .offsetTextureQuad(BoxSide.TOP, 16F, 7F)
                .rotateTextureQuad(BoxSide.TOP, FaceRotation.HALF_TURN)
                .offsetTextureQuad(BoxSide.BACK, -5F, -6F)
                .offsetTextureQuad(BoxSide.BOTTOM, -7F, 7F)
                .rotateTextureQuad(BoxSide.BOTTOM, FaceRotation.HALF_TURN)
                .rotateTextureQuad(BoxSide.LEFT, FaceRotation.CLOCKWISE)
                .offsetTextureQuad(BoxSide.LEFT, 1F, 1F)
                .rotateTextureQuad(BoxSide.RIGHT, FaceRotation.COUNTER_CLOCKWISE)
                .offsetTextureQuad(BoxSide.RIGHT, 0F, 1F)
                .offsetTextureQuad(BoxSide.FRONT, 1F, -6F)
                .create();

        original.wolfLeg1 = wolfLeg1ArmorInner = (ModelPartExtended) new ModelPartExtended(original, 0, 18)
                .setParent(wolfBodyArmorInner)
                .setPosition(-2.5F, 16F, 7F);
        wolfLeg1ArmorInner.addBox(-1F, 0F, -1F, 2, 4, 2, scaleFactor);

        original.wolfLeg2 = wolfLeg2ArmorInner = (ModelPartExtended) new ModelPartExtended(original, 0, 18)
                .setParent(wolfBodyArmorInner)
                .setPosition(0.5F, 16F, 7F);
        wolfLeg2ArmorInner.addBox(-1F, 0F, -1F, 2, 4, 2, scaleFactor);

        original.wolfLeg3 = wolfLeg3ArmorInner = (ModelPartExtended) new ModelPartExtended(original, 0, 18)
                .setParent(wolfBodyArmorInner)
                .setPosition(-2.5F, 0F, -4F);
        wolfLeg3ArmorInner.addBox(-1F, 0F, -1F, 2, 4, 2, scaleFactor);

        original.wolfLeg4 = wolfLeg4ArmorInner = (ModelPartExtended) new ModelPartExtended(original, 0, 18)
                .setParent(wolfBodyArmorInner)
                .setPosition(0.5F, 0F, -4F);
        wolfLeg4ArmorInner.addBox(-1F, 0F, -1F, 2, 4, 2, scaleFactor);

        ((ModelWolfAccessor)original).setWolfTail(wolfTailArmorInner = new ModelPart(original, 9, 18)
                .setParent(wolfBodyArmorInner)
                .setPosition(-1F, 0F, 8F));
        wolfTailArmorInner.addBox(-1F, 0F, -2F, 2, 8, 2, scaleFactor);

        noseArmorInner = new ModelPart(original, 0, 10)
                .setPosition(0, 1F, -4F);
        noseArmorInner.developBox(-1.5F, -1F, -4F, 3, 2, 4, scaleFactor)
                .hideFace(BoxSide.BOTTOM)
                .create();
        wolfHeadMainArmorInner.addChild(noseArmorInner);

        mouthArmorInner = new ModelPart(original, 0, 12)
                .setPosition(0, 2F, -4F);
        mouthArmorInner.developBox(-1.5F, 0F, -4F, 3, 1, 4, scaleFactor)
                .hideFace(BoxSide.TOP)
                .create();
        wolfHeadMainArmorInner.addChild(mouthArmorInner);

        leftEarArmorInner = new ModelPart(original, 16, 14)
                .setPosition(0, 1F, -4F);
        leftEarArmorInner.addBox(-1F, -2F, -1F, 2, 2, 1, scaleFactor);
        wolfHeadMainArmorInner.addChild(leftEarArmorInner);

        rightEarArmorInner = new ModelPart(original, 16, 14)
                .setPosition(0, 1F, -4F);
        rightEarArmorInner.addBox(-1F, -2F, -1F, 2, 2, 1, scaleFactor);
        wolfHeadMainArmorInner.addChild(rightEarArmorInner);

        foreLeg1ArmorInner = new ModelPart(original, 0, 22)
                .setParent(wolfLeg1)
                .setPosition(0F, -4F, -1F);
        foreLeg1ArmorInner.addBox(-1F, 0, 0, 2, 4, 2, scaleFactor);
        wolfLeg1ArmorInner.setExtension(foreLeg1ArmorInner);

        foreLeg2ArmorInner = new ModelPart(original, 0, 22)
                .setParent(wolfLeg2)
                .setPosition(0F, -4F, -1F);
        foreLeg2ArmorInner.addBox(-1F, 0, 0, 2, 4, 2, scaleFactor);
        wolfLeg2ArmorInner.setExtension(foreLeg2ArmorInner);

        foreLeg3ArmorInner = new ModelPart(original, 0, 22)
                .setParent(wolfLeg3)
                .setPosition(0F, -4F, 1F);
        foreLeg3ArmorInner.addBox(-1F, 0, -2, 2, 4, 2, scaleFactor);
        wolfLeg3ArmorInner.setExtension(foreLeg3ArmorInner);

        foreLeg4ArmorInner = new ModelPart(original, 0, 22)
                .setParent(wolfLeg4)
                .setPosition(0F, -4F, 1F);
        foreLeg4ArmorInner.addBox(-1F, 0, -2, 2, 4, 2, scaleFactor);
        wolfLeg4ArmorInner.setExtension(foreLeg4ArmorInner);

    }

    private void createBackpackLayerParts(ModelWolfBackpack original, float scale) {
        original.backpackLeftTop = this.backpackLeftTop = new ModelPart(original, 0, 16)
                .setPosition(0F, 13F, 8F);
        this.backpackLeftTop.setTextureSize(16, 32);
        this.backpackLeftTop.addBox(-5F, -2F, -4F, 2, 2, 5, scale);

        original.backpackRightTop = this.backpackRightTop = new ModelPart(original, 0, 0)
                .setPosition(0F, 13F, 8F);
        this.backpackRightTop.setTextureSize(16, 32);
        this.backpackRightTop.addBox(3F, -2F, -4F, 2, 2, 5, scale);

        original.backpackLeftBottom = this.backpackLeftBottom = new ModelPart(original, 0, 23)
                .setPosition(0F, 13F, 8F);
        this.backpackLeftBottom.setTextureSize(16, 32);
        this.backpackLeftBottom.addBox(-6F, 0F, -4F, 3, 4, 5, scale);

        original.backpackRightBottom = this.backpackRightBottom = new ModelPart(original, 0, 7)
                .setPosition(0, 13F, 8F);
        this.backpackRightBottom.setTextureSize(16, 32);
        this.backpackRightBottom.addBox(3F, 0F, -4F, 3, 4, 5, scale);
    }

    @Override
    public void syncUpWithData(WolfData data) {
        super.syncUpWithData(data);

        if (this.wolfHeadMainArmorOuter != null) {
            wolfHeadMainArmorOuter.syncUp(data.head);
            wolfBodyArmorOuter.syncUp(data.body);
            wolfLeg1ArmorOuter.syncUp(data.leg1);
            wolfLeg2ArmorOuter.syncUp(data.leg2);
            wolfLeg3ArmorOuter.syncUp(data.leg3);
            wolfLeg4ArmorOuter.syncUp(data.leg4);
            wolfTailArmorOuter.syncUp(data.tail);
            wolfManeArmorOuter.syncUp(data.mane);

            noseArmorOuter.syncUp(data.nose);
            mouthArmorOuter.syncUp(data.mouth);
            leftEarArmorOuter.syncUp(data.leftEar);
            rightEarArmorOuter.syncUp(data.rightEar);
            foreLeg1ArmorOuter.syncUp(data.foreLeg1);
            foreLeg2ArmorOuter.syncUp(data.foreLeg2);
            foreLeg3ArmorOuter.syncUp(data.foreLeg3);
            foreLeg4ArmorOuter.syncUp(data.foreLeg4);
        }
        if (this.wolfHeadMainArmorInner != null) {
            wolfHeadMainArmorInner.syncUp(data.head);
            wolfBodyArmorInner.syncUp(data.body);
            wolfLeg1ArmorInner.syncUp(data.leg1);
            wolfLeg2ArmorInner.syncUp(data.leg2);
            wolfLeg3ArmorInner.syncUp(data.leg3);
            wolfLeg4ArmorInner.syncUp(data.leg4);
            wolfTailArmorInner.syncUp(data.tail);
            wolfManeArmorInner.syncUp(data.mane);

            noseArmorInner.syncUp(data.nose);
            mouthArmorInner.syncUp(data.mouth);
            leftEarArmorInner.syncUp(data.leftEar);
            rightEarArmorInner.syncUp(data.rightEar);
            foreLeg1ArmorInner.syncUp(data.foreLeg1);
            foreLeg2ArmorInner.syncUp(data.foreLeg2);
            foreLeg3ArmorInner.syncUp(data.foreLeg3);
            foreLeg4ArmorInner.syncUp(data.foreLeg4);
        }
        if (this.backpackLeftTop != null) {
            backpackLeftTop.syncUp(data.body);
            backpackRightTop.syncUp(data.body);
            backpackLeftBottom.syncUp(data.body);
            backpackRightBottom.syncUp(data.body);
        }
    }

    private boolean isModelVanilla(ModelWolfBackpack model)
    {
        return !(model.backpackRightTop instanceof IModelPart);
    }
}
