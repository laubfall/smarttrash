package de.ludwig.smt.req.frontend.tec;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import de.ludwig.jodd.JoddPowered;
import de.ludwig.rdd.Requirement;
import de.ludwig.smt.req.frontend.EditCreateFlow;
import de.ludwig.smt.tec.frontend.ModalProvider;
import jodd.petite.meta.PetiteBean;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

/**
 * Service that is responsible for creating modals.
 * 
 * @author Daniel
 *
 */
@PetiteBean
@Requirement
public class ModalService
{
	private static Map<String, Class<? extends ModalProvider>> modalProviders = new HashMap<>();

	static {
		modalProviders.put("editCreateFlow", EditCreateFlow.class);
	}

	public BiFunction<Request, Response, ModelAndView> openModal()
	{
		return (req, res) -> {
			String params = req.params("name");
			Class<? extends ModalProvider> provider = modalProviders.get(params);
			ModalProvider modalProvider = JoddPowered.petite.getBean(provider);
			return modalProvider.modal().apply(req, res);
		};
	}
}
