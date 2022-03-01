package pl.first.firstjava;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;



public class FileSudokuBoardDao implements Dao<SudokuBoard> {

    private final String filename;

    public FileSudokuBoardDao(String filename) {
        this.filename = filename;
    }

    @Override
    public SudokuBoard read() {
        SudokuBoard obj = null;
        try (FileInputStream fis = new FileInputStream(filename);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
             obj = (SudokuBoard) ois.readObject();
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }

    @Override
    public void write(SudokuBoard obj) {
        try (FileOutputStream fos = new FileOutputStream(filename);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(obj);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
    }
}
