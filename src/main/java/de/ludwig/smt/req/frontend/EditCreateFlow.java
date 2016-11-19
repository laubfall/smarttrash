package de.ludwig.smt.req.frontend;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import de.ludwig.rdd.Requirement;
import de.ludwig.smt.tec.frontend.ModalProvider;
import jodd.petite.meta.PetiteBean;
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
	public BiFunction<Request, Response, ModelAndView> showEditCreateFlow()
	{
		return (req, res) -> {
			final Map<String, Object> model = new HashMap<>();
			model.put("modal-content", "editCreateFlow");
			return new ModelAndView(model , "editCreateFlow");
		};
	}

	@Override
	public BiFunction<Request, Response, ModelAndView> modal()
	{
		return showEditCreateFlow();
	}
}
