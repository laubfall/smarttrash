package smt.tec.frontend;

import spark.ModelAndView;

/**
 * ModelAndView for Modal Windows.
 * @author Daniel
 *
 */
public class ModalModelAndView extends ModelAndView
{

	public ModalModelAndView(ModalModelObject model) {
		super(model, "modal");
	}

}
