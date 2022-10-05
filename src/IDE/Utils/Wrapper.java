package IDE.Utils;

/** way to contain some object with a getter and setter */
public class Wrapper<E> {
    private E val;

    public Wrapper(E val) {this.val = val;}

    public E getVal() {return val;}
    public void setVal(E val) {this.val = val;}

    @Override public String toString() {return val + "";}
}