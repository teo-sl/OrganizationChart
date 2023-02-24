package fileManager;

import fileManager.abstractFactory.ConverterFactories;
import fileManager.abstractFactory.ConverterFactory;
import fileManager.converter.ConverterFromFile;
import fileManager.converter.ConverterToFile;

public class ConverterMediator {
    private final ConverterFromFile loader;
    private final ConverterToFile saver;
    private final ConverterFactory cf;

    public ConverterMediator() {
        cf= ConverterFactories.JsonFactory;
        loader=cf.getConverterFrom();
        saver=cf.getConverterTo();
    }
    public boolean save2File(String path) {
        saver.convertDb();
        return saver.saveToFile(path);
    }
    public boolean loadFromFile(String path) {
        return loader.convertFromFile(path);
    }
}
