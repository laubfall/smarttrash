package de.ludwig.smt.tec;

import java.util.function.Consumer;

import org.elasticsearch.action.index.IndexResponse;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import de.ludwig.jodd.JoddPowered;
import de.ludwig.jodd.PropsElasticsearchProps;
import de.ludwig.smt.app.AbstractElasticSearchTest;
import de.ludwig.smt.app.data.Flow;

public class ElasticSearchTest extends AbstractElasticSearchTest
{
	@Test
	public void indexDocumentException()
	{
		ElasticSearch es = JoddPowered.petite.getBean(ElasticSearch.class);

		Consumer<Throwable> mock = Mockito.mock(Consumer.class);

		// no document given so expect an Exception
		es.indexDocument("", "smarttrash", "test", null, mock);

		Mockito.verify(mock, Mockito.times(1)).accept(Mockito.any());
	}

	@Test
	public void indexDocument()
	{
		ElasticSearch es = JoddPowered.petite.getBean(ElasticSearch.class);
		Flow f = new Flow("testFlow");
		f.setDescription("test flow");

		Consumer<Throwable> exception = Mockito.mock(Consumer.class);
		ElasticSearchResponse<IndexResponse> success = new ElasticSearchResponse<>();
		es.indexDocument(Flow.toJson(f), JoddPowered.settings.getValue(PropsElasticsearchProps.INDEX.getPropertyName()), "flow", success, exception);

		Mockito.verify(exception, Mockito.never()).accept(Mockito.any());
		
		Assert.assertNotNull(success.response);
		Assert.assertNotNull(success.response.getId());
	}
}
