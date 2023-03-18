package local.hackathon.util;

import java.io.*;
import java.util.HashMap;

public class Scoreboard {
    public static File SCORE_FILE = new File("score.ser");

    public static HashMap<String, Integer> scores = new HashMap<>();

    public static void serialize(){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SCORE_FILE))){
            oos.writeObject(scores);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void deSerialize(){
        if(!SCORE_FILE.exists()){
            serialize();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SCORE_FILE))){
            scores = (HashMap<String, Integer>) ois.readObject();
        } catch (IOException | ClassNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
    }
}
