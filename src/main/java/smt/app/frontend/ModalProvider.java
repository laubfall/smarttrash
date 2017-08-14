package smt.app.frontend;

import java.util.function.BiFunction;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

/**
 * Indicates that a class provides the information that is required creating a modal.
 * 
 * @author Daniel
 *
 */
@FunctionalInterface
public interface ModalProvider
{
	/**
	 * Create the modal.
	 * @return {@link BiFunction} creating the modal.
	 */
	BiFunction<Request, Response, ModelAndView> modal();
}
