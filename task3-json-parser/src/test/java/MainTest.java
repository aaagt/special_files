import com.google.gson.JsonSyntaxException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import specialfiles.json.Employee;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Тесты для Main")
class MainTest {

    public static void assertEmployeeEquals(Employee expected, Employee actual) {
        assertAll("Проверка совпадения данных работника",
                () -> assertEquals(expected.id, actual.id),
                () -> assertEquals(expected.firstName, actual.firstName),
                () -> assertEquals(expected.lastName, actual.lastName),
                () -> assertEquals(expected.country, actual.country),
                () -> assertEquals(expected.age, actual.age)
        );
    }

    public static void assertEmployeeListsEquals(List<Employee> expected, List<Employee> actual) {
        assertAll("Проверка списков на совпадение",
                () -> assertEquals(expected.size(),
                        actual.size(),
                        "Размер списков не совпадает"
                ),
                () -> assertTrue(
                        () -> {
                            for (int i = 0; i < expected.size(); i++) {
                                final var expectedEmployee = expected.get(i);
                                final var actualEmployee = actual.get(i);
                                assertEmployeeEquals(expectedEmployee, actualEmployee);
                            }
                            return true;
                        },
                        "Содержимое списков не совпадает"
                )
        );
    }

    @Test
    @DisplayName("jsonToList проходит с валидными аргументами")
    void jsonToList_validArgument_success() {
        // given:
        final var json = """
                [
                  {
                    "id": 1,
                    "firstName": "John",
                    "lastName": "Smith",
                    "country": "USA",
                    "age": 25
                  },
                  {
                    "id": 2,
                    "firstName": "Inav",
                    "lastName": "Petrov",
                    "country": "RU",
                    "age": 23
                  }
                ]
                """;
        final var expected = List.of(
                new Employee(1, "John", "Smith", "USA", 25),
                new Employee(2, "Inav", "Petrov", "RU", 23)
        );

        // when:
        final var employees = Main.jsonToList(json);

        // then:
        assertEmployeeListsEquals(expected, employees);
    }

    @Test
    @DisplayName("jsonToList распознаёт пустой список json")
    void jsonToList_emptylist() {
        // given:
        final var json = "[]";
        final var expected = Collections.<Employee>emptyList();

        // when:
        final var employees = Main.jsonToList(json);

        // then:
        assertEmployeeListsEquals(expected, employees);
    }

    @Test
    @DisplayName("jsonToList распознаёт пустой json")
    void jsonToList_emptyjson() {
        // given:
        final var json = "";
        final var expected = Collections.<Employee>emptyList();

        // when:
        final var employees = Main.jsonToList(json);

        // then:
        assertEmployeeListsEquals(expected, employees);
    }

    @Test
    @DisplayName("jsonToList некорректный json вызывает JsonSyntaxException")
    void jsonToList_invalidJson_throws_JsonSyntaxException() {
        // given:
        final var json = "---";

        // when:
        Executable executable = () -> Main.jsonToList(json);

        // then:
        assertThrows(
                JsonSyntaxException.class,
                executable
        );
    }
}
