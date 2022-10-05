package IDE.Utils;

/** requires that implementation can move itself using xy deltas */

public final class Movable {
    public interface Double {void translate(double dx, double dy);}
    public interface Int {void translate(int dx, int dy);}
}
