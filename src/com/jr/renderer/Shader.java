package com.jr.renderer;

import com.jr.renderer.vectormath.VectorF;

import java.awt.Color;

/**
 * @author <a href="mailto:dimaopen@gmail.com">Dmitry Openkov</a>
 *         Created 31.01.16.
 */
public interface Shader {
    VectorF vertex(RenderingContext ctx, OldModel.Vertex vertex, int vertexNum);

    Color fragment(RenderingContext ctx, VectorF barycentric);

}
