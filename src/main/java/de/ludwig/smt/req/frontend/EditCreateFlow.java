package de.ludwig.smt.req.frontend;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import de.ludwig.rdd.Requirement;
import de.ludwig.smt.app.data.Flow;
import de.ludwig.smt.req.backend.FlowService;
import de.ludwig.smt.tec.frontend.EditCreateFlowModel;
import de.ludwig.smt.tec.frontend.FormMessage;
import de.ludwig.smt.tec.frontend.ModalFormResult;
import de.ludwig.smt.tec.frontend.ModalProvider;
import de.ludwig.smt.tec.validation.ValidationContext;
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
public class EditCreateFlow implements ModalProvider
{
	@PetiteInject
	protected FlowService flowService;

	@PetiteInject
	protected FOverview overviewService;

	public BiFunction<Request, Response, ModelAndView> showEditCreateFlow()
	{
		return (req, res) -> {
			final Map<String, Object> model = new HashMap<>();
			model.put("modalContent", "editCreateFlow");
			ModalFormResult modalFormResult = new EditCreateFlowModel();
			model.put("model", modalFormResult);
			return new ModelAndView(model, "modal");
		};
	}

	public BiFunction<Request, Response, ModelAndView> saveFlow()
	{
		return (req, res) -> {
			// TODO convert req
			Flow convertValue = new Flow();

			ValidationContext<Flow> validateFlow = flowService.validateFlow(convertValue);
			if (validateFlow.isValid() == false) {
				return displayValidationMessage(validateFlow);
			}

			flowService.saveFlow(convertValue, null);

			final Map<String, Object> model = new HashMap<>();
			return closeEditCreateFlowDialog(model, req, res);
		};
	}

	public ModelAndView displayValidationMessage(final ValidationContext<Flow> ctx)
	{
		final Map<String, Object> model = new HashMap<>();
		ModelAndView mav = new ModelAndView(model, "modal");
		model.put("modalContent", "editCreateFlow");
		ModalFormResult modalFormResult = new EditCreateFlowModel();
		model.put("model", modalFormResult);
		
		ctx.messages().forEach(entry -> entry.getValue().forEach(msg -> modalFormResult.addMessage(new FormMessage(msg.getI18nKey(), 1))));
		
		return mav;
	}

	public ModelAndView closeEditCreateFlowDialog(Map<String, Object> model, Request req, Response res)
	{
		return overviewService.showWelcomePage().apply(req, res);
	}

	@Override
	public BiFunction<Request, Response, ModelAndView> modal()
	{
		return showEditCreateFlow();
	}
}
