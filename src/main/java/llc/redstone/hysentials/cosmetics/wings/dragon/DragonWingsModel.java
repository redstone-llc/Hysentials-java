package llc.redstone.hysentials.cosmetics.wings.dragon;

import llc.redstone.hysentials.utils.animation.Animation;
import llc.redstone.hysentials.utils.animation.Keyframe;
import llc.redstone.hysentials.utils.animation.KeyframeType;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.resources.data.AnimationFrame;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.model.b3d.B3DModel;
import org.lwjgl.Sys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DragonWingsModel extends ModelBase {
    private final ModelRenderer all;
    private final ModelRenderer right;
    private final ModelRenderer rr;
    private final ModelRenderer ri;
    private final ModelRenderer left;
    private final ModelRenderer ll;
    private final ModelRenderer cube_r1;
    private final ModelRenderer li;
    private final ModelRenderer cube_r2;

    private final Animation rightAnimation;
    private final Animation leftAnimation;


    public DragonWingsModel() {
        textureWidth = 128;
        textureHeight = 128;

        all = new ModelRenderer(this);
        all.setRotationPoint(0.0F, 4.0F, 3.5F);
        all.cubeList.add(new ModelBox(all, 74, 40, -5.0F, -4.0F, -1.5F, 10, 3, 3, 0.0F, false));

        right = new ModelRenderer(this);
        right.setRotationPoint(-5.0F, -1.0F, -1.5F);
        all.addChild(right);


        rr = new ModelRenderer(this);
        rr.setRotationPoint(-14.0F, 0.0F, 0.0F);
        right.addChild(rr);
        rr.cubeList.add(new ModelBox(rr, 45, 4, -16.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F, false));
        rr.cubeList.add(new ModelBox(rr, 0, 13, -16.0F, 0.0F, 1.0F, 16, 0, 13, 0.0F, false));
        rr.cubeList.add(new ModelBox(rr, 32, 38, -16.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F, false));
        rr.cubeList.add(new ModelBox(rr, 0, 13, -16.0F, 0.0F, 1.0F, 16, 0, 13, 0.0F, false));

        ri = new ModelRenderer(this);
        ri.setRotationPoint(0.0F, 0.0F, 0.0F);
        right.addChild(ri);
        ri.cubeList.add(new ModelBox(ri, 28, 26, -14.0F, 0.0F, 2.0F, 14, 0, 12, 0.0F, false));
        ri.cubeList.add(new ModelBox(ri, 28, 26, -14.0F, 0.0F, 2.0F, 14, 0, 12, 0.0F, false));
        ri.cubeList.add(new ModelBox(ri, 0, 38, -14.0F, -2.0F, -2.0F, 14, 4, 4, 0.0F, false));
        ri.cubeList.add(new ModelBox(ri, 0, 38, -14.0F, -2.0F, -2.0F, 14, 4, 4, 0.0F, false));

        left = new ModelRenderer(this);
        left.setRotationPoint(5.0F, -1.0F, -1.5F);
        all.addChild(left);


        ll = new ModelRenderer(this);
        ll.setRotationPoint(14.0F, 0.0F, 0.0F);
        left.addChild(ll);
        ll.cubeList.add(new ModelBox(ll, 32, 38, 0.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F, false));
        ll.cubeList.add(new ModelBox(ll, 45, 4, 0.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F, false));

        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(-20.0F, 1.0F, 0.0F);
        ll.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.0F, 0.0F, 3.1416F);
        cube_r1.cubeList.add(new ModelBox(cube_r1, 0, 0, -36.0F, 1.0F, 1.0F, 16, 0, 13, 0.0F, false));
        cube_r1.cubeList.add(new ModelBox(cube_r1, 0, 0, -36.0F, 1.0F, 1.0F, 16, 0, 13, 0.0F, false));

        li = new ModelRenderer(this);
        li.setRotationPoint(0.0F, 0.0F, 0.0F);
        left.addChild(li);
        li.cubeList.add(new ModelBox(li, 0, 38, 0.0F, -2.0F, -2.0F, 14, 4, 4, 0.0F, false));
        li.cubeList.add(new ModelBox(li, 0, 38, 0.0F, -2.0F, -2.0F, 14, 4, 4, 0.0F, false));

        cube_r2 = new ModelRenderer(this);
        cube_r2.setRotationPoint(-6.0F, 1.0F, 0.0F);
        li.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.0F, 0.0F, 3.1416F);
        cube_r2.cubeList.add(new ModelBox(cube_r2, 0, 26, -20.0F, 1.0F, 2.0F, 14, 0, 12, 0.0F, false));
        cube_r2.cubeList.add(new ModelBox(cube_r2, 0, 26, -20.0F, 1.0F, 2.0F, 14, 0, 12, 0.0F, false));

        rightAnimation = new Animation(
            right,
            Arrays.asList(
                Keyframe.create(0.0, 0.0, 0.0, 0.0, -45.0, 0.0, -35.0),
                Keyframe.create(1.0, 0.0, 0.0, 0.0, -45.0, 0.0, 35.0),
                Keyframe.create(2.0, 0.0, 0.0, 0.0, -45.0, 0.0, -35.0)
            ),
            Arrays.asList(
                new Animation(rr, Arrays.asList(
                    Keyframe.create(0.1667, 0.0, 0.0, 0.0, 0.0, 0.0, -30.0),
                    Keyframe.create(1.1667, 0.0, 0.0, 0.0, 0.0, 0.0, 30.0),
                    Keyframe.create(2.1667, 0.0, 0.0, 0.0, 0.0, 0.0, -30.0)
                ), null)
            )
        );

        leftAnimation = new Animation(
            left,
            Arrays.asList(
                Keyframe.create(0.0, 0.0, 0.0, 0.0, -45.0, 0.0, 35.0),
                Keyframe.create(1.0, 0.0, 0.0, 0.0, -45.0, 0.0, -35.0),
                Keyframe.create(2.0, 0.0, 0.0, 0.0, -45.0, 0.0, 35.0)
            ),
            Arrays.asList(
                new Animation(ll, Arrays.asList(
                    Keyframe.create(0.1667, 0.0, 0.0, 0.0, 0.0, 0.0, 30.0),
                    Keyframe.create(1.1667, 0.0, 0.0, 0.0, 0.0, 0.0, -30.0),
                    Keyframe.create(2.1667, 0.0, 0.0, 0.0, 0.0, 0.0, 30.0)
                ), null)
            )
        );
    }

    Long start1 = null;
    Keyframe rightKeyframe = null;
    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        if (start1 == null) {
            start1 = System.currentTimeMillis();
        }
        float time = (System.currentTimeMillis() - start1) / 1000.0f;
        //round to 4 decimal places
        time = Math.round(time * 10000.0f) / 10000.0f;
        if (time > 2.1667) {
            start1 = System.currentTimeMillis();
        }

        rightAnimation.apply(time);
        leftAnimation.apply(time);
        all.render(f5);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
