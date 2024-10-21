package pkgGeneratePolygons;

import pkgSlRenderer.RenderEngine;

import java.util.Random;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.opengl.GL11.*;


public class GeneratePolygons extends RenderEngine {
    private float RADIUS = 0.05f;
    private int MAX_SIDES = 40;
    private int MAX_POLYGONS = 5;
    private final int NUM_RGBA = 4;
    private final int NUM_3D_COORDS = 3;
    private int UPDATE_INTERVAL = 250;
    private float[][] rand_colors;
    private float[][] rand_coords;
    private float[][] rowCols;
    private int[] rand_sides;

    Random my_rand;

    public GeneratePolygons() {
        my_rand = new Random();
    }
    public void setRadius(float radius) {
        this.RADIUS = Math.max(0.01f, radius);
    }


    public void setSides(int maxSides) {
        this.MAX_SIDES = maxSides;
    }

    public void setNumberPolygons(int maxPolygons) {
        this.MAX_POLYGONS = maxPolygons;
    }

    private void generateSegmentVertices(float[] center, float[] v1, float[] v2, float radius, float angle1, float angle2) {
        v1[0] = center[0]+ radius * (float) Math.cos(angle1);
        v1[1] = center[1] + radius * (float) Math.sin(angle1);
        v1[2] = 0.0f; // z-coordinate

        v2[0] = center[0]+ radius * (float) Math.cos(angle2);
        v2[1] = center[1] + radius * (float) Math.sin(angle2);
        v2[2] = 0.0f; // z-coordinate

    }


    public void generateRandomNumPolygons() {
        int[] boundary = my_wm.getCurrentWindowSize();

        rand_coords = new float[MAX_POLYGONS][NUM_3D_COORDS];
        rand_colors = new float[MAX_POLYGONS][NUM_RGBA];
        rand_sides = new int[MAX_POLYGONS];

        for (int i = 0; i < MAX_POLYGONS; i++) {
            rand_coords[i][0] = (my_rand.nextFloat() * (2.0f - 2.0f * RADIUS)) - (1.0f - RADIUS);
            rand_coords[i][1] = (my_rand.nextFloat() * (2.0f - 2.0f * RADIUS)) - (1.0f - RADIUS);
            rand_coords[i][2] = 0.0f;

            // Random colors (RGBA)
            rand_colors[i][0] = my_rand.nextFloat(); // Red
            rand_colors[i][1] = my_rand.nextFloat(); // Green
            rand_colors[i][2] = my_rand.nextFloat(); // Blue
            rand_colors[i][3] = 1.0f; // Alpha (fully opaque)

            // Random number of sides for the polygon
            rand_sides[i] = my_rand.nextInt(MAX_SIDES - 2) + 3; // Number of sides between 3 and MAX_SIDES

        }
    }

    public void drawRandPolygons() {
        generateRandomNumPolygons();
        long lastUpdateTime = System.currentTimeMillis(); // Track the last time circles were updated
        final float begin_angle = 0.0f, end_angle = (float) (2.0f * Math.PI);
        while (!my_wm.isGlfwWindowClosed()) {
            glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT);

            // Check if the update interval has passed
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastUpdateTime > UPDATE_INTERVAL) {
                generateRandomNumPolygons(); // Update positions of circles
                lastUpdateTime = currentTime; // Reset the timer
            }

            for (int polygon = 0; polygon < MAX_POLYGONS; polygon++) {
                float[] center = rand_coords[polygon]; // Center of the current circle
                float[] color = rand_colors[polygon]; // Color of the current circle
                int sides = rand_sides[polygon];
                glColor4f(color[0], color[1], color[2], color[3]);
                glBegin(GL_TRIANGLES);

                for (int triangle = 0; triangle < sides; triangle++){
                    float[] v1 = new float[NUM_3D_COORDS];
                    float[] v2 = new float[NUM_3D_COORDS];
                    float angle_step = (end_angle - begin_angle) / sides;

                    float angle1 = triangle * angle_step;
                    float angle2 = (triangle + 1) * angle_step;

                    generateSegmentVertices(center, v1, v2, RADIUS, angle1, angle2);

                    glVertex3f(center[0], center[1], center[2]);
                    glVertex3f(v1[0], v1[1], v1[2]);
                    glVertex3f(v2[0], v2[1], v2[2]);
                }
                glEnd();
            }
            my_wm.swapBuffers();
        } // while (!my_wm.isGlfwWindowClosed())
        my_wm.destroyGlfwWindow();
    }


    @Override
    public void genPolygons(float[] center, float radius, int sides) {
        //2d array with coords
        //array with sides

    }


    @Override
    public void render(int frameDelay, int rows, int cols) {
        UPDATE_INTERVAL = frameDelay;

        // Calculate the width and height of each cell in the grid
        float cellWidth = 2.0f / cols; // Screen space width (-1.0 to 1.0 is 2.0 units)
        float cellHeight = 2.0f / rows; // Screen space height (-1.0 to 1.0 is 2.0 units)

        glfwPollEvents();
        glClear(GL_COLOR_BUFFER_BIT);

        // Generate polygons in grid formation
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // Calculate center position of the current grid cell
                float centerX = -1.0f + (col + 0.5f) * cellWidth;
                float centerY = -1.0f + (row + 0.5f) * cellHeight;

                float[] center = {centerX, centerY, 0.0f};
                float[] color = {my_rand.nextFloat(), my_rand.nextFloat(), my_rand.nextFloat(), 1.0f}; // Random color
                int sides = my_rand.nextInt(MAX_SIDES - 2) + 3; // Random sides between 3 and MAX_SIDES

                glColor4f(color[0], color[1], color[2], color[3]);
                glBegin(GL_TRIANGLES);

                final float begin_angle = 0.0f;
                final float end_angle = (float) (2.0f * Math.PI);
                float angle_step = (end_angle - begin_angle) / sides;

                for (int triangle = 0; triangle < sides; triangle++) {
                    float[] v1 = new float[NUM_3D_COORDS];
                    float[] v2 = new float[NUM_3D_COORDS];

                    float angle1 = triangle * angle_step;
                    float angle2 = (triangle + 1) * angle_step;

                    generateSegmentVertices(center, v1, v2, RADIUS, angle1, angle2);

                    glVertex3f(center[0], center[1], center[2]);
                    glVertex3f(v1[0], v1[1], v1[2]);
                    glVertex3f(v2[0], v2[1], v2[2]);
                }
                glEnd();
            }
        }
        my_wm.swapBuffers();
    }

    @Override
    public void render(float radius) {
    }

    @Override
    public void render() {

    }
}
