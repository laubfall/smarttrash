package smt.persistence.es;

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

import jodd.petite.meta.PetiteBean;
import jodd.petite.meta.PetiteInject;
import smt.app.rdd.Requirement;
import smt.app.rdd.RequirementMapping;
import smt.domain.model.Hit;
import smt.domain.model.Note;
import smt.domain.service.LatestNotesService;

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
