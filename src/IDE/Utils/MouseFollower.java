package IDE.Utils;

import java.awt.*;
import java.util.function.Consumer;

/** class that moves some moveable subclass with the mouse pointer */
public class MouseFollower<E extends Movable.Int> extends ExtendableThread {
    private final Wrapper<Boolean> following = new Wrapper<>(false);
    private final Point mouseInital = MouseInfo.getPointerInfo().getLocation();
    private E target;

    /** target must implement movable */
    public MouseFollower(E target) {this.target = target;}

    // follow code
    @Override public void execute() throws InterruptedException {
        Point cursor = MouseInfo.getPointerInfo().getLocation();
        int mDeltaX = (int) (cursor.getX()-mouseInital.getX());
        int mDeltaY = (int) (cursor.getY()-mouseInital.getY());

        if (target!=null) {target.translate(mDeltaX, mDeltaY);}

        mouseInital.setLocation(cursor);
        wait(1000/60);
    }

    // when to follow condition used to tell the extendable thread when to wait and not execute follow code
    @Override public boolean waitCondition() {return !following.getVal();}

    /** change the target */
    public void setTarget(E target) {this.target = target;}

    /** use the target without major side effects (setting the target to something else) still may change the targets state */
    public void useTarget(Consumer<E> action) {if (target!=null) {action.accept(target);}}

    /** have the target start following the cursor */
    public void startFollowing() {following.setVal(true);restart(); mouseInital.setLocation(MouseInfo.getPointerInfo().getLocation());}

    /** have the target stop following the cursor */
    public void stopFollowing() {following.setVal(false); }
}