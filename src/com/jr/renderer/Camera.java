package com.jr.renderer;

import com.jr.renderer.vectormath.Matrix;
import com.jr.renderer.vectormath.VectorF;

/**
 * Created by denys on 11.08.18.
 */
public class Camera {
    public VectorF cameraLocation, cameraUp,cameraDirection;

    public final static float STEP_SIZE = (float) 0.3;

    public Camera() {
        cameraDirection = new VectorF(-1, -1, -3);
        cameraLocation = new VectorF(1, 1, 3);
        cameraUp = new VectorF(0, 1, 0);
    }

    public Matrix getLookAt() {
        VectorF lookAt = cameraLocation.add(cameraDirection);

        VectorF z = cameraLocation.sub(lookAt).normalize();
        VectorF x = cameraUp.cross(z).normalize();
        VectorF y = z.cross(x).normalize();
        Matrix modelView = Matrix.identity(4);
        for (int i = 0; i < 3; i++) {
            modelView.set(0, i, x.getComponent(i));
            modelView.set(1, i, y.getComponent(i));
            modelView.set(2, i, z.getComponent(i));
            modelView.set(i, 3, -lookAt.getComponent(i));
        }
        return modelView;
    }

    public Matrix getProjection() {
        float coeff = -1.f / cameraLocation.sub(cameraDirection).length();
        Matrix projection = Matrix.identity(4);
        projection.set(3, 2, coeff);
        return projection;
    }

    public void move(float right, float top, float forward) {
        VectorF v =  new VectorF(right, top, forward);
        cameraLocation = cameraLocation.add(v.scale(STEP_SIZE));
    }
}
