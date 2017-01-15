package de.ludwig.smt.tec.frontend;

import spark.ModelAndView;

public class ModalModelAndView extends ModelAndView
{

	public ModalModelAndView(ModalModelObject model) {
		super(model, "modal");
	}

}
