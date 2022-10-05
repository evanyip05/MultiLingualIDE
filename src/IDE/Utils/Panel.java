package IDE.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;

/** subclass of JPanel that uses buffers to create overlays or different layouts manualy using awt.rectangle */
public class Panel extends JPanel {
    private final ArrayList<Wrapper<Boolean>> bufferStates = new ArrayList<>();
    private final ArrayList<Buffer> buffers = new ArrayList<>();
    private Consumer<Graphics> overlayCommand = g -> {};
    private Consumer<Graphics> underlayCommand = g -> {};

    /** implementation of JPanel, size defines panel size */
    public Panel(Dimension size) {
        setSize(size);
        setPreferredSize(size);
        setVisible(true);
    }

    /** get the cursor position relative to the panel */
    public Point getRelCursorPos() {
        Point cursor = MouseInfo.getPointerInfo().getLocation();
        Point panel = getLocationOnScreen();

        return new Point(cursor.x-panel.x, cursor.y-panel.y);
    }

    /** get the graphics of a specific buffer */
    public Graphics getBufferGraphics(int index) {return buffers.get(index).getGraphics();}

    public boolean checkBuffer(int index, Predicate<Buffer> check) {return check.test(buffers.get(index));}

    /** access buffer and its info w/o major side effects?
     * no setting buffer in array to a new value, but can still affect the buffer */
    public void useBuffer(int index, Consumer<Buffer> action) {action.accept(buffers.get(index));}

    public void forEachBuffer(Consumer<Buffer> action) {buffers.forEach(action);}

    /** add a buffer to the panel */
    public void addBuffer(BufferedImage buffer, Dimension targetSize, Point dest, Point camera) {buffers.add(new Buffer(buffer, targetSize, dest, camera)); bufferStates.add(new Wrapper<>(true));}

    /** add a buffer to the panel */
    public void addBuffer(Buffer buffer) {buffers.add(buffer); bufferStates.add(new Wrapper<>(true));}

    public void addBuffer() {addBuffer(new BufferedImage(0,0,BufferedImage.TRANSLUCENT), new Dimension(), new Point(), new Point()); bufferStates.add(new Wrapper<>(true));}

    /** enable buffer to draw to screen */
    public void enableBuffer(int index) {bufferStates.get(index).setVal(true);}
    /** prevent buffer from drawing to screen */
    public void disableBuffer(int index) {bufferStates.get(index).setVal(false);}

    /** set a draw command that gets drawn to the panel over everything on it */
    public void setOverlayCommand(Consumer<Graphics> overlayCommand) {this.overlayCommand = overlayCommand;}

    /** set a draw command that gets drawn to the panel under everything on it */
    public void setUnderlayCommand(Consumer<Graphics> underlayCommand) {this.underlayCommand = underlayCommand;}

    /** draw all the layers at their locations and scales
     * draw order: underlay, buffer draw to panel, buffer clear, overlay
     * ui render sets up buffers, repaint paints buffers to screen */
    public void paint(Graphics g) {
        //System.out.println("cleared");
        g.clearRect(0, 0, getWidth(), getHeight());
        // something gets drawn to buffer buffer draws it to screen and clears itself (internal)
        //System.out.println("drawing to screen... ");
        underlayCommand.accept(g);
        for (int i = 0; i < buffers.size(); ++i) {if (bufferStates.get(i).getVal()) {buffers.get(i).drawSelf(g);}}
        overlayCommand.accept(g);
        //System.out.println("drawn\n");
    }
}

