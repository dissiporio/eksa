package main.physics;

import main.utils.vec2f;
import main.utils.vec2i;

public class AABB {
    public vec2f pos;
    public vec2i size;

    public AABB(vec2f pos, vec2i size) {
        this.pos = pos;
        this.size = size;
    }

    //intersect detection (collision)
    public boolean intersects(AABB aabb) {
        if (this.pos.x > aabb.pos.x + aabb.size.x || aabb.pos.x > this.pos.x + this.size.x) {
            return false;
        }
        if (this.pos.y > aabb.pos.y + aabb.size.y || aabb.pos.y > this.pos.y + this.size.y) {
            return false;
        }
        return true;
    }
}
