package cc.woverflow.hysentials.util;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelRenderBuilder {
    private ModelRenderer modelRenderer;
    public ModelRenderBuilder(ModelBase model, String boxNameIn) {
        this.modelRenderer = new ModelRenderer(model, boxNameIn);
    }

    public ModelRenderBuilder(ModelBase model) {
        this.modelRenderer = new ModelRenderer(model);
    }

    public ModelRenderBuilder(ModelBase model, int texOffX, int texOffY) {
        this.modelRenderer = new ModelRenderer(model, texOffX, texOffY);
    }

    public ModelRenderBuilder addBox(float x, float y, float z, int width, int height, int depth) {
        modelRenderer.addBox(x, y, z, width, height, depth);
        return this;
    }

    public ModelRenderBuilder addBox(float x, float y, float z, int width, int height, int depth, boolean mirror) {
        modelRenderer.addBox(x, y, z, width, height, depth, mirror);
        return this;
    }

    public ModelRenderBuilder addBox(float x, float y, float z, int width, int height, int depth, float scaleFactor) {
        modelRenderer.addBox(x, y, z, width, height, depth, scaleFactor);
        return this;
    }

    public ModelRenderBuilder addBox(float x, float y, float z, float width, float height, float depth, float scaleFactor) {
        this.addBox(x, y, z, Math.round(width), Math.round(height), Math.round(depth), scaleFactor);
        return this;
    }

    public ModelRenderBuilder setRotationPoint(float rotationPointXIn, float rotationPointYIn, float rotationPointZIn) {
        modelRenderer.setRotationPoint(rotationPointXIn, rotationPointYIn, rotationPointZIn);
        return this;
    }

    public ModelRenderBuilder addChild(ModelRenderer child) {
        modelRenderer.addChild(child);
        return this;
    }

    public ModelRenderBuilder addChild(ModelRenderBuilder child) {
        modelRenderer.addChild(child.modelRenderer);
        return this;
    }

    public ModelRenderBuilder offsetAndRotation(float x, float y, float z, float xRot, float yRot, float zRot) {
        modelRenderer.offsetZ = z;
        modelRenderer.offsetY = y;
        modelRenderer.offsetX = x;
        modelRenderer.rotateAngleX = xRot;
        modelRenderer.rotateAngleY = yRot;
        modelRenderer.rotateAngleZ = zRot;
        return this;
    }

    public ModelRenderBuilder offset(float x, float y, float z) {
        modelRenderer.offsetZ = z;
        modelRenderer.offsetY = y;
        modelRenderer.offsetX = x;
        return this;
    }

    public ModelRenderBuilder rotation(float xRot, float yRot, float zRot) {
        modelRenderer.rotateAngleX = xRot;
        modelRenderer.rotateAngleY = yRot;
        modelRenderer.rotateAngleZ = zRot;
        return this;
    }

    public ModelRenderBuilder ZERO() {
        modelRenderer.offsetZ = 0;
        modelRenderer.offsetY = 0;
        modelRenderer.offsetX = 0;
        modelRenderer.rotateAngleX = 0;
        modelRenderer.rotateAngleY = 0;
        modelRenderer.rotateAngleZ = 0;
        return this;
    }

    public ModelRenderer build() {
        return modelRenderer;
    }
}
