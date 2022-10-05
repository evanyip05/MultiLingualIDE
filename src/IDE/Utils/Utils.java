package IDE.Utils;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.*;
import java.nio.file.Path;
import java.util.*;

public final class Utils {

    /** swap a hashMap's keys and values */
    public static<K,V> HashMap<V,K> invertMap(HashMap<K,V> map) {
        HashMap<V,K> res = new HashMap<>(); map.forEach((key, val) -> res.put(val, key)); return res;
    }

    /** list non directory filenames in some directory */
    public static ArrayList<String> listFiles(String dir) {
        File[] files = new File(dir).listFiles();
        ArrayList<String> names = new ArrayList<>();
        for (File file : files) {if (!file.isDirectory()) {names.add(file.getName());}}
        return names;
    }

    /** returns string from a text file using a directory, returns an empty string if read fails*/
    public static String fileToString(String dir) {
        String res = "";

        try {
            Scanner reader = new Scanner(Path.of(dir));
            while (reader.hasNext()) {res += reader.nextLine()+"\n";}
        }
        catch (IOException e) {System.out.println("file did not exist or could not read");}

        return res;
    }

    /** overwrite a file with a string*/
    public static void writeToFile(String dir, String content) {
        try {FileWriter writer = new FileWriter(dir); writer.write(content); writer.close();}
        catch (IOException e) {System.out.println("file does not exist or could not write");}
    }

    /** draw a point using something's graphics */
    public static void drawPoint(Point2D p, Graphics g, int size) {g.drawRoundRect((int)(p.getX()-(size/2)), (int) (p.getY()-(size/2)), size,size,size,size );}

    /** draw a line using something's graphics */
    public static void drawLine(Point2D a, Point2D b, Graphics g) {g.drawLine((int)a.getX(),(int) a.getY(),(int) b.getX(), (int)b.getY());}

    /** fill tri */
    public static void fillFace(Point2D a, Point2D b, Point2D c, Graphics g) {g.fillPolygon(new int[]{(int) a.getX(), (int) b.getX(), (int) c.getX()}, new int[] {(int) a.getY(), (int) b.getY(), (int) c.getY()}, 3);}

    /** fill poly */
    public static void fillFace(ArrayList<Point2D> points, Graphics g) {
        int[] xVals = new int[points.size()];
        int[] yVals = new int[points.size()];

        for (int i = 0; i < points.size()-1; ++i) {
            xVals[i] = (int) points.get(i).getX();
            yVals[i] = (int) points.get(i).getY();
        }

        g.fillPolygon(xVals, yVals, points.size());
    }

    /** get the reduced numerator for some denominator */
    public static int reduceNumerator(int numerator, int denominator) {return (numerator/gcf(numerator, denominator));}

    /** get the reduced denominator for some numerator */
    public static int reduceDenominator(int numerator, int denominator) {return (denominator/gcf(numerator, denominator));}

    /** greatest common factor using Euclid's algorithm */
    public static int gcf(int int1, int int2) {
        int biggest, smallest, temp;

        if (int1 > int2) {
            biggest = int1;
            smallest = int2;
        } else {
            biggest = int2;
            smallest = int1;
        }

        while (!(biggest%smallest == 0)) {
            temp = smallest;
            smallest = biggest%smallest;
            biggest = temp;
        }

        return smallest;
    }
}

/*

 */