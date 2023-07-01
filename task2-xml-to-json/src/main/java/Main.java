import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import specialfiles.xml.Employee;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        final var inputFileName = "data.xml";
        final var outputFileName = "data.json";
        final var list = parseXML(inputFileName);
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

    private static List<Employee> parseXML(String fileName) {

        try {

            final var result = new ArrayList<Employee>();

            final var documentFactory = DocumentBuilderFactory
                    .newInstance();

            final var documentBuilder = documentFactory
                    .newDocumentBuilder();

            final var document = documentBuilder
                    .parse(new File(fileName));

            final var staff = document
                    .getDocumentElement();

            final var staffNodeList = staff
                    .getChildNodes();

            for (int i = 0; i < staffNodeList.getLength(); i++) {

                final var node = staffNodeList.item(i);
                System.out.println("Teкyщий элeмeнт: " + node.getNodeName());

                if (node.getNodeType() == Node.ELEMENT_NODE &&
                        node.getNodeName() == "employee") {
                    final var element = (Element) node;
                    final var employee = parseEmployeeNode(element);
                    result.add(employee);
                }
            }

            return result;

        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    private static Employee parseEmployeeNode(Element EmployeeElement) {

        final var employee = new Employee();

        final var employeeFieldsNodeList = EmployeeElement
                .getChildNodes();

        for (int i = 0; i < employeeFieldsNodeList.getLength(); i++) {

            final var employeeFieldNode = employeeFieldsNodeList.item(i);

            if (employeeFieldNode.getNodeType() == Node.ELEMENT_NODE) {
                final var element = (Element) employeeFieldNode;
                switch (element.getNodeName()) {
                    case "id" ->
                            employee.id = Long.parseLong(element.getTextContent());
                    case "firstName" ->
                            employee.firstName = element.getTextContent();
                    case "lastName" ->
                            employee.lastName = element.getTextContent();
                    case "country" ->
                            employee.country = element.getTextContent();
                    case "age" ->
                            employee.age = Integer.parseInt(element.getTextContent());
                }
            }
        }

        return employee;
    }
}
