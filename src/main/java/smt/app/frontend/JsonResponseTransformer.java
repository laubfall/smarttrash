package smt.app.frontend;

import jodd.json.JsonSerializer;
import spark.ResponseTransformer;

/**
 * Json Response Transformer for Spark.
 * @author Daniel
 *
 */
public class JsonResponseTransformer implements ResponseTransformer
{
	private JsonSerializer ser = new JsonSerializer().deep(true);
	
	@Override
	public String render(Object model) throws Exception
	{
		ser.deep(true);
		return ser.serialize(model);
	}

}
