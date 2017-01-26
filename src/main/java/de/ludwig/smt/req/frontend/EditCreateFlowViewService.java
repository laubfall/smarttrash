package de.ludwig.smt.req.frontend;

import java.util.List;
import java.util.Locale;
import java.util.function.BiFunction;

import de.ludwig.jodd.vtor.VtorProfile;
import de.ludwig.rdd.Requirement;
import de.ludwig.smt.app.data.Flow;
import de.ludwig.smt.app.data.Hit;
import de.ludwig.smt.req.backend.FlowService;
import de.ludwig.smt.req.frontend.tec.EditCreateFlowModel;
import de.ludwig.smt.tec.frontend.AjaxTriggeredResponse;
import de.ludwig.smt.tec.frontend.AjaxTriggeredResponse.Usage;
import de.ludwig.smt.tec.frontend.FormMessage;
import de.ludwig.smt.tec.frontend.ModalModelAndView;
import de.ludwig.smt.tec.frontend.ModalModelObject;
import de.ludwig.smt.tec.frontend.ModalProvider;
import de.ludwig.smt.tec.frontend.StandaloneStandardMessageResolver;
import jodd.petite.meta.PetiteBean;
import jodd.petite.meta.PetiteInject;
import jodd.util.StringUtil;
import jodd.vtor.Violation;
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
public class EditCreateFlowViewService implements ModalProvider
{
	@PetiteInject
	protected FlowService flowService;

	@PetiteInject
	protected OverviewService overviewService;

	private static StandaloneStandardMessageResolver I18N = new StandaloneStandardMessageResolver("editCreateFlow");

	public BiFunction<Request, Response, ModelAndView> showEditCreateFlow()
	{
		return (req, res) -> {
			final EditCreateFlowModel modalFormResult = new EditCreateFlowModel();

			Flow modelObject = null;
			if (isNewFlow(req)) {
				modelObject = new Flow();
			} else {
				modelObject = loadFlowFillForm(req, modalFormResult);
			}
			modalFormResult.setFlow(modelObject);

			final ModalModelObject mmo = new ModalModelObject();
			mmo.modalContentName("editCreateFlow").formActionName("editCreateFlow").modelObject(modalFormResult)
					.title(I18N.resolveMessage("title", Locale.GERMAN)); // TODO resolve chosen language.

			return new ModalModelAndView(mmo);
		};
	}

	private Flow loadFlowFillForm(Request req, final EditCreateFlowModel modalFormResult)
	{
		Flow modelObject;
		final String esDocId = req.queryMap("flowId").value();
		// load the elasticsearch document.
		Hit<Flow> hit = flowService.getFlow(esDocId);
		modelObject = hit.getDocument();
		modalFormResult.setEsDocumentId(hit.getDocumentId());
		return modelObject;
	}

	public BiFunction<Request, Response, AjaxTriggeredResponse> saveFlow()
	{
		return (req, res) -> {
			Flow convertValue = new Flow();

			final String esDocumentId = req.queryMap("id").value();
			if (StringUtil.isNotBlank(esDocumentId)) {
				Hit<Flow> hit = flowService.getFlow(esDocumentId);
				convertValue = hit.getDocument();
			}

			convertValue.setDescription(req.queryMap("description").value());
			convertValue.setName(req.queryMap("name").value());
			List<Violation> validateFlow = flowService.validateDocument(convertValue, VtorProfile.GUI);

			if (validateFlow.isEmpty() == false) {
				ModelAndView displayValidationMessage = displayValidationMessage(validateFlow);
				AjaxTriggeredResponse atr = new AjaxTriggeredResponse();
				atr.setMav(displayValidationMessage);
				return atr;
			}

			flowService.saveFlow(convertValue, null, esDocumentId);

			final AjaxTriggeredResponse atr = new AjaxTriggeredResponse();
			atr.setUsage(Usage.JS_ONLY);
			atr.setEvaluatableJS(closeEditCreateFlowDialog());
			return atr;
		};
	}

	public ModelAndView displayValidationMessage(final List<Violation> ctx)
	{
		final EditCreateFlowModel modalFormResult = new EditCreateFlowModel();
		modalFormResult.setFlow((Flow) ctx.iterator().next().getValidatedObject());

		ctx.stream().forEach(violation -> {
			final String msgResolved = I18N.resolveMessage(violation.getName(), Locale.GERMAN); // TODO resolve the
																								// chosen locale.
			modalFormResult.addMessage(new FormMessage(msgResolved, 1)); // TODO adjust message level.
		});

		final ModalModelObject mmo = new ModalModelObject();
		mmo.modalContentName("editCreateFlow").modelObject(modalFormResult);
		return new ModalModelAndView(mmo);
	}

	public String closeEditCreateFlowDialog()
	{
		return "closeModal()";
	}

	public boolean isNewFlow(Request req)
	{
		// TODO we do not check if a flow with the given id really exists.
		return req.queryMap("flowId").hasValue() == false;
	}

	@Override
	public BiFunction<Request, Response, ModelAndView> modal()
	{
		return showEditCreateFlow();
	}
}
