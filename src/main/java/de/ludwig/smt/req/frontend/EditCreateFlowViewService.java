package de.ludwig.smt.req.frontend;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import de.ludwig.rdd.Requirement;
import de.ludwig.smt.app.config.FlowId;
import de.ludwig.smt.app.data.Flow;
import de.ludwig.smt.req.backend.FlowService;
import de.ludwig.smt.req.frontend.tec.AjaxTriggeredResponse;
import de.ludwig.smt.req.frontend.tec.AjaxTriggeredResponse.Usage;
import de.ludwig.smt.req.frontend.tec.ModalService;
import de.ludwig.smt.tec.frontend.EditCreateFlowModel;
import de.ludwig.smt.tec.frontend.FormMessage;
import de.ludwig.smt.tec.frontend.ModalFormResult;
import de.ludwig.smt.tec.frontend.ModalProvider;
import de.ludwig.smt.tec.validation.ValidationContext;
import jodd.petite.meta.PetiteBean;
import jodd.petite.meta.PetiteInject;
import spark.ModelAndView;
import spark.Redirect.Status;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

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

	public BiFunction<Request, Response, ModelAndView> showEditCreateFlow()
	{
		return (req, res) -> {
			final Map<String, Object> model = new HashMap<>();
			model.put("modalContent", "editCreateFlow");
			model.put(ModalService.REQ_PARAM_ACTION_NAME, "editCreateFlow");
			final EditCreateFlowModel modalFormResult = new EditCreateFlowModel();
			// TODO this actually only works for creating a new flow
			modalFormResult.setFlow(new Flow());
			model.put("model", modalFormResult);
			return new ModelAndView(model, "modal");
		};
	}

	public BiFunction<Request, Response, AjaxTriggeredResponse> saveFlow()
	{
		return (req, res) -> {
			// TODO convert req
			final Flow convertValue = new Flow();
			convertValue.setDescription(req.queryMap("description").value());
			convertValue.setName(req.queryMap("name").value());
			// TODO map the id. How without breaking the flowid mechanism? Maybe send the FlowId of the orginal object as json.
//			convertValue.setId(FlowId.);
			convertValue.setId(new FlowId());
			ValidationContext<Flow> validateFlow = flowService.validateFlow(convertValue);
			if (validateFlow.isValid() == false) {
				ModelAndView displayValidationMessage = displayValidationMessage(validateFlow);
				AjaxTriggeredResponse atr = new AjaxTriggeredResponse();
				atr.setMav(displayValidationMessage);
				return atr; 
			}

			flowService.saveFlow(convertValue, null);

			final AjaxTriggeredResponse atr = new AjaxTriggeredResponse();
			atr.setUsage(Usage.JS_ONLY);
			atr.setEvaluatableJS(closeEditCreateFlowDialog());
			return atr;
		};
	}

	public ModelAndView displayValidationMessage(final ValidationContext<Flow> ctx)
	{
		final Map<String, Object> model = new HashMap<>();
		ModelAndView mav = new ModelAndView(model, "modal");
		model.put("modalContent", "editCreateFlow");
		EditCreateFlowModel modalFormResult = new EditCreateFlowModel();
		modalFormResult.setFlow(ctx.getValidatedObject());
		model.put("model", modalFormResult);
		
		ctx.messages().forEach(entry -> entry.getValue().forEach(msg -> modalFormResult.addMessage(new FormMessage(msg.getI18nKey(), 1))));
		
		return mav;
	}

	public String closeEditCreateFlowDialog()
	{
		return "closeModal()";
	}

	@Override
	public BiFunction<Request, Response, ModelAndView> modal()
	{
		return showEditCreateFlow();
	}
}
