package IDE.Utils;

import javax.swing.*;

/** subclass of JFrame  */
public class Frame extends JFrame implements Movable.Int {
    public Frame(boolean undecorated) {
        setUndecorated(undecorated);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    @Override public void translate(int dx, int dy) {setLocation(getX()+dx, getY()+dy);}
}