package de.ludwig.smt.tec.frontend;

import java.util.function.BiFunction;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

/**
 * 
 * @author Daniel
 *
 */
@FunctionalInterface
public interface ModalProvider
{
	BiFunction<Request, Response, ModelAndView> modal();
}
