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

/**git com
 * Created by denys on 11.08.18.
 */
public class Model {
    public class Vertex {

        public VectorF location;
        public Color color;

        public Vertex(VectorF location, Color color){
            this.location = location;
            this.color = color;
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

    public void loadFaces(BufferedImage color, BufferedImage depth){
        int w = color.getWidth();
        int h = color.getHeight();

        // get vectors
        final VectorF[] vectors = new VectorF[h * w];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                float x = (float) i * 2 / w - 1;
                float y = (float) j * 2 / h - 1;
                vectors[j * w + i] = new VectorF(x, y, 0.0f);
            }
        }

        for (int x = 0; x < w - 1; x++) {
            for (int y = 0; y < h - 1; y++) {
                final Vertex[] faceRight = new Vertex[3];
                Color c = new Color(color.getRGB(x, y));
                faceRight[0] = new Vertex(vectors[y * w + x], c);
                faceRight[1] = new Vertex(vectors[y * w + x + 1], c);
                faceRight[2] = new Vertex(vectors[(y + 1) * w + x], c);
                faces.add(faceRight);

                final Vertex[] faceLeft = new Vertex[3];
                faceLeft[0] = new Vertex(vectors[y * w + x + 1], c);
                faceLeft[1] = new Vertex(vectors[(y + 1) * w + x], c);
                faceLeft[2] = new Vertex(vectors[(y + 1) * w + x + 1], c);
                faces.add(faceLeft);
            }
        }
    }
}
