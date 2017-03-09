package de.ludwig.smt.req.frontend;

import java.util.Locale;
import java.util.function.BiFunction;

import de.ludwig.rdd.Requirement;
import de.ludwig.smt.app.data.Flow;
import de.ludwig.smt.req.backend.FlowService;
import de.ludwig.smt.req.backend.tec.ElasticSearchDocumentService;
import de.ludwig.smt.req.frontend.tec.EditCreateDocumentModelObject;
import de.ludwig.smt.req.frontend.tec.EditCreateDocumentService;
import de.ludwig.smt.tec.frontend.AjaxTriggeredResponse;
import de.ludwig.smt.tec.frontend.AjaxTriggeredResponse.Usage;
import de.ludwig.smt.tec.frontend.ModalModelAndView;
import de.ludwig.smt.tec.frontend.ModalModelObject;
import de.ludwig.smt.tec.frontend.ModalProvider;
import de.ludwig.smt.tec.frontend.StandaloneStandardMessageResolver;
import jodd.petite.meta.PetiteBean;
import jodd.petite.meta.PetiteInject;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

/**
 * Business methods for creating or editing a flow.
 * 
 * @author Daniel
 *
 */
@PetiteBean
@Requirement
public class EditCreateFlowViewService extends EditCreateDocumentService<Flow> implements ModalProvider
{
	@PetiteInject
	protected FlowService flowService;

	@PetiteInject
	protected OverviewService overviewService;

	private static StandaloneStandardMessageResolver I18N = new StandaloneStandardMessageResolver("editCreateFlow");

	@Override
	public BiFunction<Request, Response, ModelAndView> modal()
	{
		return showEditCreateDocument();
	}

	@Override
	public ElasticSearchDocumentService<Flow> documentService()
	{
		return flowService;
	}

	@Override
	public ModelAndView mavForShowEditCreateDocument(EditCreateDocumentModelObject<Flow> model)
	{
		final ModalModelObject mmo = new ModalModelObject();
		mmo.modalContentName("editCreateFlow").formActionName("editCreateFlow").modelObject(model)
				.title(I18N.resolveMessage("title", Locale.GERMAN)); // TODO resolve chosen language.

		return new ModalModelAndView(mmo);
	}

	@Override
	public ModelAndView mavForDisplayValidationMessage(EditCreateDocumentModelObject<Flow> validatedObject)
	{
		final ModalModelObject mmo = new ModalModelObject();
		mmo.modalContentName("editCreateFlow").modelObject(validatedObject);
		return new ModalModelAndView(mmo);
	}

	@Override
	public AjaxTriggeredResponse respForSaveDocument()
	{
		AjaxTriggeredResponse resp = new AjaxTriggeredResponse();
		resp.usage(Usage.JS_ONLY).evaluatableJS("editCreateFlowAndUpdateView()");
		return resp;
	}

	@Override
	public Flow initiateDocument()
	{
		return new Flow();
	}

	@Override
	public StandaloneStandardMessageResolver messageResolver()
	{
		return I18N;
	}

	@Override
	public void copyFormValues(Request req, Flow document)
	{
		document.setDescription(req.queryMap("description").value());
		document.setName(req.queryMap("name").value());
	}
}
