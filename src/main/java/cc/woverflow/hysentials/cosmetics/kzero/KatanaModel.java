package cc.woverflow.hysentials.cosmetics.kzero;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class KatanaModel extends ModelBase {
	private final ModelRenderer bone;

	public KatanaModel() {
		textureWidth = 128;
		textureHeight = 128;

		bone = new ModelRenderer(this);
		bone.setRotationPoint(3.5F, 12.25F, 7.75F);
		bone.cubeList.add(new ModelBox(bone, 92, 0, 0.0F, -2.25F, -5.75F, 1, 2, 2, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 86, 4, 0.0F, -5.25F, -11.75F, 1, 2, 2, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 86, 8, 0.0F, -3.25F, -10.75F, 1, 1, 1, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 86, 0, 0.0F, -6.25F, -12.75F, 1, 2, 2, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 86, 0, 0.0F, -7.25F, -13.75F, 1, 2, 2, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 80, 0, 0.0F, -8.25F, -14.75F, 1, 2, 2, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 98, 0, 0.0F, -1.25F, -3.75F, 1, 2, 2, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 104, 0, 0.0F, -1.25F, -1.75F, 1, 3, 2, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 110, 0, 0.0F, -1.25F, 0.25F, 1, 4, 1, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 114, 0, 0.0F, -0.25F, 1.25F, 1, 3, 2, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 120, 0, 0.0F, -0.25F, 3.25F, 1, 2, 3, 0.0F, false));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		bone.render(f5);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
