package details.repositories;

import core.entities.User;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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

    public T unserialize(String line) {
        String[] values = StringUtils.split(line, ",");
        Constructor<T> constructor = null;
        try {
            constructor = objectClass.getConstructor();
            return constructor.newInstance(UUID.fromString(values[0]), values[1], Integer.parseInt(values[2]));
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public String serialize(T object) {

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

        return fields.stream().map(f -> {
            try {
                return f.get(object).toString();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.joining(","));
    }
}
