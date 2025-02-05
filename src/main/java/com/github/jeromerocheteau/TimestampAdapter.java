package com.github.jeromerocheteau;

import java.lang.reflect.Type;
import java.sql.Timestamp;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class TimestampAdapter implements JsonSerializer<Timestamp>, JsonDeserializer<Timestamp> {

	@Override
	public Timestamp deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
		try {
			long time = element.getAsLong();
			return new Timestamp(time);
		} catch (Exception e) {
			throw new JsonParseException(e);
		}
	}

	@Override
	public JsonElement serialize(Timestamp timestamp, Type type, JsonSerializationContext context) {
		long value = timestamp.getTime();
		return new JsonPrimitive(value);
	}
}