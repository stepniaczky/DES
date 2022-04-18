package des;

import java.io.*;


public class FileDao implements Dao<SudokuBoard> {

    private final String filename;

    public FileDao(String filename) {
        this.filename = filename;
    }

    @Override
    public SudokuBoard read() throws IOException {
        SudokuBoard obj;
        try (FileInputStream fis = new FileInputStream(filename);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
             obj = (SudokuBoard) ois.readObject();
        } catch (ClassNotFoundException | FileNotFoundException e) {
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
