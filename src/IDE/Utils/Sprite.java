package IDE.Utils;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public final class Sprite {
    public static abstract class Int implements Drawable, Movable.Int {
        private final ArrayList<Point> references = new ArrayList<>();
        private final Rectangle bounds;
        private final Point inital;

        public Int(int x, int y, int width, int height) {this.bounds = new Rectangle(x, y, width, height); this.inital = new Point(x, y);}

        public boolean contains(Point point) {return bounds.contains(point);}
        public boolean atRefX(int index) {return getX() == references.get(index).x;}
        public boolean atRefY(int index) {return getY() == references.get(index).y;}

        public int getX() {return bounds.x;}
        public int getY() {return bounds.y;}
        public int getInitalX() {return inital.x;}
        public int getInitalY() {return inital.y;}
        public int getDisplacementX() {return getX() - inital.x;}
        public int getDisplacementY() {return getY() - inital.y;}
        public int getWidth() {return bounds.width;}
        public int getHeight() {return bounds.height;}

        public int getXRefDistance(int index) {return getX() - references.get(index).x;}
        public int getYRefDistance(int index) {return getY() - references.get(index).y;}

        /** reference point can be added to compare position later*/
        public void addRef(Point ref) {references.add(ref);}
        /** add current pos to reference list */
        public void savePos() {references.add(new Point(getX(), getY()));}
        public void setXY(int x, int y) {bounds.setLocation(x, y);}
        public void translate(int dx, int dy) {bounds.translate(dx, dy);}
    }

    public static abstract class Double implements Drawable, Movable.Double {
        private final ArrayList<Point2D> references = new ArrayList<>();
        private final Rectangle2D bounds;
        private final Point2D inital;

        public Double(double x, double y, double width, double height) {this.bounds = new Rectangle2D.Double(x, y, width, height); this.inital = new Point2D.Double(x, y);}

        public boolean contains(Point point) {return bounds.contains(point);}
        public boolean atRefX(int index) {return getX() == references.get(index).getX();}
        public boolean atRefY(int index) {return getY() == references.get(index).getY();}

        public double getX() {return bounds.getX();}
        public double getY() {return bounds.getY();}
        public double getInitalX() {return inital.getX();}
        public double getInitalY() {return inital.getY();}
        public double getDisplacementX() {return getX() - inital.getX();}
        public double getDisplacementY() {return getY() - inital.getY();}
        public double getWidth() {return bounds.getWidth();}
        public double getHeight() {return bounds.getHeight();}

        public double getXRefDistance(int index) {return getX() - references.get(index).getX();}
        public double getYRefDistance(int index) {return getY() - references.get(index).getY();}

        /** reference point can be added to compare position later*/
        public void addRef(Point2D ref) {references.add(ref);}
        /** add current pos to reference list */
        public void savePos() {references.add(new Point2D.Double(getX(), getY()));}
        public void setXY(int x, int y) {bounds.setRect(x, y, getWidth(), getHeight());}
        public void translate(int dx, int dy) {bounds.setRect(getX()+dx, getY()+dy,getWidth(),getHeight());}
    }
}
