package br.pucpr.imagem;

import java.awt.*;

public class Vector3 implements Cloneable {
    private float r;
    private float g;
    private float b;

    public Vector3(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public Vector3(Color c) {
        r = c.getRed() / 255.0f;
        g = c.getGreen() / 255.0f;
        b = c.getBlue() / 255.0f;
    }

    public Vector3(int color) {
        this(new Color(color));
    }

    public float getR() {
        return r;
    }

    public float getG() {
        return g;
    }

    public float getB() {
        return b;
    }

    public int intR() {
        return (int)(r * 255);
    }

    public int intG() {
        return (int)(g * 255);
    }

    public int intB() {
        return (int)(b * 255);
    }

    public Vector3 add(Vector3 other) {
        r += other.r;
        g += other.g;
        b += other.b;
        return this;
    }

    public Vector3 subtract(Vector3 other) {
        r -= other.r;
        g -= other.g;
        b -= other.b;
        return this;
    }

    public Vector3 multiply(float s) {
        r *= s;
        g *= s;
        b *= s;
        return this;
    }

    public Vector3 multiply(Vector3 other) {
        r *= other.r;
        g *= other.g;
        b *= other.b;
        return this;
    }

    public Vector3 divide(float s) {
        return multiply(1.0f / s);
    }

    public float dot(Vector3 other) {
        return r * other.r + g * other.g + b * other.b;
    }

    private float clamp(float v) {
        return v > 1.0f ? 1.0f : (v < 0.0f ? 0.0f : v);
    }

    public Vector3 clamp() {
        r = clamp(r);
        g = clamp(g);
        b = clamp(b);
        return this;
    }

    public float sizeSqr() {
        return r*r + g*g + b*b;
    }

    public float size() {
        return (float) Math.sqrt(sizeSqr());
    }

    public Vector3 clone() {
        return new Vector3(r, g, b);
    }

    public int getRGB() {
        return new Color(intR(), intG(), intB()).getRGB();
    }
}
