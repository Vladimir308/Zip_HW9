package tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;

import model.Person;

public class JsonTest {
    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void readerJsonAndParseToClassTest() throws Exception {
        ClassLoader cl = JsonTest.class.getClassLoader();
        try (Reader reader = new InputStreamReader(Objects.requireNonNull(cl.getResourceAsStream("jsonTest.json")))) {
            Person person = mapper.readValue(reader, Person.class);

            Assertions.assertEquals("1", person.getId());
            Assertions.assertEquals("German", person.getName());
            Assertions.assertEquals("89776546369", person.getNumber());
        }
    }
}