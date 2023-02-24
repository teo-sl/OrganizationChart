package fileManager.abstractFactory;

import fileManager.converter.ConverterFromFile;
import fileManager.converter.ConverterToFile;

public interface ConverterFactory {
    ConverterFromFile getConverterFrom();
    ConverterToFile getConverterTo();
}
