package smt.domain.service;

import java.util.Date;
import java.util.Locale;
import java.util.function.BiFunction;

import jodd.petite.meta.PetiteBean;
import jodd.petite.meta.PetiteInject;
import smt.app.frontend.AjaxTriggeredResponse;
import smt.app.frontend.ModalModelAndView;
import smt.app.frontend.ModalModelObject;
import smt.app.frontend.ModalProvider;
import smt.app.frontend.StandaloneStandardMessageResolver;
import smt.app.frontend.AjaxTriggeredResponse.Usage;
import smt.app.rdd.Requirement;
import smt.app.rdd.RequirementMapping;
import smt.domain.model.Note;
import smt.persistence.es.ElasticSearchDocumentService;
import smt.persistence.es.NoteService;
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
	@Override
	public BiFunction<Request, Response, ModelAndView> showEditCreateDocument()
	{
		// TODO Auto-generated method stub
		return super.showEditCreateDocumentKO();
	}

	private static StandaloneStandardMessageResolver I18N = new StandaloneStandardMessageResolver("editCreateNote");
	
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
		resp.usage(Usage.JS_ONLY).evaluatableJS("editCreateNoteAndUpdateView()");
		return resp;
	}

	@Override
	public Note initiateDocument()
	{
		final Note note = new Note();
		note.setCreatedat(new Date());
		return note; 
	}

	@Override
	public void copyFormValues(Request req, Note document)
	{
		document.setContent(req.queryMap("content").value());
//		document.setFlow(flow);
	}

	@Override
	public StandaloneStandardMessageResolver messageResolver()
	{
		return I18N;
	}

	@Override
	protected EditCreateDocumentModelObject<Note> initiateModelObject()
	{
		return new EditCreateNoteModelObject();
	}

}
