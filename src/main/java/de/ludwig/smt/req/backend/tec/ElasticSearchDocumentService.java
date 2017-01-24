package de.ludwig.smt.req.backend.tec;

import java.util.function.Function;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;

import de.ludwig.jodd.JoddPowered;
import de.ludwig.jodd.PropsElasticsearchProps;
import de.ludwig.rdd.NoRequirement;
import de.ludwig.smt.app.data.Hit;
import de.ludwig.smt.tec.ElasticSearch;
import de.ludwig.smt.tec.validation.ValidationContext;
import jodd.petite.meta.PetiteBean;
import jodd.petite.meta.PetiteInject;
import spark.utils.StringUtils;

@PetiteBean
public abstract class ElasticSearchDocumentService<D>
{
	@PetiteInject
	protected ElasticSearch es;

	private String indexName = JoddPowered.settings.getValue(PropsElasticsearchProps.INDEX.getPropertyName());

	public String saveDocument(D document, String esDocumentId)
	{
		validateDocument(document); // TODO provide a callback or exception to signal that there are validation errors.

		if (isNewDocument(esDocumentId)) {
			return indexDocument(document);
		} else {
			return updateDocument(document, esDocumentId);
		}
	}

	public boolean isNewDocument(String esDocumentId)
	{
		if (StringUtils.isBlank(esDocumentId)) {
			return true;
		}

		return getDocument(esDocumentId) == null;
	}

	public String indexDocument(D document)
	{
		IndexResponse indexResponse = es.esClient().prepareIndex(indexName, documentType())
				.setSource(jsonifier().apply(document)).setRefresh(true).get();

		return indexResponse.getId();
	}

	private String updateDocument(D document, String esDocumentId)
	{
		UpdateResponse updateResponse = es.esClient().prepareUpdate(indexName, documentType(), esDocumentId)
				.setDoc(jsonifier().apply(document)).get();

		return updateResponse.getId();
	}

	@NoRequirement
	public abstract ValidationContext<D> validateDocument(D document);

	@NoRequirement
	public abstract Hit<D> getDocument(final String esDocumentId);

	@NoRequirement
	public abstract String documentType();

	@NoRequirement
	public abstract Function<D, String> jsonifier();
}
