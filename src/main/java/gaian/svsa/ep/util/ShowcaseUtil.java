package gaian.svsa.ep.util;

import java.lang.reflect.Field;

public class ShowcaseUtil {
    public static Object getPropertyValueViaReflection(Object obj, String fieldName) throws ReflectiveOperationException {
        try {
            // Usando reflexão para acessar o campo de forma dinâmica
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);  // Torna o campo acessível
            return field.get(obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new ReflectiveOperationException("Erro ao acessar o campo via reflexão", e);
        }
    }
}
