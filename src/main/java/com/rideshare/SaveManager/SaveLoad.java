package com.rideshare.SaveManager;

import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import com.rideshare.ScoreKeeper;

import javafx.scene.chart.PieChart.Data;

public class SaveLoad {

    ScoreKeeper sk;

    public SaveLoad(ScoreKeeper scoreKeeper) {
        this.sk = scoreKeeper;
    }

    public void save(String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(fileName)))) {

            DataStorage ds = new DataStorage();

            ds.score = sk.calculateScore();
            ds.mailboxesCompleted = sk.getMailboxesCompleted();
            ds.level = sk.getLevel();
            ds.CO2Saved = sk.getCO2Saved();
            ds.CO2Used = sk.getCO2Used();
            ds.level = sk.getTotalMailboxes();



            
            oos.writeObject(ds);

        } catch (Exception e) {
            System.out.println("Something's gone wrong with the save!! :( )");
        }

    }

    public void load(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(fileName)))) {
            DataStorage ds = (DataStorage) ois.readObject();

            sk.setScore(ds.score);
            sk.setMailboxesCompleted(ds.mailboxesCompleted);
            sk.setLevel(ds.level);
            sk.setC02Saved(ds.CO2Saved);
            sk.setC02Used(ds.CO2Used);
            sk.setTotalMailboxes(ds.totalMailboxes);
            
            
        } catch (Exception e) {
            System.out.println("Something's gone wrong with the load!! :( )");
        }
    } 
}
