package com.jr.renderer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class RendererOld extends JPanel {

    private BufferedImage canvas;

    public RendererOld(int width, int height) {
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


    public static void main(String[] args) {
//        int width = 640;
//        int height = 480;
//        JFrame frame = new JFrame("Direct draw demo");
//
//        RendererOld panel = new RendererOld(width, height);
//        Vec2i t0[] = {new Vec2i(10, 70), new Vec2i(50, 160), new Vec2i(70, 80)};
//        Vec2i t1[] = {new Vec2i(180, 50), new Vec2i(150, 1), new Vec2i(70, 180)};
//        Vec2i t2[] = {new Vec2i(180, 150), new Vec2i(120, 160), new Vec2i(130, 180)};
//
//        panel.triangle(t0[0], t0[1], t0[2], Color.RED);
//        panel.triangle(t1[0], t1[1], t1[2], Color.WHITE);
//        panel.triangle(t2[0], t2[1], t2[2], Color.GREEN);
//
//        frame.add(panel);
//        frame.pack();
//        frame.setVisible(true);
//        frame.setResizable(false);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


}