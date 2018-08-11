package com.jr.renderer;

import com.jr.renderer.vectormath.Matrix;
import com.jr.renderer.vectormath.VectorF;

/**
 * Created by denys on 11.08.18.
 */
public class Camera {
    public VectorF location, up, forward, right;

    public final static float STEP_SIZE = (float) 0.3;
    public final static float ROTATION_SIZE = (float) Math.PI / 20;

    public float coeff;
    public Camera() {
        location = new VectorF(0, 0, -1);
        forward = new VectorF(0, 0, 1);
        right = new VectorF(1,0,0);
        up = new VectorF(0, 1, 0);
        coeff = -1.f / 2;
    }

    public Matrix getLookAt() {
        VectorF lookAt = location.add(forward);

        VectorF z = forward.normalize();
        VectorF x = up.cross(z).normalize();
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
        Matrix projection = Matrix.identity(4);
        projection.set(3, 2, coeff);
        return projection;
    }

    public void move(float right, float top, float forward) {
        VectorF v = this.forward.scale(-forward);
        v = v.add(this.right.scale(right));
        v = v.add(this.up.scale(top));
        location = location.add(v.scale(STEP_SIZE));
    }

    public void rotate(float[] r){
        Matrix m = new Matrix(r, 3);
        forward = m.mul(forward);
        up = m.mul(up);
        right = m.mul(right);
    }

    public void rotateX(float ang) {
        float s = (float) Math.sin(ang * ROTATION_SIZE);
        float c = (float) Math.cos(ang * ROTATION_SIZE);
        float[] r = {
             1, 0, 0,
             0, c, -s,
             0, s, c
        };
        rotate(r);
    }

    public void rotateY(float ang) {
        float s = (float) Math.sin(ang * ROTATION_SIZE);
        float c = (float) Math.cos(ang * ROTATION_SIZE);
        float[] r = {
                c, 0, s,
                0, 1, 0,
                -s, 0, c
        };
        rotate(r);
    }

    public void rotateZ(float ang) {
        float s = (float) Math.sin(ang * ROTATION_SIZE);
        float c = (float) Math.cos(ang * ROTATION_SIZE);
        float[] r = {
                c, -s, 0,
                s, c, 0,
                0, 0, 1
        };
        rotate(r);
    }
}
