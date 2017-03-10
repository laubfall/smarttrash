package de.ludwig.smt.req.frontend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import de.ludwig.smt.app.data.Note;
import de.ludwig.smt.req.backend.NoteService;
import de.ludwig.smt.tec.frontend.AjaxTriggeredResponse;
import jodd.petite.meta.PetiteBean;
import jodd.petite.meta.PetiteInject;
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