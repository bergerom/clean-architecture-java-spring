package details.repositories;

import core.entities.User;
import details.repositories.builder.GameScoreBuilder;
import details.repositories.builder.UserBuilder;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class EntitySerializer<T> {

    private final Class<T> objectClass;

    public EntitySerializer(Class<T> objectClass) {
        this.objectClass = objectClass;
    }

    // TODO : avoid cast warnings here
    public T parseLine(String line) {
        String[] values = StringUtils.split(line, ",");
        if (objectClass.getName().endsWith("User")) {
            return (T) new UserBuilder().fromArray(values).build();
        } else if (objectClass.getName().endsWith("GameScore")) {
            return (T) new GameScoreBuilder().fromArray(values).build();
        }
        return null;
    }

    public String toCsvLine(T object) {

        List<Field> fields = getFieldsFromRecord();

        return fields.stream().map(f -> {
            try {
                return f.get(object).toString();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.joining(","));
    }

    private List<Field> getFieldsFromRecord() {
        List<Field> fields = new ArrayList<>();

        RecordComponent[] components = objectClass.getRecordComponents();
        for (var comp : components) {
            try {
                Field field = objectClass
                        .getDeclaredField(comp.getName());
                field.setAccessible(true);
                fields.add(field);
            } catch (NoSuchFieldException e) {
                // for simplicity, error handling is skipped
            }
        }

        return fields;
    }
}
