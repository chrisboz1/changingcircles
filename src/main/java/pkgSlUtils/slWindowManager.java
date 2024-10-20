package pkgSlUtils;
import pkgDriver.slSpot;


import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class slWindowManager {


    private long glfw_win = 0;
    private static slWindowManager my_window;

    private slWindowManager(int width, int height, String label) {
        if (glfw_win != 0) {
            glfw_win = glfwCreateWindow(width, height, label, NULL, NULL);
//            glfwMakeContextCurrent(glfw_win);
        }

    }


    public int[] getCurrentWindowSize() {
        int[] width = new int[1];
        int[] height = new int[1];
        glfwGetWindowSize(glfw_win, width, height);
        return new int[]{width[0], height[0]};
    }

    public static slWindowManager get() {
        if(my_window == null) {
            my_window = new slWindowManager(slSpot.WIN_WIDTH, slSpot.WIN_HEIGHT, "CSC 133");

        }
        return my_window;
    }

    public void destroyGlfwWindow() {
        glfwDestroyWindow(glfw_win);
    }

    public void swapBuffers() {
        glfwSwapBuffers(glfw_win);
    }

    public boolean isGlfwWindowClosed() {
        return glfwWindowShouldClose(glfw_win);
    }

    public long initGLFWWindow(int width, int height, String label) {
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        glfw_win = glfwCreateWindow(width, height, label, NULL, NULL);
        glfwMakeContextCurrent(glfw_win);
        return glfw_win;

    }

    public int[] getWindowSize(){
        return new int[]{};
    }

    public void updateContextToThis() {
        glfwMakeContextCurrent(glfw_win);
    }




}
