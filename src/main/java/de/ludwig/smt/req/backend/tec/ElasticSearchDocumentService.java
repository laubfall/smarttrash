package de.ludwig.smt.req.backend.tec;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy;
import org.elasticsearch.action.update.UpdateResponse;

import de.ludwig.jodd.JoddPowered;
import de.ludwig.jodd.PropsElasticsearchProps;
import de.ludwig.jodd.vtor.SmtVtor;
import de.ludwig.jodd.vtor.VtorProfile;
import de.ludwig.smt.app.data.Hit;
import de.ludwig.smt.tec.ElasticSearch;
import jodd.petite.meta.PetiteBean;
import jodd.petite.meta.PetiteInject;
import jodd.vtor.Violation;
import spark.utils.StringUtils;

/**
 * Base class that provides functionality to index ES documents.
 * 
 * @author Daniel
 *
 * @param <D> ES document type. Typically a POJO.
 */
@PetiteBean
public abstract class ElasticSearchDocumentService<D>
{
	@PetiteInject
	protected ElasticSearch es;

	protected String indexName = JoddPowered.settings.getValue(PropsElasticsearchProps.INDEX.getPropertyName());

	/**
	 * Index or update a document. Before it does it does validation of the document.
	 * 
	 * @param document document to index or update.
	 * @param esDocumentId ES Document ID. Required if you want to update a document.
	 * @param onValidationErrors callback in case of validation violations.
	 * @return esDocumentId. either the newly created one, or the id of an existing document. Null if validation of
	 *         document failed.
	 */
	public String saveDocument(D document, String esDocumentId, Consumer<List<Violation>> onValidationErrors)
	{
		if (validateDocument(document, onValidationErrors) == false) {
			return null;
		}

		if (isNewDocument(esDocumentId)) {
			return indexDocument(document);
		} else {
			return updateDocument(document, esDocumentId);
		}
	}

	/**
	 * Checks if there already exists an es document for the given document id.
	 * 
	 * @param esDocumentId id to check.
	 * @return see description.
	 */
	public boolean isNewDocument(String esDocumentId)
	{
		if (StringUtils.isBlank(esDocumentId)) {
			return true;
		}

		return getDocument(esDocumentId) == null;
	}

	/**
	 * Index a document.
	 * 
	 * @param document document to index.
	 * @return es id of the indexed document.
	 */
	public String indexDocument(D document)
	{
		IndexResponse indexResponse = es.esClient().prepareIndex(indexName, documentType())
				.setSource(jsonifier().apply(document)).setRefreshPolicy(RefreshPolicy.WAIT_UNTIL).get();

		return indexResponse.getId();
	}

	/**
	 * Update a document.
	 * 
	 * @param document document to update.
	 * @param esDocumentId Mandatory. ID of the document to update.
	 * @return esDocumentId.
	 */
	public String updateDocument(D document, String esDocumentId)
	{
		UpdateResponse updateResponse = es.esClient().prepareUpdate(indexName, documentType(), esDocumentId)
				.setDoc(jsonifier().apply(document)).get();

		return updateResponse.getId();
	}

	/**
	 * Does the validation of a document before it gets indexed or updated.
	 * 
	 * @param document
	 * @return
	 */
	public List<Violation> validateDocument(D document, VtorProfile... profile)
	{
		final SmtVtor vtor = new SmtVtor();
		vtor.useProfiles(profile);
		final List<Violation> validate = vtor.validate(document);
		if (validate == null) {
			return new ArrayList<>(0);
		}
		return validate;
	}

	/**
	 * Loads a document for a given ID.
	 * 
	 * @param esDocumentId id of the document to load.
	 * @return loaded document. Null if there is no document for the given id.
	 */
	public abstract Hit<D> getDocument(final String esDocumentId);

	/**
	 * Type of the elasticsearch documen type.
	 * 
	 * @return s. description.
	 */
	public abstract String documentType();

	/**
	 * Factory-method.
	 * 
	 * @return a Function that provides the functionality to convert a pojo to a json.
	 */
	public abstract Function<D, String> jsonifier();

	private boolean validateDocument(D document, Consumer<List<Violation>> onValidationErrors)
	{
		List<Violation> validateDocument = validateDocument(document, VtorProfile.PERSISTENCE);
		if (validateDocument != null && validateDocument.isEmpty() == false && onValidationErrors != null) {
			onValidationErrors.accept(validateDocument);
			return false;
		}
		return true;
	}
}
