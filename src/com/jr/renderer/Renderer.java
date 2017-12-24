package com.jr.renderer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Renderer extends JPanel {

    private BufferedImage canvas;

    public Renderer(int width, int height) {
        canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        fillCanvas(Color.BLACK);
    }

    public Dimension getPreferredSize() {
        return new Dimension(canvas.getWidth(), canvas.getHeight());
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(canvas, null, null);
    }

    public void pixel(int x, int y, Color c) {
        // not sure why, but we draw from left bottom
        canvas.setRGB(x, canvas.getHeight() - y, c.getRGB());
    }

    public void fillCanvas(Color c) {
        int color = c.getRGB();
        for (int x = 0; x < canvas.getWidth(); x++) {
            for (int y = 0; y < canvas.getHeight(); y++) {
                canvas.setRGB(x, y, color);
            }
        }
        repaint();
    }


    public void drawLine(Color c, int x1, int y1, int x2, int y2) {
        // Implement line drawing
        repaint();
    }

    public void drawRect(Color c, int x1, int y1, int width, int height) {
        int color = c.getRGB();
        // Implement rectangle drawing
        for (int x = x1; x < x1 + width; x++) {
            for (int y = y1; y < y1 + height; y++) {
                canvas.setRGB(x, y, color);
            }
        }
        repaint();
    }

    public void drawOval(Color c, int x1, int y1, int width, int height) {
        // Implement oval drawing
        repaint();
    }

    void triangle(Vec2i t0, Vec2i t1, Vec2i t2, Color color) {
        // for swaping
        Vec2i t;

        // source https://github.com/ssloy/tinyrenderer/blob/024ad4619b824f9179c86dc144145e2b8b155f52/main.cpp
        if (t0.y == t1.y && t0.y == t2.y) return; // i dont care about degenerate triangles
        if (t0.y > t1.y) {
            t = t1;
            t1 = t0;
            t0 = t;
        }
        if (t0.y > t2.y) {
            t = t2;
            t2 = t0;
            t0 = t;
        }
        if (t1.y > t2.y) {
            t = t2;
            t2 = t1;
            t1 = t;
        }
        int total_height = t2.y - t0.y;
        for (int i = 0; i < total_height; i++) {
            boolean second_half = i > t1.y - t0.y || t1.y == t0.y;
            int segment_height = second_half ? t2.y - t1.y : t1.y - t0.y;
            float alpha = (float) i / total_height;
            float beta  = (float) (i - (second_half ? t1.y - t0.y : 0)) / segment_height; // be careful: with above conditions no division by zero here
            Vec2i a = t2.sub(t0).scale(alpha).add(t0);
            Vec2i b = second_half ? t2.sub(t1).scale(beta).add(t1) : t1.sub(t0).scale(beta).add(t0);
            if (a.x > b.x) {
                t = b;
                b = a;
                a = t;
            };
            for (int j = a.x; j <= b.x; j++) {
                pixel(j, t0.y + i, color); // attention, due to int casts t0.y+i != A.y
            }
        }
    }

    public static void main(String[] args) {
        int width = 640;
        int height = 480;
        JFrame frame = new JFrame("Direct draw demo");

        Renderer panel = new Renderer(width, height);
        Vec2i t0[] = {new Vec2i(10, 70), new Vec2i(50, 160), new Vec2i(70, 80)};
        Vec2i t1[] = {new Vec2i(180, 50), new Vec2i(150, 1), new Vec2i(70, 180)};
        Vec2i t2[] = {new Vec2i(180, 150), new Vec2i(120, 160), new Vec2i(130, 180)};

        panel.triangle(t0[0], t0[1], t0[2], Color.RED);
        panel.triangle(t1[0], t1[1], t1[2], Color.WHITE);
        panel.triangle(t2[0], t2[1], t2[2], Color.GREEN);

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


}