package main.utils;

public class vec2f {
    public float x, y;

    public vec2f(float a, float b) {
        x = a;
        y = b;
    }

    //cast to vec2i
    public vec2i exp() {return to_int();}
    public vec2i to_int() {return new vec2i((int)x, (int)y);}

    //math with vec2
    public void add(vec2f a) {x+=a.x;y+=a.y;}
    public void sub(vec2f a) {x-=a.x;y-=a.y;}
    public void mul(vec2f a) {x*=a.x;y*=a.y;}

    public static vec2f blank() { return new vec2f(0, 0); }
}