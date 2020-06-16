package com.github.jeromerocheteau;

import java.lang.reflect.Type;
import java.sql.Date;
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

public class DateAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {

	private DateFormat formatter;
	
	private DateFormat getFormatter() {
		if (formatter == null) {
			formatter = new SimpleDateFormat("yyyy-MM-dd");
		}
		return formatter;
	}
	
	@Override
	public Date deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
		try {
			DateFormat formatter =  this.getFormatter();
			String value = element.getAsString();
			long time = formatter.parse(value).getTime();
			return new Date(time);
		} catch (ParseException e) {
			throw new JsonParseException(e);
		}
	}

	@Override
	public JsonElement serialize(Date date, Type type, JsonSerializationContext context) {
		String value = this.getFormatter().format(date);
		return new JsonPrimitive(value);
	}
}