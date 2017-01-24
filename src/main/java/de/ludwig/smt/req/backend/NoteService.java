package de.ludwig.smt.req.backend;

import java.util.function.Function;

import de.ludwig.rdd.Requirement;
import de.ludwig.rdd.RequirementMapping;
import de.ludwig.smt.app.config.FlowId;
import de.ludwig.smt.app.data.Hit;
import de.ludwig.smt.app.data.Note;
import de.ludwig.smt.req.backend.tec.ElasticSearchDocumentService;
import de.ludwig.smt.tec.validation.ValidationContext;
import de.ludwig.smt.tec.validation.ValidationMessage;
import de.ludwig.smt.tec.validation.ValidationState;
import jodd.petite.meta.PetiteBean;

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
		@RequirementMapping(name = "validateNote", target = "validateDocument")
})
@PetiteBean
public class NoteService extends ElasticSearchDocumentService<Note>
{

	/**
	 * ES Type description for the Flow.
	 */
	private static String esNoteType = "note";

	@Override
	public ValidationContext<Note> validateDocument(Note note)
	{
		FlowId flow = note.getFlow();
		ValidationContext<Note> vctx = new ValidationContext<Note>(note);
		if (flow == null) {
			vctx.addValidationMessage("flow", new ValidationMessage("flow.empty", ValidationState.ERROR));
		}
		return vctx;
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
