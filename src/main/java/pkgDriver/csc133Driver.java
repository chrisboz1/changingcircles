package pkgDriver;

import pkgGeneratePolygons.GeneratePolygons;
import pkgSlRenderer.RenderEngine;
import pkgSlUtils.slWindowManager;

import static pkgDriver.slSpot.*;

public class csc133Driver {
    public static void main(String[] my_args) {
        RenderEngine my_re = new GeneratePolygons();
        // Get the window manager instance
        slWindowManager my_window_manager = slWindowManager.get();
        // Initialize the GLFW window
        my_window_manager.initGLFWWindow(WIN_WIDTH, WIN_HEIGHT, "CSUS CSC133");
        // Initialize OpenGL with the window manager
        my_re.initOpenGL(my_window_manager);
//        my_re.drawRandPolygons();
        my_re.render(40, 4, 5);


//        slWindowManager.get().initGLFWWindow(WIN_WIDTH, WIN_HEIGHT, "CSUS CSC133");
//        my_re.initOpenGL(slWindowManager.get());
//        final int FRAME_DELAY = 700, NUM_ROWS = 4, NUM_COLS = 4;

    } // public static void main(String[] my_args)
} // public class csc133Driver(...)
