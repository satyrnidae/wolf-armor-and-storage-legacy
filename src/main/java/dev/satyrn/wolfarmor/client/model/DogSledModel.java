// Made with Blockbench 4.1.4
package dev.satyrn.wolfarmor.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class DogSledModel extends ModelBase {
	private final ModelRenderer sled;
	private final ModelRenderer runners;
	private final ModelRenderer runnerFronts;
	private final ModelRenderer leftRunnerFront_r1;
	private final ModelRenderer brushBow;
	private final ModelRenderer leftBrushBowCurve;
	private final ModelRenderer rightBrushBowCurve;
	private final ModelRenderer brushBowFront;
	private final ModelRenderer stanchions;
	private final ModelRenderer topRails;
	private final ModelRenderer handleBar;
	private final ModelRenderer bed;
	private final ModelRenderer bedSupports;
	private final ModelRenderer seat;

	public DogSledModel() {
		textureWidth = 128;
		textureHeight = 32;

		sled = new ModelRenderer(this);
		sled.setRotationPoint(0.0F, 24.0F, 0.0F);
		setRotationAngle(sled, 0.0F, -1.5708F, 0.0F);


		runners = new ModelRenderer(this);
		runners.setRotationPoint(0.0F, 0.0F, 0.0F);
		sled.addChild(runners);
		runners.cubeList.add(new ModelBox(runners, 0, 0, -28.0F, -1.0F, -9.0F, 48, 1, 2, 0.0F, false));
		runners.cubeList.add(new ModelBox(runners, 0, 0, -28.0F, -1.0F, 7.0F, 48, 1, 2, 0.0F, false));

		runnerFronts = new ModelRenderer(this);
		runnerFronts.setRotationPoint(-28.0F, 0.0F, 0.0F);
		runners.addChild(runnerFronts);


		leftRunnerFront_r1 = new ModelRenderer(this);
		leftRunnerFront_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
		runnerFronts.addChild(leftRunnerFront_r1);
		setRotationAngle(leftRunnerFront_r1, 0.0F, 0.0F, 0.5236F);
		leftRunnerFront_r1.cubeList.add(new ModelBox(leftRunnerFront_r1, 100, 0, -8.0F, -1.0F, -9.0F, 8, 1, 2, 0.0F, false));
		leftRunnerFront_r1.cubeList.add(new ModelBox(leftRunnerFront_r1, 100, 0, -8.0F, -1.0F, 7.0F, 8, 1, 2, 0.0F, false));

		brushBow = new ModelRenderer(this);
		brushBow.setRotationPoint(0.0F, 0.0F, 0.0F);
		sled.addChild(brushBow);
		setRotationAngle(brushBow, 0.0F, 0.0F, 0.1047F);
		brushBow.cubeList.add(new ModelBox(brushBow, 0, 3, -39.0F, -3.1F, -10.0F, 39, 2, 1, 0.0F, false));
		brushBow.cubeList.add(new ModelBox(brushBow, 0, 3, -39.0F, -3.1F, 9.0F, 39, 2, 1, 0.0F, false));

		leftBrushBowCurve = new ModelRenderer(this);
		leftBrushBowCurve.setRotationPoint(-38.675F, 0.0F, -9.075F);
		brushBow.addChild(leftBrushBowCurve);
		setRotationAngle(leftBrushBowCurve, 0.0F, 0.8378F, 0.0F);
		leftBrushBowCurve.cubeList.add(new ModelBox(leftBrushBowCurve, 80, 3, -7.6F, -3.1F, -0.9F, 8, 2, 1, 0.0F, false));

		rightBrushBowCurve = new ModelRenderer(this);
		rightBrushBowCurve.setRotationPoint(-38.675F, -1.0F, 9.075F);
		brushBow.addChild(rightBrushBowCurve);
		setRotationAngle(rightBrushBowCurve, 0.0F, -0.8378F, 0.0F);
		rightBrushBowCurve.cubeList.add(new ModelBox(rightBrushBowCurve, 80, 3, -7.625F, -2.1F, -0.1F, 8, 2, 1, 0.0F, false));

		brushBowFront = new ModelRenderer(this);
		brushBowFront.setRotationPoint(0.0F, 0.0F, 0.0F);
		brushBow.addChild(brushBowFront);
		setRotationAngle(brushBowFront, 0.0F, -1.5708F, 0.0F);
		brushBowFront.cubeList.add(new ModelBox(brushBowFront, 98, 3, -4.0F, -3.1F, 43.45F, 8, 2, 1, 0.0F, false));

		stanchions = new ModelRenderer(this);
		stanchions.setRotationPoint(0.0F, 0.0F, 0.0F);
		sled.addChild(stanchions);
		stanchions.cubeList.add(new ModelBox(stanchions, 0, 6, -2.0F, -13.0F, 8.0F, 2, 12, 1, 0.0F, false));
		stanchions.cubeList.add(new ModelBox(stanchions, 6, 6, -14.0F, -10.0F, -9.0F, 2, 9, 1, 0.0F, false));
		stanchions.cubeList.add(new ModelBox(stanchions, 12, 6, -27.0F, -7.0F, -9.0F, 2, 6, 1, 0.0F, false));
		stanchions.cubeList.add(new ModelBox(stanchions, 0, 6, -2.0F, -13.0F, -9.0F, 2, 12, 1, 0.0F, false));
		stanchions.cubeList.add(new ModelBox(stanchions, 12, 6, -27.0F, -7.0F, 8.0F, 2, 6, 1, 0.0F, false));
		stanchions.cubeList.add(new ModelBox(stanchions, 6, 6, -14.0F, -10.0F, 8.0F, 2, 9, 1, 0.0F, false));

		topRails = new ModelRenderer(this);
		topRails.setRotationPoint(0.0F, -13.0F, 0.0F);
		sled.addChild(topRails);
		setRotationAngle(topRails, 0.0F, 0.0F, -0.2531F);
		topRails.cubeList.add(new ModelBox(topRails, 12, 13, -36.0F, -1.0F, -9.0F, 37, 1, 1, 0.0F, false));
		topRails.cubeList.add(new ModelBox(topRails, 12, 13, -36.0F, -1.0F, 8.0F, 37, 1, 1, 0.0F, false));
		topRails.cubeList.add(new ModelBox(topRails, 88, 13, 3.0F, -1.0F, 8.0F, 4, 1, 1, 0.0F, false));
		topRails.cubeList.add(new ModelBox(topRails, 88, 13, 3.0F, -1.0F, -9.0F, 4, 1, 1, 0.0F, false));

		handleBar = new ModelRenderer(this);
		handleBar.setRotationPoint(0.0F, 0.0F, 0.0F);
		topRails.addChild(handleBar);
		setRotationAngle(handleBar, 0.0F, -1.5708F, 0.0F);
		handleBar.cubeList.add(new ModelBox(handleBar, 12, 15, -11.0F, -1.0F, -3.0F, 22, 1, 2, 0.0F, false));

		bed = new ModelRenderer(this);
		bed.setRotationPoint(0.0F, 0.0F, 0.0F);
		sled.addChild(bed);
		bed.cubeList.add(new ModelBox(bed, 0, 19, -35.0F, -6.0F, 5.0F, 35, 1, 2, 0.0F, false));
		bed.cubeList.add(new ModelBox(bed, 0, 19, -35.0F, -6.0F, -7.0F, 35, 1, 2, 0.0F, false));
		bed.cubeList.add(new ModelBox(bed, 0, 19, -35.0F, -6.0F, 2.0F, 35, 1, 2, 0.0F, false));
		bed.cubeList.add(new ModelBox(bed, 0, 19, -35.0F, -6.0F, -4.0F, 35, 1, 2, 0.0F, false));
		bed.cubeList.add(new ModelBox(bed, 0, 19, -35.0F, -6.0F, -1.0F, 35, 1, 2, 0.0F, false));

		bedSupports = new ModelRenderer(this);
		bedSupports.setRotationPoint(-36.0F, -5.0F, -8.0F);
		bed.addChild(bedSupports);
		setRotationAngle(bedSupports, 0.0F, 1.5708F, 0.0F);
		bedSupports.cubeList.add(new ModelBox(bedSupports, 74, 19, -17.0F, -1.0F, -1.0F, 18, 1, 2, 0.0F, false));
		bedSupports.cubeList.add(new ModelBox(bedSupports, 0, 22, -16.0F, 0.0F, 9.0F, 16, 1, 2, 0.0F, false));
		bedSupports.cubeList.add(new ModelBox(bedSupports, 0, 22, -16.0F, 0.0F, 22.0F, 16, 1, 2, 0.0F, false));
		bedSupports.cubeList.add(new ModelBox(bedSupports, 0, 22, -16.0F, 0.0F, 34.0F, 16, 1, 2, 0.0F, false));

		seat = new ModelRenderer(this);
		seat.setRotationPoint(0.0F, 0.0F, 0.0F);
		sled.addChild(seat);
		setRotationAngle(seat, 0.0F, 1.5708F, 1.5708F);
		seat.cubeList.add(new ModelBox(seat, 0, 25, -9.0F, -19.0F, -4.0F, 18, 4, 1, 0.0F, false));
		seat.cubeList.add(new ModelBox(seat, 0, 25, -9.0F, -14.0F, -4.0F, 18, 4, 1, 0.0F, false));
		seat.cubeList.add(new ModelBox(seat, 0, 25, -9.0F, -9.0F, -4.0F, 18, 4, 1, 0.0F, false));
		seat.cubeList.add(new ModelBox(seat, 120, 0, -8.0F, -18.0F, -3.0F, 1, 12, 2, 0.0F, false));
		seat.cubeList.add(new ModelBox(seat, 120, 0, 7.0F, -18.0F, -3.0F, 1, 12, 2, 0.0F, false));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		sled.render(f5);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}