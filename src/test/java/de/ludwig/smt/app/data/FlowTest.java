package de.ludwig.smt.app.data;

import org.junit.Assert;
import org.junit.Test;

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
		
		Flow fromElasticSearch = Flow.fromElasticSearch(elasticSearch);
		Assert.assertNotNull(fromElasticSearch);
		Assert.assertEquals(fromElasticSearch, f);
	}
}
