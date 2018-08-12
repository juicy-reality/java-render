package com.jr.renderer;

import com.jr.renderer.vectormath.VectorF;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by denys on 11.08.18.
 */
public class Model {
    public class Vertex {

        public VectorF location;
        public Color color;

        public Vertex(VectorF location, Color color){

        }
    }

    private List<Vertex[]> faces;

    public Model(Path colorFile, Path depthFile) throws FileNotFoundException {
        faces = new ArrayList<>(1024);
        loadFaces(loadImg(colorFile), loadImg(depthFile));
    }

    private BufferedImage loadImg(Path path) {
        try {
            return ImageIO.read(path.toFile());
        } catch (IOException e) {
            System.out.println("Cannot load file;");
        }
        return null;
    }

    public FAce vertex
    public void loadFaces(BufferedImage color, BufferedImage depth){
        int w = color.getWidth();
        int h = color.getHeight();

        // get vertices
        final Vertex[] vertices = new Vertex[h * w];
        for (int i = 1; i < w; i++) {
            for (int j = 1; j <= h; j++) {
                float x = (float) i * 2 / w - 1;
                float y = (float) i * 2 / h - 1;
                Vertex v = new Vertex();
                vet
                vertices[i * w + j] = new Vertex()
            }
        }

        for (int x = 1; x < w; x++) {
            for (int y = 1; y <= h; y++) {
                Vertex vertex = new Vertex();
                vertex.location = vertexes.get(scanner.nextInt() - 1);
                vertex.coloruv = uvs.get(scanner.nextInt() - 1);
                vertex.normal = normals.get(scanner.nextInt() - 1);
                face[i] = vertex;
            }
        }
        return new Dimension(img.getWidth(null), img.getHeight(null));
    }
}
