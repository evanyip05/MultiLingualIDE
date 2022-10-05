package IDE;

import IDE.Utils.*;
import IDE.Utils.Frame;
import IDE.Utils.Panel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

public class UserInterface {
    private final Frame frame;
    private final Panel panel;

    private final Listener listener = new Listener();
    private final MouseFollower<Frame> frameFollower;
    private final IDE ide;

    private final int menuBar = 24, space = 8;

    private final ExtendableThread render = new ExtendableThread() {
        @Override public void execute() throws InterruptedException {
            render();
            wait(16);
        }
    };

    private Dimension size;

    public UserInterface(Dimension dir, Dimension edit, IDE ide) {
        this.ide = ide;

        size = new Dimension(space*3+edit.width+dir.width, menuBar+(space*2)+Math.max(edit.height, dir.height));
        panel = new Panel(size);
        frame = new Frame(true);
        frameFollower = new MouseFollower<>(frame);

        // edit and dir are fit to screen rn (will be dynamic with content)
        panel.addBuffer(new BufferedImage(edit.width, edit.height, BufferedImage.TRANSLUCENT), edit, new Point(space*2+dir.width, menuBar+space), new Point(0,0));
        panel.addBuffer(new BufferedImage(dir.width, dir.height, BufferedImage.TRANSLUCENT), dir, new Point(space, menuBar+space), new Point(0,0));
        panel.addBuffer(new BufferedImage(size.width, menuBar, BufferedImage.TRANSLUCENT), new Dimension(size.width, menuBar), new Point(0,0), new Point(0,0));


        panel.setUnderlayCommand(g -> {
            g.setColor(Color.GRAY);
            g.fillRect(0,0, size.width, size.height);
        });

        panel.addMouseListener(listener);
        panel.addKeyListener(listener);

        frame.add(panel);
        frame.pack();

        frame.setLocation(100, 100);

        setupListener();

        render.start();
    }

    public int getCursorBuffer() {
        Wrapper<Integer> buffer = new Wrapper<>(-1);
        Wrapper<Integer> bufferNum = new Wrapper<>(0);

        panel.forEachBuffer(buffer1 -> {if(panel.checkBuffer(bufferNum.getVal(), buffer2 -> buffer2.contains(panel.getRelCursorPos()))){buffer.setVal(bufferNum.getVal());}bufferNum.setVal(bufferNum.getVal()+1);});

        return buffer.getVal();
    }

    public void setupListener() {
        listener.setOnMousePress(mouseEvent -> panel.requestFocus());
        
        listener.addMousePressBind(MouseEvent.BUTTON1, mouseEvent -> {if (getCursorBuffer() == 2){frameFollower.startFollowing();}});
        listener.addMouseReleaseBind(MouseEvent.BUTTON1, mouseEvent -> frameFollower.stopFollowing());

        listener.addKeyPressBind(KeyEvent.VK_ESCAPE, keyEvent -> System.exit(0));
    }

    public void resize(Dimension edit, Dimension dir) {
        size = new Dimension(space*3+edit.width+dir.width, menuBar+(space*2)+Math.max(edit.height, dir.height));
        frame.setSize(size);
        panel.setSize(size);
    }

    public void render() {
        Graphics g1 = panel.getBufferGraphics(0);
        Graphics g2 = panel.getBufferGraphics(1);
        Graphics g3 = panel.getBufferGraphics(2);

        g1.setColor(Color.DARK_GRAY);
        g2.setColor(Color.DARK_GRAY);
        g3.setColor(Color.DARK_GRAY);

        panel.useBuffer(0, edit -> g1.fillRect(0, 0, edit.getViewDimension().width, edit.getViewDimension().height));
        panel.useBuffer(1, edit -> g2.fillRect(0, 0, edit.getViewDimension().width, edit.getViewDimension().height));
        panel.useBuffer(2, edit -> g3.fillRect(0, 0, edit.getViewDimension().width, edit.getViewDimension().height));

        g1.setColor(Color.WHITE);
        ide.drawContents(g1);

        panel.repaint();
    }
}
