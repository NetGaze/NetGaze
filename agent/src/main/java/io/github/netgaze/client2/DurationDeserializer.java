package io.github.netgaze.client2;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Duration;

public class DurationDeserializer extends JsonDeserializer<Duration> {

    @Override
    public Duration deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String durationStr = p.getValueAsString();
        if (durationStr.endsWith("s")) {
            return Duration.ofSeconds(Long.parseLong(durationStr.replace("s", "")));
        } else if (durationStr.endsWith("m")) {
            return Duration.ofMinutes(Long.parseLong(durationStr.replace("m", "")));
        } else if (durationStr.endsWith("h")) {
            return Duration.ofHours(Long.parseLong(durationStr.replace("h", "")));
        } else if (durationStr.endsWith("d")) {
            return Duration.ofDays(Long.parseLong(durationStr.replace("d", "")));
        }
        throw new IOException("Unknown duration format: " + durationStr);
    }
}
