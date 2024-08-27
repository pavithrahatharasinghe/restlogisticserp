package org.example.restlogisticserp.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class TimestampSerializer extends JsonSerializer<Timestamp> {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @Override
    public void serialize(Timestamp timestamp, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String formattedDate = dateFormat.format(timestamp);
        gen.writeString(formattedDate);
    }
}
