package de.ludwig.smt.req.frontend;

import java.util.Locale;
import java.util.function.BiFunction;

import de.ludwig.rdd.Requirement;
import de.ludwig.rdd.RequirementMapping;
import de.ludwig.smt.app.data.Note;
import de.ludwig.smt.req.backend.NoteService;
import de.ludwig.smt.req.backend.tec.ElasticSearchDocumentService;
import de.ludwig.smt.req.frontend.tec.EditCreateDocumentModelObject;
import de.ludwig.smt.req.frontend.tec.EditCreateDocumentService;
import de.ludwig.smt.tec.frontend.AjaxTriggeredResponse;
import de.ludwig.smt.tec.frontend.AjaxTriggeredResponse.Usage;
import de.ludwig.smt.tec.frontend.ModalModelAndView;
import de.ludwig.smt.tec.frontend.ModalModelObject;
import de.ludwig.smt.tec.frontend.ModalProvider;
import jodd.petite.meta.PetiteBean;
import jodd.petite.meta.PetiteInject;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

/**
 * Service that provides methods to interact with a note in a modal window.
 * 
 * @author Daniel
 *
 */
@PetiteBean
@Requirement(mappings = { @RequirementMapping(name = "showEditCreateNote", target = "showEditCreateDocument") })
public class EditCreateNoteViewService extends EditCreateDocumentService<Note> implements ModalProvider
{
	@PetiteInject
	protected NoteService noteService;

	@Override
	public BiFunction<Request, Response, ModelAndView> modal()
	{
		return showEditCreateDocument();
	}

	@Override
	public ElasticSearchDocumentService<Note> documentService()
	{
		return noteService;
	}

	@Override
	public ModelAndView mavForShowEditCreateDocument(EditCreateDocumentModelObject<Note> model)
	{
		final ModalModelObject mmo = new ModalModelObject();
		mmo.modalContentName("editCreateNote").formActionName("editCreateNote")
				.title(I18N.resolveMessage("title", Locale.GERMAN)).modelObject(model);
		return new ModalModelAndView(mmo);
	}

	@Override
	public ModelAndView mavForDisplayValidationMessage(final EditCreateDocumentModelObject<Note> validatedObject)
	{
		final ModalModelObject mmo = new ModalModelObject();
		mmo.modalContentName("editCreateNote").modelObject(validatedObject);
		return new ModalModelAndView(mmo);
	}

	@Override
	public AjaxTriggeredResponse respForSaveDocument()
	{
		AjaxTriggeredResponse resp = new AjaxTriggeredResponse();
		resp.usage(Usage.JS_ONLY).evaluatableJS("closeModal()");
		return resp;
	}

	@Override
	public Note initiateDocument()
	{
		return new Note();
	}

	@Override
	public void copyFormValues(Request req, Note document)
	{
		document.setContent(req.queryMap("content").value());
	}

}
