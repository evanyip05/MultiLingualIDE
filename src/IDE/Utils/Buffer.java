package IDE.Utils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Buffer implements Drawable {
    private BufferedImage buffer;
    private final Point camera, dest;
    private final Dimension targetSize;

    private double camScale = 1;

    /**
     * BufferedImage interface that utilizes a point as a camera, and a dimension defining its view
     * also takes a second point to define where the cameras image or output is drawn to
     */
    public Buffer(BufferedImage buffer, Dimension targetSize, Point dest, Point camera) {
        this.targetSize = targetSize;
        this.buffer = buffer;
        this.camera = camera;
        this.dest = dest;
    }

    public Point getCameraLoc() {return new Point(camera.x, camera.y);}
    public Point getDestLoc() {return new Point(dest.x, dest.y);}

    public Dimension getViewDimension() {return new Dimension(targetSize);}
    public Dimension getBufferDimension() {return new Dimension(buffer.getWidth(), buffer.getHeight());}

    /** get the graphics of this buffer */
    public Graphics getGraphics() {return buffer.getGraphics();}

    /** returns if camera can move within the buffer */
    public boolean cameraCanMove(int dx, int dy) {
        boolean xValid = camera.x+dx >= 0 && (camera.x+targetSize.getWidth()*camScale)+dx <= buffer.getWidth();
        boolean yValid = camera.y+dy >= 0 && (camera.y+targetSize.getHeight()*camScale)+dy <= buffer.getHeight();

        return (dy==0)?xValid:(dx==0)?yValid:xValid&&yValid;
    }

    public boolean contains(Point p) {return new Rectangle(dest, targetSize).contains(p);}

    /** get the scale that the camera is viewing at,
     * ex 2x camera is 2 times the size of the view dim, .5 camera is half the size of the view dim */
    public double getCamScale() {return camScale;}

    /** clear buffer and reset transparency */
    public void resetBuffer() {
        Graphics bg = buffer.getGraphics();
        bg.clearRect(0,0, buffer.getWidth(), buffer.getHeight());
        bg.setColor(new Color(0,0,0,0));
        bg.fillRect(0,0, buffer.getWidth(), buffer.getHeight());
        bg.dispose();
    }

    /** resize buffer to new size (copy over anything that was drawn on buffer) */
    public void setBufferSize(int width, int height) {
        BufferedImage replace = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
        Graphics g = replace.getGraphics();
        g.drawImage(buffer, 0, 0, null);
        g.dispose();
        buffer = replace;
    }

    /** set target size */
    public void setTargetSize(int width, int height) {targetSize.setSize(width, height);}

    /** set cam scale */
    public void setCamScale(double camScale) {this.camScale=camScale;}

    /** set draw destination location */
    public void setDest(int x, int y) {dest.setLocation(x, y);}

    /** move the camera in the buffer */
    public void moveCamera(int dx, int dy) {camera.translate(dx, dy);}

    /** draw buffer image to graphics */
    @Override public void drawSelf(Graphics g) {
        // draw buffer to anywhere, if it has something drawn to the buffer
        g.drawImage(buffer, dest.x, dest.y,
                dest.x+targetSize.width,
                dest.y+targetSize.height,
                camera.x,camera.y,
                (int)(camera.x+targetSize.width*camScale),
                (int) (camera.y+targetSize.height*camScale),
                null
        );

        resetBuffer();
    }
}
/*
buffer, dest.x, dest.y,
                dest.x+targetSize.width,
                dest.y+targetSize.height,
                camera.x,camera.y,
                (int)(camera.x+targetSize.width*camScale),
                (int) (camera.y+targetSize.height*camScale),
                null


        System.out.println(camera.x + " " + camera.y);
        System.out.println(buffer.getWidth() + " " + buffer.getHeight());
        System.out.println((camera.x+targetSize.getWidth()*camScale) + " " + (camera.y+targetSize.getHeight()*camScale) + " \n");
 */
