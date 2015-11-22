package hu.zoltanmihalyi.mp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class Serializer extends SynchronousConverter<Object, byte[]> {
    @Override
    public byte[] convert(Object o) throws ConversionFailureException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(o);
            return bos.toByteArray();
        } catch (IOException e) {
            throw new ConversionFailureException(e);
        }
    }
}
