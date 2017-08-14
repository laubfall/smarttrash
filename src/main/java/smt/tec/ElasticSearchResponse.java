package smt.tec;

import java.util.function.Consumer;

import org.elasticsearch.action.ActionResponse;

/**
 * 
 * @author Daniel
 *
 * @param <R>
 */
public class ElasticSearchResponse<R extends ActionResponse> implements Consumer<R>
{
	public R response;

	@Override
	public void accept(R t)
	{
		response = t;
	}

}
