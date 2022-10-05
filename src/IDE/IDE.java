package IDE;

import IDE.Utils.Executor;

import java.awt.*;
import java.awt.event.KeyEvent;

import static IDE.Utils.Utils.writeToFile;
import static IDE.Utils.Utils.fileToString;

public class IDE {
    private String currentFileDir;
    private String buffer = "lmao;bruh thats dum\nknai;aaaa";

    private final Executor executor = new Executor();
    private final Lang lang;

    public IDE(Lang lang, String currentFileDir) {
        this.currentFileDir = currentFileDir;
        this.lang = lang;

        load();
    }

    public void drawContents(Graphics g) {
        g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 18));
        FontMetrics gMetrics = g.getFontMetrics();
        int initalH = gMetrics.getAscent(), dh = gMetrics.getAscent()+gMetrics.getDescent();
        String[] content = buffer.split("[\n]");

        for (int line = 0; line < content.length; ++line) {g.drawString(content[line], 3, initalH+(dh*line));}
    }

    public int getHeight(Graphics g) {
        FontMetrics gMetrics = g.getFontMetrics();
        String[] content = buffer.split(lang.end+"");
        return gMetrics.getHeight()*content.length;
    }

    public void edit(int keycode) {
        buffer += KeyEvent.getKeyText(keycode);
    }

    public void setFile(String newDir) {save(); currentFileDir = newDir; load();}
    public void load() {buffer = fileToString(currentFileDir);}
    public void save() {writeToFile(buffer, currentFileDir);}
}
