package pkgGeneratePolygons;

import pkgSlRenderer.RenderEngine;

import java.util.Random;
import java.lang.Thread;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL11.*;


public class GeneratePolygons extends RenderEngine {
    private float RADIUS = 0.05f;
    private int MAX_SIDES = 40;
    private int MAX_POLYGONS = 5;
    private final int NUM_RGBA = 4;
    private final int NUM_3D_COORDS = 3;
    private int UPDATE_INTERVAL = 500;
    private float[][] rand_colors;
    private float[][] rand_coords;
    private float[][] coords;
    private float[][] colors;
    private int[] rand_sides;
    private int currentSideCount = 3; // Start with 3 sides
    private long lastSideUpdateTime = System.currentTimeMillis(); // Track the last time sides were updated

    Random my_rand;

    public GeneratePolygons() {
        my_rand = new Random();
        rand_colors = new float[MAX_POLYGONS][NUM_RGBA];
        rand_coords = new float[MAX_POLYGONS][NUM_3D_COORDS];
        rand_sides = new int[MAX_POLYGONS];
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
//    public void drawPolygon(float centerX, float centerY) {
//        // Generate a random color (RGBA)
//        float[] color = {
//                0.0f, // Red
//                3.0f, // Green
//                1.0f, // Blue
//                1.0f  // Alpha (fully opaque)
//        };
//
//        // Set the color for the polygon
//        glColor4f(color[0], color[1], color[2], color[3]);
//
//        glBegin(GL_TRIANGLE_FAN);
//        int sides = 20; // Number of sides for the polygon
//        for (int i = 0; i < sides; i++) {
//            // Calculate the angle for each vertex
//            float angle = (float) (2.0f * Math.PI / sides * i);
//            float x = centerX + RADIUS * (float) Math.cos(angle); // X coordinate
//            float y = centerY + RADIUS * (float) Math.sin(angle); // Y coordinate
//            glVertex3f(x, y, 0.0f); // Specify the vertex (Z coordinate is 0)
//        }
//        glEnd();
//    }

    public void drawGrid(int rows, int cols, float spacing) {
        // Calculate half width and height for centering
        float halfWidth = (cols - 1) * spacing / 2;
        float halfHeight = (rows - 1) * spacing / 2;

        // Generate a random color (RGBA)
        float[] color = {
                my_rand.nextFloat(), // Red
                my_rand.nextFloat(), // Green
                my_rand.nextFloat(), // Blue
                1.0f  // Alpha (fully opaque)
        };

        // Set the color for the polygon
        glColor4f(color[0], color[1], color[2], color[3]);
        // Loop through each row and column
        try {
            Thread.sleep(500);
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    // Calculate the center position for each polygon
                    float centerX = col * spacing - halfWidth; // Centering the grid horizontally
                    float centerY = row * spacing - halfHeight; // Centering the grid vertically

                    drawPolygon(centerX, centerY, color); // Draw the polygon at calculated position
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
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

    public void updateRadiusBasedOnWindowSize(int rows, int cols) {
        int[] windowSize = my_wm.getCurrentWindowSize(); // Get current window size
        int width = windowSize[0];
        int height = windowSize[1];


        float availableWidth = width / (float) cols;
        float availableHeight = height / (float) rows;

        // Calculate the radius as half of the smaller dimension available for each polygon
        float radius = Math.min(availableWidth, availableHeight) / 10.0f; // Adjust this divisor as needed

        // Ensure the radius does not go below the minimum value
        radius = Math.max(radius, 0.05f); // Set minimum radius
        setRadius(radius); // Update the radius for the polygons
    }

    @Override
    public void genPolygons(float[] center, float radius, int sides) {
        //2d array with coords
        //array with sides

    }



    @Override
    public void render(int frameDelay, int rows, int cols) {

    }

    @Override
    public void render(float radius) {
    }

    @Override
    public void render() {
//        updateRadiusBasedOnWindowSize(20, 20);
        //based on window size determine polygon radius to fit in window
        while (!my_wm.isGlfwWindowClosed()) {
            glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT);

            drawGrid(20, 20, 0.1f); // Example: 5 rows, 5 columns, and spacing of 0.4 units

            my_wm.swapBuffers();
        }
    }

    public void drawPolygon(float centerX, float centerY, float[] color) {
        // Update the current number of sides
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastSideUpdateTime > UPDATE_INTERVAL) {
            currentSideCount++;
            if (currentSideCount > 20) {
                currentSideCount = 3; // Reset to 3 sides after reaching 20
            }
            lastSideUpdateTime = currentTime; // Reset the timer
        }

//        // Generate a random color (RGBA)
//        float[] color = {
//                my_rand.nextFloat(), // Red
//                my_rand.nextFloat(), // Green
//                my_rand.nextFloat(), // Blue
//                1.0f  // Alpha (fully opaque)
//        };
//
//        // Set the color for the polygon
        glColor4f(color[0], color[1], color[2], color[3]);

        glBegin(GL_TRIANGLE_FAN);
        for (int i = 0; i < currentSideCount; i++) {
            // Calculate the angle for each vertex
            float angle = (float) (2.0f * Math.PI / currentSideCount * i);
            float x = centerX + RADIUS * (float) Math.cos(angle); // X coordinate
            float y = centerY + RADIUS * (float) Math.sin(angle); // Y coordinate
            glVertex3f(x, y, 0.0f); // Specify the vertex (Z coordinate is 0)
        }
        glEnd();
    }


}






