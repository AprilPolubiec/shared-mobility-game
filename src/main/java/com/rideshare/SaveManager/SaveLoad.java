package com.rideshare.SaveManager;

import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;

import com.rideshare.ScoreKeeper;

import javafx.scene.chart.PieChart.Data;

public class SaveLoad {

    ScoreKeeper sk;

    public SaveLoad(ScoreKeeper scoreKeeper) {
        this.sk = scoreKeeper;
    }

    public void save(String fileName, DataStorage data) {
        try {
            // Create a FileOutputStream in append mode
            OutputStream outputStream = new FileOutputStream(fileName, true);
            // Wrap it with a BufferedOutputStream for better performance
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            // Create ObjectOutputStream with BufferedOutputStream
            ObjectOutputStream oos = new ObjectOutputStream(bufferedOutputStream);
            // Write the serialized DataStorage object to the file
            oos.writeObject(data);
            System.out.println("Game state appended to: " + fileName);
            oos.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Failure to append game state to: " + fileName);
            e.printStackTrace();
        }
    }

    public void load(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(fileName)))) {
            DataStorage ds = (DataStorage) ois.readObject();

            sk.setScore(ds.score);
            sk.setMailboxesCompleted(ds.mailboxesCompleted);
            sk.setLevel(ds.level);
            sk.setCO2Saved(ds.CO2Saved);
            sk.setCO2Used(ds.CO2Used);
            sk.setTotalMailboxes(ds.totalMailboxes);

        } catch (Exception e) {
            System.out.println("Something's gone wrong with the load!! :( )");
        }
    }

}
