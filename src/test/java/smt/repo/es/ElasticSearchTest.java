package smt.repo.es;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import smt.app.AbstractElasticSearchTest;
import smt.app.jodd.JoddPowered;
import smt.app.jodd.PropsElasticsearchProps;
import smt.domain.model.Flow;
import smt.domain.model.Note;
import smt.persistence.es.ElasticSearch;
import smt.persistence.es.ElasticSearchResponse;

/**
 * Testing basic elasticsearch functionality.
 * 
 * @author Daniel
 *
 */
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
		es.indexDocument(Flow.toJson(f), JoddPowered.settings.getValue(PropsElasticsearchProps.INDEX.getPropertyName()),
				"flow", success, exception);

		Mockito.verify(exception, Mockito.never()).accept(Mockito.any());

		Assert.assertNotNull(success.response);
		Assert.assertNotNull(success.response.getId());
	}

	@Test
	public void searchDocument()
	{
		ElasticSearch es = JoddPowered.petite.getBean(ElasticSearch.class);
		String indexName = JoddPowered.settings.getValue(PropsElasticsearchProps.INDEX.getPropertyName());
		Flow f = new Flow("find me");
		f.setDescription("searchable text");
		es.indexDocument(Flow.toJson(f), indexName, "flow", new ElasticSearchResponse<>(), ex -> {
		});

		QueryBuilder queryBuilder = new TermQueryBuilder("description", "text");
		Collection<SearchHit> searchDocuments = es.searchDocuments("flow", queryBuilder);
		Assert.assertNotNull(searchDocuments);
		Assert.assertFalse(searchDocuments.isEmpty());

		f = new Flow("a flow with a createdAt Date");
		f.setDescription(" text");
		es.indexDocument(Flow.toJson(f), indexName, "flow", new ElasticSearchResponse<>(), ex -> {
		});
	}

	@Test
	public void multiGetDocument() throws InterruptedException
	{
		final ElasticSearch es = JoddPowered.petite.getBean(ElasticSearch.class);
		String indexName = JoddPowered.settings.getValue(PropsElasticsearchProps.INDEX.getPropertyName());
		final List<String> createdDocumentIds = new ArrayList<>();
		for(int i = 0; i < 10; ++i) {
			Note n = new Note();
			n.setContent("content " + i);
			es.indexDocument(Note.toJson(n), indexName, "note", res -> createdDocumentIds.add(res.getId()), res->Assert.fail());
		}
		
		Assert.assertEquals(10, createdDocumentIds.size());
		Collection<Note> multiGet = es.multiGet("note", Note::fromJson, createdDocumentIds.toArray(new String[10]));
		Assert.assertNotNull(multiGet);
		Assert.assertEquals(10, multiGet.size());
	}
}
