package pkgDriver;

import pkgGeneratePolygons.GeneratePolygons;
import pkgSlRenderer.RenderEngine;
import pkgSlUtils.slWindowManager;

import static pkgDriver.slSpot.*;

public class csc133Driver {
    public static void main(String[] my_args) {
        RenderEngine my_re = new GeneratePolygons();
        slWindowManager.get().initGLFWWindow(WIN_WIDTH, WIN_HEIGHT, "CSUS CSC133");
        my_re.initOpenGL(slWindowManager.get());
//        final int FRAME_DELAY = 700, NUM_ROWS = 4, NUM_COLS = 4;
        my_re.genPolygons();
    } // public static void main(String[] my_args)
} // public class csc133Driver(...)
