package com.github.jeromerocheteau;

import java.lang.reflect.Type;
import java.sql.Time;
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

public class TimeAdapter implements JsonSerializer<Time>, JsonDeserializer<Time> {

	private DateFormat formatter;
	
	private DateFormat getFormatter() {
		if (formatter == null) {
			formatter = new SimpleDateFormat("HH:mm:ss");
		}
		return formatter;
	}
	
	@Override
	public Time deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
		try {
			DateFormat formatter =  this.getFormatter();
			String value = element.getAsString();
			long time = formatter.parse(value).getTime();
			return new Time(time);
		} catch (ParseException e) {
			throw new JsonParseException(e);
		}
	}

	@Override
	public JsonElement serialize(Time time, Type type, JsonSerializationContext context) {
		String value = this.getFormatter().format(time);
		return new JsonPrimitive(value);
	}
}