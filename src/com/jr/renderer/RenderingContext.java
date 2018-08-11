package com.jr.renderer;

import com.jr.renderer.vectormath.Matrix;
import com.jr.renderer.vectormath.VectorF;

/**
 * @author <a href="mailto:dimaopen@gmail.com">Dmitry Openkov</a>
 *         Created 26.01.16.
 */
public class RenderingContext {
    private Matrix viewportMatrix;
    private Matrix projection;
    private Matrix modelView;
    private Matrix transform;
    private VectorF lightingDir;
    private OldModel oldModel;

    public RenderingContext(Matrix viewportMatrix, Matrix projection, Matrix modelView) {
        this.viewportMatrix = viewportMatrix;
        this.projection = projection;
        this.modelView = modelView;
        transform = viewportMatrix.mul(projection).mul(modelView);
    }

    public Matrix getViewportMatrix() {
        return viewportMatrix;
    }

    public Matrix getProjection() {
        return projection;
    }

    public Matrix getModelView() {
        return modelView;
    }

    public Matrix getTransform() {
        return transform;
    }

    public VectorF getLightingDir() {
        return lightingDir;
    }

    public void setLightingDir(VectorF lightingDir) {
        this.lightingDir = lightingDir;
    }

    public OldModel getOldModel() {
        return oldModel;
    }

    public void setOldModel(OldModel oldModel) {
        this.oldModel = oldModel;
    }
}
