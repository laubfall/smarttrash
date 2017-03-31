package de.ludwig.smt.req.frontend.tec;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import spark.Request;
import spark.Response;

public class JsonDataService
{
	private static Map<String, Function<String, Serializable>> dataProviders = new HashMap<>();

	public static Serializable provideData(Request req, Response res)
	{
		String value = req.params("viewModel");
		Function<String, Serializable> function = dataProviders.get(value);
		return function.apply(req.params("param"));
	}

	public static void registerProvider(String name, Function<String, Serializable> provider)
	{
		dataProviders.put(name, provider);
	}
}
