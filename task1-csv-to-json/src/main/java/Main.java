import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBeanBuilder;
import specialfiles.csv.Employee;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        final var columnMapping = new String[]{"id", "firstName", "lastName", "country", "age"};
        final var inputFileName = "data.csv";
        final var outputFileName = "data.json";
        final var list = parseCSV(columnMapping, inputFileName);
        final var json = listToJson(list);
        writeString(json, outputFileName);
    }

    private static void writeString(String json, String outputFileName) {

        // Стрим JSON-файла
        try (var fileWriter = new FileWriter(outputFileName)) {
            fileWriter.write(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String listToJson(List<Employee> list) {

        // Сериализатор в JSON
        final var gson = new GsonBuilder()
                .enableComplexMapKeySerialization()
                .serializeNulls()
                .setDateFormat(DateFormat.LONG)
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                .setPrettyPrinting()
                .setVersion(1.0)
                .create();

        // Создание new TypeToken<...>() {}.getType() передаёт тип определённый
        // во время компиляции (между < и >) в рантайм объект
        // java.lang.reflect.Type.
        final Type listType = new TypeToken<List<Employee>>() {}.getType();

        // Преобразование списка в JSON
        return gson.toJson(list, listType);
    }

    private static List<Employee> parseCSV(String[] columnMapping, String fileName) {

        // Стрим CSV-файла
        try (var fileReader = new FileReader(fileName)) {

            // Настроенный CSVParser. Реализует разделение одной строки на поля
            // в соответствии с заданными правилами
            final var csvParser = new CSVParserBuilder()
                    .withSeparator(',')
                    .build();

            // Ридер предназначен для чтения CSV-стримов
            final var csvReader = new CSVReaderBuilder(fileReader)
                    .withCSVParser(csvParser)
                    .build();

            // Настройки стратегии маппинга файла в Employee объект
            final var mappingStrategy = new ColumnPositionMappingStrategy<Employee>();
            mappingStrategy.setType(Employee.class);
            mappingStrategy.setColumnMapping(columnMapping);

            // Маппер
            final var beanToCsv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(mappingStrategy)
                    .build();

            // Парсинг файла и возвращение резульата
            return beanToCsv.parse();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
