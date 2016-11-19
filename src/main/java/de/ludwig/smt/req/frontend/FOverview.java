package de.ludwig.smt.req.frontend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import de.ludwig.rdd.Requirement;
import de.ludwig.smt.req.frontend.tec.MenuEntry;
import de.ludwig.smt.req.frontend.tec.OverviewDataService;
import jodd.petite.meta.PetiteBean;
import jodd.petite.meta.PetiteInject;
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
public class FOverview
{
	@PetiteInject
	protected OverviewDataService ods;

	public BiFunction<Request, Response, ModelAndView> showWelcomePage()
	{
		return (req, res) -> {
			final Map<String, Object> model = new HashMap<>();
			createMenu(model);
			createFlowOverview();

			model.put("template", "index");
			return new ModelAndView(model, "main");
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
		model.put("test", "hello world");
		return model;
	}

	public Map<String, Object> createFlowOverview()
	{

		return null;
	}
}
