package des;

import java.io.*;


public class FileDao implements Dao<String> {

    private final String filename;

    public FileDao(String filename) {
        this.filename = filename;
    }

    @Override
    public String read() throws IOException {
        StringBuilder obj = new StringBuilder();
        try (FileReader fileReader = new FileReader(filename);) {
            int i;
            while ((i = fileReader.read()) != -1)
                obj.append((char) i);
        } catch (FileNotFoundException e) {
            return null;
        }
        return obj.toString();
    }

    @Override
    public void write(String obj) {
        try (FileWriter fileWriter = new FileWriter(filename);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
             printWriter.print(obj);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
    }
}
