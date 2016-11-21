package de.ludwig.smt.tec.frontend;

import jodd.json.JsonSerializer;
import spark.ResponseTransformer;

/**
 * Json Response Transformer for Spark.
 * @author Daniel
 *
 */
public class JsonResponseTransformer implements ResponseTransformer
{
	private JsonSerializer ser = new JsonSerializer();
	
	@Override
	public String render(Object model) throws Exception
	{
		return ser.serialize(model);
	}

}
