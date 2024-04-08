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
        this.sk = sk;
    }

    public void save() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("save.dat")));
            
            DataStorage ds = new DataStorage();

            // variables that we're saving
            ds.score = sk.score;
            ds.mailboxesCompleted = sk.mailboxesCompleted;
            ds.level = sk.level;

            // writing to the file save.dat
            oos.writeObject(ds);

        } catch (Exception e) {
            System.out.println("Something's gone wrong with the save!! :( )");
        }
        
    }

    public void loadSave() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("save.dat")));

            DataStorage ds = (DataStorage) ois.readObject();

            // returning the values we're saving
            sk.score = ds.score;
            sk.mailboxesCompleted = ds.mailboxesCompleted;
            sk.level = ds.level;

        } catch (Exception e) {
            System.out.println("Something's gone wrong with the load!! :( )");
        }
    } 
    
}
