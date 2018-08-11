package com.jr.renderer;

import com.jr.renderer.vectormath.Matrix;
import com.jr.renderer.vectormath.VectorF;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.awt.image.BufferedImage;
import java.util.Arrays;

import static java.lang.Math.abs;
import static java.lang.Math.round;

/**
 * @author denys
 *
 */
public class Renderer extends JPanel {
    public int outWidth, outHeight;
    public Camera camera;
    public VectorF lightDirection;
    private Model model;
    private BufferedImage renderedImage;

    public Renderer(Model model, int width, int height) {
        this.model = model;
        outWidth = width;
        outHeight = height;
        camera = new Camera();
        lightDirection = new VectorF(-1, -1, -1);

        renderedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        fillCanvas(Color.BLACK);

        // Keyboard listener
        KeyListener listener = new MyKeyListener(this);
        addKeyListener(listener);
        setFocusable(true);
    }

    public Dimension getPreferredSize() {
        return new Dimension(renderedImage.getWidth(), renderedImage.getHeight());
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(renderedImage, null, null);
    }

    public void fillCanvas(Color c) {
        int color = c.getRGB();
        for (int x = 0; x < renderedImage.getWidth(); x++) {
            for (int y = 0; y < renderedImage.getHeight(); y++) {
                renderedImage.setRGB(x, y, color);
            }
        }
        repaint();
    }


    public void render() {
        Matrix viewport = viewport(outWidth / 8, outHeight / 8, outWidth * 3 / 4, outHeight * 3 / 4);
        RenderingContext ctx = new RenderingContext(viewport, camera.getProjection(), camera.getLookAt());
        ctx.setLightingDir(lightDirection.normalize());
        ctx.setModel(model);

        Shader shader = new GouraudShader();
        float[] zbuffer = createZBuffer();
        for (Model.Vertex[] face : model.getFaces()) {
            VectorF[] screenCoord = new VectorF[3];
            for (int i = 0; i < 3; i++) {
                screenCoord[i] = shader.vertex(ctx, face[i], i);
            }
            triangle(screenCoord, ctx, shader, zbuffer);
        }
    }

    private float[] createZBuffer() {
        float[] zbuffer = new float[outWidth * outHeight];
        Arrays.fill(zbuffer, Float.NEGATIVE_INFINITY);
        return zbuffer;
    }

    private Matrix viewport(int x, int y, int w, int h) {
        Matrix viewportMatrix = Matrix.identity(4);
        viewportMatrix.set(0, 3, x + w / 2.f);
        viewportMatrix.set(1, 3, y + h / 2.f);
        viewportMatrix.set(2, 3, 255.f / 2.f);
        viewportMatrix.set(0, 0, w / 2.f);
        viewportMatrix.set(1, 1, h / 2.f);
        viewportMatrix.set(2, 2, 255.f / 2.f);
        return viewportMatrix;
    }





    VectorF barycentric(VectorF A, VectorF B, VectorF C, VectorF P) {
        VectorF s0 = new VectorF(C.getX() - A.getX(), B.getX() - A.getX(), A.getX() - P.getX());
        VectorF s1 = new VectorF(C.getY() - A.getY(), B.getY() - A.getY(), A.getY() - P.getY());

        VectorF u = s0.cross(s1);
        if (abs(u.getZ()) > .00001f) {
            return new VectorF(1.f - (u.getX() + u.getY()) / u.getZ(), u.getY() / u.getZ(), u.getX() / u.getZ());
        }
        // If z component is zero then triangle ABC is degenerate
        // in this case generate negative coordinates, it will be thrown away by the rasterizer
        return new VectorF(-1, 1, 1);
    }

