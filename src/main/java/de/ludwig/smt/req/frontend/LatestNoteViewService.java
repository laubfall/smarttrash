package de.ludwig.smt.req.frontend;

import java.util.function.BiFunction;

import de.ludwig.smt.req.backend.NoteService;
import de.ludwig.smt.tec.frontend.AjaxTriggeredResponse;
import jodd.petite.meta.PetiteBean;
import jodd.petite.meta.PetiteInject;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

@PetiteBean
public class LatestNoteViewService
{
	@PetiteInject
	protected NoteService noteService;

	public BiFunction<Request, Response, AjaxTriggeredResponse> createLatestNotesView()
	{
		return (req, res) -> {			
			final AjaxTriggeredResponse atr = new AjaxTriggeredResponse();
			
			// TODO provide the model object
			final ModelAndView modelAndView = new ModelAndView(null, "latestNotesView");
			atr.mav(modelAndView);
			
			// TODO load youngest Notes
			noteService.loadYoungestNotes();
			
			return atr;
		};
	}
}
