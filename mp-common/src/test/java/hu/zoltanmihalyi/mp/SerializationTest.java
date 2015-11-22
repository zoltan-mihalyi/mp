package hu.zoltanmihalyi.mp;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class SerializationTest {
    @Test
    @SuppressWarnings("unchecked")
    public void testSerializationAndDeserializationReturnsEqualObject() throws ConversionFailureException {
        Serializer serializer = new Serializer();
        Deserializer<String> deserializer = new Deserializer<>(String.class);
        String test = "test";

        byte[] serialized = serializer.convert(test);
        String result = deserializer.convert(serialized);
        assertEquals(test, result);
        assertNotSame(test, result);
    }

    @Test(expected = ConversionFailureException.class)
    @SuppressWarnings("unchecked")
    public void testDeserializeTransformerErrors() throws ConversionFailureException {
        Deserializer<String> deserializer = new Deserializer<>(String.class);
        deserializer.convert(new byte[0]);
    }

    @Test(expected = ConversionFailureException.class)
    @SuppressWarnings("unchecked")
    public void testSerializeTransformerErrors() throws ConversionFailureException {
        Serializer serializer = new Serializer();
        serializer.convert(new NotSerializable());
    }

    private static class NotSerializable {
    }
}
