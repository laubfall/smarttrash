package de.ludwig.smt.app;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import de.ludwig.rdd.Requirement;
import jodd.petite.meta.PetiteBean;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

@Requirement
@PetiteBean
public class FOverview {
	public BiFunction<Request, Response, ModelAndView> showWelcomePage(){		
		return (req, res) -> {
			createFlowOverviewMenu();
			createFlowOverview();
			
			Map<String, Object> model = new HashMap<>();
			return new ModelAndView(model, "index");
		};
	}
	
	public void createFlowOverviewMenu(){
		
	}
	
	public Map<String, Object> createFlowOverview(){
		
		return null;
	}
}
