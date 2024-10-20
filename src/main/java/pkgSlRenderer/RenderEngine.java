package pkgSlRenderer;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import pkgSlUtils.slWindowManager;

import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public abstract class RenderEngine {


    protected slWindowManager my_wm;
    Random my_rand = new Random();


    public abstract void render(int frameDelay, int rows, int cols);
    public abstract void render(float radius);
    public abstract void render();








}
