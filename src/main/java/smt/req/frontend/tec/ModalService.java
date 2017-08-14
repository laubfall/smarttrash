package smt.req.frontend.tec;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import jodd.petite.meta.PetiteBean;
import smt.jodd.JoddPowered;
import smt.rdd.Requirement;
import smt.req.frontend.EditCreateFlowViewService;
import smt.req.frontend.EditCreateNoteViewService;
import smt.tec.frontend.ModalProvider;
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
	/**
	 * Provide a request parameter with that name that holds the values of the form action attribute. This is typically
	 * used by {@link ModalProvider}s.
	 */
	public static final String REQ_PARAM_ACTION_NAME = "action";

	/**
	 * Provide a request parameter with that name that holds the name of a modal provider.
	 */
	private static final String REQ_PARAM_PROVIDER_NAME = "name";

	/**
	 * Holds the modal providers. Key is the name of the provider. You choose the provider by providing a request
	 * parameter called {@value #REQ_PARAM_PROVIDER_NAME}.
	 */
	private static Map<String, Class<? extends ModalProvider>> modalProviders = new HashMap<>();

	static {
		modalProviders.put("editCreateFlowKO", EditCreateFlowViewService.class);
		modalProviders.put("editCreateNoteKO", EditCreateNoteViewService.class);
	}

	public BiFunction<Request, Response, ModelAndView> openModal()
	{
		return (req, res) -> {
			final String modalProviderName = req.params(REQ_PARAM_PROVIDER_NAME);
			Class<? extends ModalProvider> provider = modalProviders.get(modalProviderName);
			ModalProvider modalProvider = JoddPowered.petite.getBean(provider);
			return modalProvider.modal().apply(req, res);
		};
	}
}
