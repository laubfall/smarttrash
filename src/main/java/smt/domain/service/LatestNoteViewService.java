package smt.domain.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import jodd.petite.meta.PetiteBean;
import jodd.petite.meta.PetiteInject;
import smt.app.frontend.AjaxTriggeredResponse;
import smt.domain.model.Note;
import smt.persistence.es.NoteService;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

/**
 * Functionality to display the latest notes made.
 * 
 * @author Daniel
 *
 */
@PetiteBean
public class LatestNoteViewService
{
	@PetiteInject
	protected NoteService noteService;

	public BiFunction<Request, Response, AjaxTriggeredResponse> createLatestNotesView()
	{
		return (req, res) -> {
			final AjaxTriggeredResponse atr = new AjaxTriggeredResponse();

			// load youngest Notes
			final Map<String, Object> modelData = new HashMap<>();
			final List<Note> loadYoungestNotes = noteService.loadYoungestNotes();
			modelData.put("youngestNotes", loadYoungestNotes);
			final ModelAndView modelAndView = new ModelAndView(modelData, "latestNotesView");
			atr.mav(modelAndView);

			return atr;
		};
	}
}
