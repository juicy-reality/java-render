package com.jr.renderer;

/**
 * Created by denys on 24.12.17.
 */
public class Vec2i {
    public int x;
    public int y;

    public Vec2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vec2i add(Vec2i b) {
        return new Vec2i(x + b.x, y + b.y);
    }

    public Vec2i sub(Vec2i b) {
        return new Vec2i(x - b.x, y - b.y);
    }

    public Vec2i scale(float a){
        return new Vec2i(Math.round(x * a), Math.round(y * a));
    }
}
