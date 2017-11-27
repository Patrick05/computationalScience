package de.bytedev.utility;

public class LinearCongurentialRandom {

    private long x0;
    private long a;
    private long c;
    private long m;

    private long xn;

    public LinearCongurentialRandom(int x0, int a, int c, int m) {
        this.x0 = x0;
        this.a = a;
        this.c = c;
        this.m = m;

        this.xn = x0;
    }

    public int nextInt() {
        this.xn = (this.a * this.xn + this.c) % this.m;
        return (int)this.xn;
    }

    public double nextDouble() {
        return (1.0*this.nextInt())/this.m;
    }
}
