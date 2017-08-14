package smt.req.frontend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import jodd.petite.meta.PetiteBean;
import jodd.petite.meta.PetiteInject;
import smt.app.data.Flow;
import smt.app.data.Hit;
import smt.rdd.Requirement;
import smt.req.backend.FlowService;
import smt.req.frontend.tec.MenuEntry;
import smt.req.frontend.tec.OverviewDataService;
import smt.tec.frontend.AjaxTriggeredResponse;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

/**
 * Frontend Overview Functionality.
 * 
 * @author Daniel
 *
 */
@Requirement
@PetiteBean
public class OverviewService
{
	@PetiteInject
	protected OverviewDataService ods;

	@PetiteInject
	protected FlowService flowService;

	// TODO only return the main page.
	public BiFunction<Request, Response, ModelAndView> showWelcomePage()
	{
		return (req, res) -> {
			final Map<String, Object> model = new HashMap<>();
			createMenu(model);
			model.putAll(createFlowOverviewModelObject());

			model.put("template", "flowOverview");
			return new ModelAndView(model, "main");
		};
	}

	public BiFunction<Request, Response, AjaxTriggeredResponse> createFlowOverview()
	{
		return (req, res) -> {
			AjaxTriggeredResponse atr = new AjaxTriggeredResponse();
			ModelAndView mav = new ModelAndView(createFlowOverviewModelObject(), "flowOverview");
			atr.setMav(mav);
			return atr;
		};
	}
	
	/**
	 * Create the menu.
	 * 
	 * @param model model object that contains the menu object.
	 * @return
	 */
	public Map<String, Object> createMenu(Map<String, Object> model)
	{
		final List<MenuEntry> createFlowOverviewMenu = ods.createFlowOverviewMenu();
		model.put("menu", createFlowOverviewMenu);
		return model;
	}

	/**
	 * Prepares flow data for the overview.
	 * 
	 * @return Map with one entry "flows";
	 */
	public Map<String, Object> createFlowOverviewModelObject()
	{
		final List<Hit<Flow>> loadFlows = flowService.loadFlows();
		final Map<String, Object> fmo = new HashMap<>();
		fmo.put("flows", loadFlows);
		
		// TODO as specified create information if there are no flows
		
		return fmo;
	}
}
