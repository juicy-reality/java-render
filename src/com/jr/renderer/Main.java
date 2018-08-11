package com.jr.renderer;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.nio.file.Path;

/**
 * @author <a href="mailto:dimaopen@gmail.com">Dmitry Openkov</a>
 *         Created 19.01.16.
 */
public class Main {

    public static void main(String[] args) {
        Path path = Paths.get("/home/denys/projects/jr/renderer/resources/african_head.obj");
        Model model = new Model();
        try {
            model.loadData(path);
        } catch (FileNotFoundException e) {
            System.err.println("Cannot find file " + args[0]);
            System.exit(1);
            return;
        }

        int width = 800;
        int height = 800;
        JFrame frame = new JFrame("Juicy reality demo");

        Renderer renderer = new Renderer(model, width, height);
        renderer.render();

        frame.add(renderer);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
