package de.ludwig.smt.req.frontend;

import java.util.function.BiFunction;

import de.ludwig.rdd.Requirement;
import de.ludwig.smt.tec.frontend.ModalProvider;
import jodd.petite.meta.PetiteBean;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

/**
 * Service that provides methods to interact with a note in a modal window.
 * @author Daniel
 *
 */
@PetiteBean
@Requirement
public class EditCreateNoteViewService implements ModalProvider
{

	@Override
	public BiFunction<Request, Response, ModelAndView> modal()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
