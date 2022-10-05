package IDE.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Executor {
    private final ArrayList<String[]> executeQueue = new ArrayList<>();
    private final ExtendableThread runner = new ExtendableThread() {
        @Override public void execute() throws InterruptedException {
            try {
                //if (!initalRead) {readTimer.restart();}

                int value = in.read();
                initalRead = false;

                System.out.println(value + ": " + (char) value);
                out.write('a');
                if (value == -1) {running = false;}
            } catch (IOException e) {e.printStackTrace();}
        }

        @Override public void executeOnRestart() {
            in = action.getInputStream();
            out = action.getOutputStream();
        }

        @Override public void executeOnWait() {initalRead = true;}

        @Override public boolean waitCondition() {return !running;}
    };

    private final ExtendableThread readTimer = new ExtendableThread() {
        @Override public void execute() throws InterruptedException {
            wait(100);
            ++time;
        }

        @Override public boolean waitCondition() {return time >= 2;}

        @Override public void executeOnWait() {
            System.out.print("input detected, enter smth: ");
            /*
            char[] input = new Scanner(System.in).nextLine().toCharArray();
            byte[] inputBytes = new byte[input.length];
            for (int i = 0; i < input.length; ++i) {
                inputBytes[i] = (byte) input[i];
            }

             */
            try {
                out.write(111);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override public void executeOnRestart() {time = 0;}
    };

    private Process action;
    private InputStream in;
    private OutputStream out;

    private boolean running = false, initalRead = true;
    private int time = 0;

    public Executor() {}

    public String getActionsStrung(String[] actions) {
        String actionString = "";
        for (String action : actions) {actionString = actionString + action + " && ";}
        return actionString.substring(0, actionString.length()-4);
    }

    public void addTask(String[] actions) {executeQueue.add(actions);}

    public void next() {
        String[] actions = executeQueue.get(0);
        executeQueue.remove(0);

        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", getActionsStrung(actions));

        try {action = builder.start();}
        catch (IOException e) {e.printStackTrace();}

        running = true;
        runner.restart();
    }
}

/*
if (info == 32) {
                    OutputStream output = action.getOutputStream();
                    System.out.println("writing");

                    output.write(111);
                    output.close();
                }
 */