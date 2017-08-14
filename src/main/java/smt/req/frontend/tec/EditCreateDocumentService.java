package smt.req.frontend.tec;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import jodd.petite.meta.PetiteBean;
import jodd.petite.meta.PetiteInject;
import jodd.util.StringUtil;
import jodd.vtor.Violation;
import smt.app.data.Hit;
import smt.req.backend.tec.ElasticSearchDocumentService;
import smt.tec.ElasticSearch;
import smt.tec.frontend.AjaxTriggeredResponse;
import smt.tec.frontend.FormMessage;
import smt.tec.frontend.FormSubmitModelObject;
import smt.tec.frontend.ModalModelAndView;
import smt.tec.frontend.ModalModelObject;
import smt.tec.frontend.StandaloneStandardMessageResolver;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

/**
 * Base functionality for classes that want to provide editing and creating function for Elasticsearch document.
 * 
 * @author Daniel
 *
 */
@PetiteBean
public abstract class EditCreateDocumentService<D>
{
	@PetiteInject
	protected ElasticSearch es;

	/**
	 * Factory-Method.
	 * 
	 * @return return the {@link ModelAndView} that shows the form for editing a document. To fill the form implement
	 *         {@link #loadDocumentFillForm(Request, EditCreateDocumentModelObject)}. The {@link ModelAndView} is
	 *         constructed by {@link #mavForShowEditCreateDocument()}.
	 */
	public BiFunction<Request, Response, ModelAndView> showEditCreateDocument()
	{
		return (req, res) -> {
			final EditCreateDocumentModelObject<D> modalFormResult = initiateModelObject();
			loadDocumentFillForm(req, modalFormResult);
			return mavForShowEditCreateDocument(modalFormResult);
		};
	}
	
	public BiFunction<Request, Response, ModelAndView> showEditCreateDocumentKO() {
		return (req, res) -> {
			final ModalModelObject mmo = new ModalModelObject();
			mmo.modalContentName("editCreateNoteKO").formActionName("editCreateNote").modelObject(new FormSubmitModelObject());
			return new ModalModelAndView(mmo);
		};
	}
	

	protected void loadDocumentFillForm(Request req, EditCreateDocumentModelObject<D> modalFormResult)
	{
		D modelObject;
		final String esDocumentId = esDocumentIdFromReq(req);
		// load the elasticsearch document.

		if (documentService().isNewDocument(esDocumentId)) {
			modelObject = initiateDocument();
		} else {
			Hit<D> hit = documentService().getDocument(esDocumentId);
			modelObject = hit.getDocument();
			modalFormResult.setEsDocumentId(hit.getDocumentId());
		}
		modalFormResult.setDocument(modelObject);
		
		modalFormResult.consumRequestParameters(req);
	}

	
	
	protected EditCreateDocumentModelObject<D> initiateModelObject()
	{
		return new EditCreateDocumentModelObject<D>();
	}

	public BiFunction<Request, Response, AjaxTriggeredResponse> saveDocument()
	{
		return (req, res) -> {
			final ElasticSearchDocumentService<D> documentService = documentService();

			D convertValue = initiateDocument();

			final String esDocumentId = esDocumentIdFromReq(req);
			if (StringUtil.isNotBlank(esDocumentId)) {
				Hit<D> hit = documentService.getDocument(esDocumentId);
				convertValue = hit.getDocument();
			}

			copyFormValues(req, convertValue);

			final List<Violation> validateFlow = new ArrayList<>();
			Consumer<List<Violation>> onValidationErrors = violations -> {
				validateFlow.addAll(violations);
			};

			documentService.saveDocument(convertValue, esDocumentId, onValidationErrors);

			if (validateFlow.isEmpty() == false) {
				ModelAndView displayValidationMessage = displayValidationMessage(validateFlow);
				AjaxTriggeredResponse atr = new AjaxTriggeredResponse();
				atr.setMav(displayValidationMessage);
				return atr;
			}

			return respForSaveDocument();
		};
	}

	public ModelAndView displayValidationMessage(List<Violation> violations)
	{
		final EditCreateDocumentModelObject<D> modalFormResult = initiateModelObject();
		modalFormResult.setDocument((D) violations.iterator().next().getValidatedObject());

		violations.stream().forEach(violation -> {
			final String msgResolved = messageResolver().resolveMessage(violation.getCheck().getMessage(),
					Locale.GERMAN); // TODO resolve the
			// chosen locale.
			modalFormResult.addMessage(new FormMessage(msgResolved, 1)); // TODO adjust message level.
		});
		return mavForDisplayValidationMessage(modalFormResult);
	}

	/**
	 * Required to retrieve specific es documents.
	 * 
	 * @return see description.
	 */
	public abstract ElasticSearchDocumentService<D> documentService();

	/**
	 * Let the concrete implementation decide what the ModelAndView look like.
	 * 
	 * @param model provided by the standard mechanism that create the model object for the view.
	 * @return ModalAndView as required.
	 */
	public abstract ModelAndView mavForShowEditCreateDocument(EditCreateDocumentModelObject<D> model);

	/**
	 * Provide a ModelAndView Object that meets the needs of showing validation messages.
	 * 
	 * @param validatedObject model object.
	 * @return see description.
	 */
	public abstract ModelAndView mavForDisplayValidationMessage(final EditCreateDocumentModelObject<D> validatedObject);

	/**
	 * Factory-Method.
	 * 
	 * @return return a response for the save document action.
	 */
	public abstract AjaxTriggeredResponse respForSaveDocument();

	/**
	 * Factory-Method.
	 * 
	 * @return return a newly initialized POJO of type D.
	 */
	public abstract D initiateDocument();

	/**
	 * Matching Message-Reslover for the view.
	 * 
	 * @return a message resolver.
	 */
	public abstract StandaloneStandardMessageResolver messageResolver();

	/**
	 * Converts form values and copy them into the pojo.
	 * 
	 * @param req request where we can read the form values from.
	 * @param document the pojo, the receiver of the form values.
	 */
	public abstract void copyFormValues(Request req, D document);

	private String esDocumentIdFromReq(Request req)
	{
		return req.queryMap("id").value();
	}
}
