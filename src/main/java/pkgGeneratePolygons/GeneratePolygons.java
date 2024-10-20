package pkgGeneratePolygons;

import pkgSlRenderer.RenderEngine;

import java.util.Random;

import static org.lwjgl.opengl.GL11.*;


public class GeneratePolygons extends RenderEngine {
    private float RADIUS = 0.05f;
    private int MAX_SIDES = 40;
    private int MAX_POLYGONS = 100;
    Random my_rand;


    public void setRadius(float radius) {
        this.RADIUS = Math.max(0.01f, radius);
    }


    public void setSides(int maxSides) {
        this.MAX_SIDES = maxSides;
    }

    public void setNumberPolygons(int maxPolygons) {
        this.MAX_POLYGONS = maxPolygons;
    }

    public void generateRandomNumPolygons() {
        for (int numPolygon = 0; numPolygon < MAX_POLYGONS; numPolygon++) {
            float[] center = new float[]{my_rand.nextFloat() * 2 - 1, my_rand.nextFloat() * 2 - 1, 0.0f};
            float[] color = new float[]{my_rand.nextFloat(), my_rand.nextFloat(), my_rand.nextFloat(), 1.0f};
            int sides = my_rand.nextInt(MAX_SIDES-2) + 3;

            glColor4f(color[0], color[1], color[2], color[3]);
            genPolygons(center, RADIUS, sides);

        }
    }

    private void genPolygons(float[] center, float radius, int sides) {
        glBegin(GL_TRIANGLE_FAN);
        glVertex3f(center[0], center[1], center[2]);

        for (int side = 0; side < sides; side++) {

        }
    }






    @Override
    public void render(int frameDelay, int rows, int cols) {

    }

    @Override
    public void render(float radius) {

    }

    @Override
    public void render() {

    }
}
