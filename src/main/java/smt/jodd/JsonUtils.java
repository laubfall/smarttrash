package smt.jodd;

import java.io.Serializable;

import jodd.json.JsonParser;
import jodd.json.JsonSerializer;
import jodd.json.ValueConverter;

public class JsonUtils
{
	public static String toJson(Serializable ser)
	{
		JsonSerializer js = new JsonSerializer();
		return js.serialize(ser);
	}

	public static <O> O fromJson(String json, Class<O> type)
	{
		JsonParser jsonParser = JsonParser.create();
		return jsonParser.parse(json, type);
	}
}
