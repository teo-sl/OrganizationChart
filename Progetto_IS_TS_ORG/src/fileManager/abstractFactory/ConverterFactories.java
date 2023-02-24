package fileManager.abstractFactory;

import fileManager.converter.json.Converter2Json;
import fileManager.converter.json.ConverterFromJson;
import fileManager.converter.ConverterFromFile;
import fileManager.converter.ConverterToFile;

public enum ConverterFactories implements ConverterFactory{

    JsonFactory {
        @Override
        public ConverterFromFile getConverterFrom() {
            return new ConverterFromJson();
        }

        @Override
        public ConverterToFile getConverterTo() {
            return new Converter2Json();
        }
    }
}
