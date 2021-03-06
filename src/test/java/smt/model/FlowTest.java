package smt.model;

import org.junit.Assert;
import org.junit.Test;

import smt.domain.model.Flow;

/**
 * 
 * @author Daniel
 *
 */
public class FlowTest
{
	@Test
	public void toElasticSearchAndBack()
	{
		Flow f = new Flow();
		f.setName("hello");
		f.setDescription("jfldsj fdjlsjf jfldsj");
		
		String elasticSearch = Flow.toJson(f);
		Assert.assertNotNull(elasticSearch);
		Assert.assertTrue(elasticSearch.contains("hello"));
		Assert.assertTrue(elasticSearch.contains("jfldsj fdjlsjf jfldsj"));
		System.out.println(elasticSearch);
		
		Flow fromElasticSearch = Flow.fromJson(elasticSearch);
		Assert.assertNotNull(fromElasticSearch);
		Assert.assertEquals(fromElasticSearch, f);
	}
}
