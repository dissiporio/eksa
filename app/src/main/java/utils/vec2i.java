package main.utils;

public class vec2i {
    public int x, y;

    public vec2i(int a, int b) {
        x = a;
        y = b;
    }

    //math with vec2
    public void add(vec2i a) {x+=a.x;y+=a.y;}
    public void sub(vec2i a) {x-=a.x;y-=a.y;}
    public void mul(vec2i a) {x*=a.x;y*=a.y;}

    public static vec2i blank() { return new vec2i(0, 0); }
}
