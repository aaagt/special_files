import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import specialfiles.json.Employee;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        final var inputFileName = "data.json";
        final var json = readString(inputFileName);
        final var employeeList = jsonToList(json);

        employeeList.forEach(System.out::println);
    }

    public static List<Employee> jsonToList(String json) {

        if (json.isEmpty()) {
            return Collections.emptyList();
        }

        // Сериализатор JSON
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

        // Преобразование списка из JSON
        return gson.fromJson(json, listType);
    }

    private static String readString(String fileName) {
        try {
            final var bytes = Files.readAllBytes(Path.of(fileName));
            return new String(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
