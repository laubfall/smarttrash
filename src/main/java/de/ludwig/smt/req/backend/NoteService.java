package de.ludwig.smt.req.backend;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import de.ludwig.rdd.Requirement;
import de.ludwig.rdd.RequirementMapping;
import de.ludwig.smt.app.data.Hit;
import de.ludwig.smt.app.data.Note;
import de.ludwig.smt.req.backend.tec.ElasticSearchDocumentService;
import de.ludwig.smt.req.backend.tec.LatestNotesService;
import jodd.petite.meta.PetiteBean;
import jodd.petite.meta.PetiteInject;

/**
 * Methods to interact with notes inside the ES.
 * 
 * @author Daniel
 *
 */
@Requirement(mappings = { //
		@RequirementMapping(name = "saveNote", target = "saveDocument"),
		@RequirementMapping(name = "isNewNote", target = "isNewDocument"),
		@RequirementMapping(name = "createNote", target = "indexDocument"),
		@RequirementMapping(name = "updateNote", target = "updateDocument"),
		@RequirementMapping(name = "validateNote", target = "validateDocument") })
@PetiteBean
public class NoteService extends ElasticSearchDocumentService<Note>
{

	/**
	 * ES Type description for the Flow.
	 */
	private static String esNoteType = "note";

	@PetiteInject
	protected LatestNotesService latestNotes;
	
	public List<Note> loadYoungestNotes()
	{
		final MatchAllQueryBuilder queryBuilder = new MatchAllQueryBuilder();
		final FieldSortBuilder order = SortBuilders.fieldSort("createdat").order(SortOrder.DESC);
		Collection<SearchHit> searchDocuments = es.searchDocuments(esNoteType, queryBuilder,order);
		List<Note> collect = searchDocuments.stream().map(sh -> Note.fromJson(sh.getSourceAsString())).collect(Collectors.toList());
		return collect;
	}

	@Override
	public Hit<Note> getDocument(String esDocumentId)
	{
		return es.documentById(esDocumentId, Note::fromJson);
	}

	@Override
	public String documentType()
	{
		return esNoteType;
	}

	@Override
	public Function<Note, String> jsonifier()
	{
		return Note::toJson;
	}
}
