package com.github.jeromerocheteau;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DateTimeAdapter implements JsonSerializer<Timestamp>, JsonDeserializer<Timestamp> {

	private DateFormat formatter;
	
	private DateFormat getFormatter() {
		if (formatter == null) {
			formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		}
		return formatter;
	}
	
	@Override
	public Timestamp deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
		try {
			DateFormat formatter =  this.getFormatter();
			String value = element.getAsString();
			long time = formatter.parse(value).getTime();
			return new Timestamp(time);
		} catch (ParseException e) {
			throw new JsonParseException(e);
		}
	}

	@Override
	public JsonElement serialize(Timestamp timestamp, Type type, JsonSerializationContext context) {
		String value = this.getFormatter().format(timestamp);
		return new JsonPrimitive(value);
	}
}