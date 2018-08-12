package com.jr.renderer;

import com.jr.renderer.vectormath.VectorF;

import java.awt.Color;

public interface Shader {
    VectorF vertex(RenderingContext ctx, Model.Vertex vertex, int vertexNum);

    Color fragment(RenderingContext ctx, VectorF barycentric);

}