    private void triangle(VectorF points[], RenderingContext ctx, Shader fragmentShader, float[] zbuffer) {
        VectorF[] rounded = Arrays.stream(points).map(VectorF::round).toArray(VectorF[]::new);
        Arrays.sort(rounded, (v1, v2) -> Float.compare(v1.getX(), v2.getX()));
        int minX = (int) Math.max(rounded[0].getX(), 0);
        int maxX = (int) Math.min(rounded[2].getX(), outWidth - 1);
        Arrays.sort(rounded, (v1, v2) -> Float.compare(v1.getY(), v2.getY()));
        int minY = (int) Math.max(rounded[0].getY(), 0);
        int maxY = (int) Math.min(rounded[2].getY(), outHeight - 1);
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                VectorF P = new VectorF(x, y);
                VectorF c = barycentric(points[0], points[1], points[2], P);
                //find z of our point P
                //we need just weighted sum of z component of each point of our triangle
                float z = points[0].getZ() * c.getComponent(0) + points[1].getZ() * c.getComponent(1) + points[2].getZ() * c.getComponent(2);
                int idx = x + y * outWidth;
                if (c.getX() < 0 || c.getY() < 0 || c.getZ() < 0 || zbuffer[idx] >= z) continue;
                zbuffer[idx] = z;
                Color color = fragmentShader.fragment(ctx, c);
                if (color != null) {
                    setPixel(x, y, color);
                }
            }
        }
    }

    private void setPixel(int x, int y, Color color) {
        //flip vertically
        y = outHeight - y;
        if (x < 0 || y < 0 || x >= outWidth || y >= outHeight) {
            return;
        }
        renderedImage.setRGB(x, y, color.getRGB());
    }

    public class MyKeyListener implements KeyListener {


        Renderer renderer;
        Camera camera;

        public MyKeyListener(Renderer renderer) {
            this.camera = renderer.camera;
            this.renderer = renderer;
        }
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            switch (KeyEvent.getKeyText(e.getKeyCode())) {
                case "W":
                    camera.move(0,0,1);
                    break;
                default:
                    break;
            }
            fillCanvas(Color.BLACK);
            renderer.render();
            renderer.repaint();
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }
}

class GouraudShader implements Shader {
    private Matrix varying_uv = new Matrix(2, 3);
    private float[] varying_intencity = new float[3];
    private Matrix normalMatrix = new Matrix(3, 3);

    @Override
    public VectorF vertex(RenderingContext ctx, Model.Vertex vertex, int vertexNum) {
        varying_intencity[vertexNum] = vertex.normal.dot(ctx.getLightingDir());
        varying_uv.setCol(vertexNum, vertex.uv);
        normalMatrix.setCol(vertexNum, vertex.normal);
        VectorF gl_Vertex = vertex.location.embedded();
        gl_Vertex = ctx.getTransform().mul(gl_Vertex);     // transform it to screen coordinates
        float lastComp = gl_Vertex.getComponent(gl_Vertex.getNumberOfComponents() - 1);
        return gl_Vertex.proj().scale(1.0f / lastComp);                  // project homogeneous coordinates to 3d
    }

    @Override
    public Color fragment(RenderingContext ctx, VectorF bc) {
        float diffuse = new VectorF(varying_intencity).dot(bc);
        if (diffuse < 0) {
            diffuse = 0;
        }
        VectorF uv = varying_uv.mul(bc);
        VectorF n = normalMatrix.mul(bc);
        VectorF l = ctx.getLightingDir().scale(-1);
        VectorF reflectedLight = n.scale(n.dot(l) * 2 ).sub(l).normalize();
        Color c = ctx.getModel().getDiffuse(uv);
        float spec = (float) Math.pow(reflectedLight.getZ(), ctx.getModel().getSecular(uv));
        if (spec < 0) {
            spec = 0;
        }
        float ambient = .1f;
        float intensity = ambient + .8f * diffuse + .2f * spec;
        if (intensity > 1) {
            intensity = 1;
        }
        return new Color(round(c.getRed() * intensity), round(c.getGreen() * intensity), round(c.getBlue() * intensity));
    }

}