package IDE.Utils;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

/** key and mouse/scroll listener in one with key and mouseButton bindings */
public class Listener implements KeyListener, MouseListener, MouseWheelListener {
    private final ArrayList<Integer> inputs = new ArrayList<>();

    // mouseMaps for press/release
    private final HashMap<Integer/*buttonNum*/, Consumer<MouseEvent>> mousePressMap = new HashMap<>();
    private final HashMap<Integer/*buttonNum*/, Consumer<MouseEvent>> mouseReleaseMap = new HashMap<>();

    // keyMaps for press/release
    private final HashMap<Integer/*keyCode*/, Consumer<KeyEvent>> keyPressMap = new HashMap<>();
    private final HashMap<Integer/*keyCode*/, Consumer<KeyEvent>> keyReleaseMap = new HashMap<>();

    // false = released, true = pressed
    private final HashMap<Integer, Wrapper<Boolean>> keyMapStates = new HashMap<>();
    private final HashMap<Integer, Wrapper<Boolean>> mouseMapStates = new HashMap<>();

    // actions for events
    private Consumer<MouseWheelEvent> onScroll = mouseWheelEvent -> {};
    private Consumer<MouseEvent> onMousePress = mouseEvent -> {}, onMouseRelease = mouseEvent -> {};
    private Consumer<KeyEvent> onKeyPress = keyEvent -> {}, onKeyRelease = keyEvent -> {};
    private Runnable onEnable = () -> {}, onDisable = () -> {};

    // activity state
    private boolean enabled = true;
    private int input;

    /** get keys being pressed. uses KeyEvent.keyCode */
    public ArrayList<Integer> getInputs() {return new ArrayList<>(inputs);}

    /** executes bind action once, sets key map key state to true */
    @Override public void keyPressed(KeyEvent e) {
        if (!inputs.contains(e.getExtendedKeyCode())) {inputs.add(e.getExtendedKeyCode());}
        input = e.getExtendedKeyCode();
        if (enabled) {onKeyPress.accept(e); if (keyPressMap.get(e.getExtendedKeyCode()) != null) {keyPressMap.get(e.getExtendedKeyCode()).accept(e);}}
        if (keyMapStates.get(e.getExtendedKeyCode()) != null) {keyMapStates.get(e.getExtendedKeyCode()).setVal(true);}
    }
    /** executes bind action, sets key map key state to false */
    @Override public void keyReleased(KeyEvent e) {
        inputs.removeIf(input -> input.equals(e.getExtendedKeyCode()));
        input = (inputs.size()>0)?inputs.get(0):0; // sets current input to if there are still keys being held to the next held else 0 for no key
        if (enabled) {onKeyRelease.accept(e); if (keyReleaseMap.get(e.getExtendedKeyCode()) != null) {keyReleaseMap.get(e.getExtendedKeyCode()).accept(e);}}
        if (keyMapStates.get(e.getExtendedKeyCode()) != null) {keyMapStates.get(e.getExtendedKeyCode()).setVal(false);}
    }
    /** executes bind action once, sets mouse map button state to true */
    @Override public void mousePressed(MouseEvent e) {
        if (enabled) {onMousePress.accept(e); if (mousePressMap.get(e.getButton()) != null) {mousePressMap.get(e.getButton()).accept(e);}}
        if (mouseMapStates.get(e.getButton()) != null) {mouseMapStates.get(e.getButton()).setVal(true);}
    }
    /** executes bind action, sets mouse map button state to false */
    @Override public void mouseReleased(MouseEvent e) {
        if (enabled) {onMouseRelease.accept(e); if (mouseReleaseMap.get(e.getButton()) != null) {mouseReleaseMap.get(e.getButton()).accept(e);}}
        if (mouseMapStates.get(e.getButton()) != null) {mouseMapStates.get(e.getButton()).setVal(false);}
    }
    /** executes scroll action */
    @Override public void mouseWheelMoved(MouseWheelEvent e) {if (enabled) {onScroll.accept(e);}}

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    /** add bind for button using MouseEvent.button on press*/
    public void addMousePressBind(Integer button, Consumer<MouseEvent> action) {mousePressMap.put(button, action); mouseMapStates.put(button, new Wrapper<>(false));}
    /** add bind for button using MouseEvent.button on release*/
    public void addMouseReleaseBind(Integer button, Consumer<MouseEvent> action) {mouseReleaseMap.put(button, action);}
    /** add bind for key using KeyEvent.keyCode on press */
    public void addKeyPressBind(Integer keyCode, Consumer<KeyEvent> action) {keyPressMap.put(keyCode, action); keyMapStates.put(keyCode, new Wrapper<>(false));}
    /** add bind for key using KeyEvent.keyCode on release */
    public void addKeyReleaseBind(Integer keyCode, Consumer<KeyEvent> action) {keyReleaseMap.put(keyCode, action);}

    /** add action that happens on disable */
    public void setOnDisable(Runnable onDisable) {this.onDisable = onDisable;}
    /** add action that happens on enable */
    public void setOnEnable(Runnable onEnable) {this.onEnable = onEnable;}
    /** add action that happens on key press */
    public void setOnKeyPress(Consumer<KeyEvent> onKeyPress) {this.onKeyPress = onKeyPress;}
    /** add action that happens on key release */
    public void setOnKeyRelease(Consumer<KeyEvent> onKeyRelease) {this.onKeyRelease = onKeyRelease;}
    /** add action that happens on mouse press */
    public void setOnMousePress(Consumer<MouseEvent> onMousePress) {this.onMousePress = onMousePress;}
    /** add action that happens on mouse release */
    public void setOnMouseRelease(Consumer<MouseEvent> onMouseRelease) {this.onMouseRelease = onMouseRelease;}
    /** add action that happens on mouse scroll */
    public void setOnScroll(Consumer<MouseWheelEvent> onScroll) {this.onScroll = onScroll;}

    /** remove mouse press/release binding by button*/
    public void removeMouseBind(Integer button) {mousePressMap.remove(button); mouseReleaseMap.remove(button); mouseMapStates.remove(button);}
    /** remove key press/release binding by keyCode*/
    public void removeKeyBind(Integer keyCode) {keyPressMap.remove(keyCode); keyReleaseMap.remove(keyCode); keyMapStates.remove(keyCode);}

    /** enable listener binds (listener still aware of states) */
    public void enable() {onEnable.run(); enabled = true;}
    /** disable listener binds (listener still aware of states) */
    public void disable() {onDisable.run(); enabled = false;}

    /** get current input, if multiple keys are pressed, the oldest input gets read */
    public int getCurrentInput() {return input;}

    /** return if listener is disabled */
    public boolean disabled() {return !enabled;}
    /** return if listener is enabled */
    public boolean enabled() {return enabled;}

    /** check if key is pressed using KeyEvent.keyCode, false if not binded*/
    public boolean keyPressed(Integer keyCode) {return keyMapStates.get(keyCode) != null && keyMapStates.get(keyCode).getVal();}
    /** check if key is released using KeyEvent.keyCode, true if not binded*/
    public boolean keyReleased(Integer keyCode) {return keyMapStates.get(keyCode) == null || !keyMapStates.get(keyCode).getVal();}
    /** check if mouse is pressed using MouseEvent.button, false if not binded*/
    public boolean mousePressed(Integer button) {return mouseMapStates.get(button) != null && mouseMapStates.get(button).getVal();}
    /** check if mouse is released using MouseEvent.button, true if not binded*/
    public boolean mouseReleased(Integer button) {return mouseMapStates.get(button) == null || !mouseMapStates.get(button).getVal();}
}
