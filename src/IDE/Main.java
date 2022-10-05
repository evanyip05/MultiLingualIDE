package IDE;

import IDE.Utils.Executor;
import IDE.Utils.Wrapper;

import java.awt.*;

public class Main {

    public static void main(String[] args) {
        UserInterface userInterface = new UserInterface(new Dimension(100,500), new Dimension(500, 500), new IDE(Lang.JAVA, "./Files/test.java"));
    }
}

/*./

 */