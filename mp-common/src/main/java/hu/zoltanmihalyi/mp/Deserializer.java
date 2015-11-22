package hu.zoltanmihalyi.mp;

import lombok.AllArgsConstructor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

@AllArgsConstructor
public class Deserializer<T> extends SynchronousConverter<byte[], T> {
    private Class<T> targetClass;

    @Override
    public T convert(byte[] bytes) throws ConversionFailureException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return targetClass.cast(ois.readObject());
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            throw new ConversionFailureException(e);
        }
    }
}
