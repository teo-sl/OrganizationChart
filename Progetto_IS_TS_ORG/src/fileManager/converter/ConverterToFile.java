package fileManager.converter;


public interface ConverterToFile {
    void convertDb();
    boolean saveToFile(String path);
}
