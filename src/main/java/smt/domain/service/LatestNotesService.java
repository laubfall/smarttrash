package smt.domain.service;

import java.util.function.Function;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.TypeQueryBuilder;
import org.elasticsearch.search.SearchHit;

import jodd.petite.meta.PetiteBean;
import smt.domain.model.Hit;
import smt.domain.model.LatestNotes;
import smt.domain.model.Note;
import smt.persistence.es.ElasticSearchDocumentService;

@PetiteBean
public class LatestNotesService extends ElasticSearchDocumentService<LatestNotes>
{
	public static final String DOC_TYPE = "latestNotes";

	/**
	 * Adds a note as the latest note.
	 * 
	 * TODO actually we do not have any user authentication or a something like a user scope. With Users we have to
	 * change this method in that way that we load the latest notes document of the current user.
	 * 
	 * @param latest the note to add as the latest note.
	 */
	public void addNoteAsLatest(Note latest)
	{
		// First try to load the latest notes document. If there is no such document, create one
		LatestNotes latestNotesDocument = loadCurrentLatestNotesDocument();

		if (latestNotesDocument == null) { // create a latestNotes document
			LatestNotes ln = new LatestNotes();
			saveDocument(ln, null, null);
			addNoteAsLatest(latest);
			return;
		}


		// ln.addLatestNote(esDocumentId);
	}

	/**
	 * TODO actually we do not have any user authentication or a something like a user scope. With Users we have to
	 * change this method in that way that we load the latest notes document of the current user.
	 * 
	 * @return
	 */
	public LatestNotes loadCurrentLatestNotesDocument()
	{
		// First try to load the latest notes document. If there is no such document, create one
		final SearchResponse search = es.esClient().prepareSearch(indexName).setQuery(new TypeQueryBuilder(DOC_TYPE))
				.get();
		SearchHit[] hits = search.getHits().getHits();
		if(hits.length == 0) {
			return null;
		}
		
		if (hits.length != 1) {
			// do some error logging
			return null;
		}
		
		return LatestNotes.fromJson(hits[0].getSourceAsString());
	}

	@Override
	public Hit<LatestNotes> getDocument(String esDocumentId)
	{
		return es.documentById(esDocumentId, LatestNotes::fromJson);
	}

	@Override
	public String documentType()
	{
		return DOC_TYPE;
	}

	@Override
	public Function<LatestNotes, String> jsonifier()
	{
		return LatestNotes::toJson;
	}

}
