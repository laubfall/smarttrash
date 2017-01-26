package de.ludwig.smt.req.frontend;

import java.util.function.BiFunction;

import de.ludwig.rdd.Requirement;
import de.ludwig.smt.app.data.Note;
import de.ludwig.smt.req.frontend.tec.EditCreateNoteModel;
import de.ludwig.smt.tec.frontend.ModalModelAndView;
import de.ludwig.smt.tec.frontend.ModalModelObject;
import de.ludwig.smt.tec.frontend.ModalProvider;
import jodd.petite.meta.PetiteBean;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

/**
 * Service that provides methods to interact with a note in a modal window.
 * 
 * @author Daniel
 *
 */
@PetiteBean
@Requirement
public class EditCreateNoteViewService implements ModalProvider
{

	public BiFunction<Request, Response, ModelAndView> showEditCreateNote()
	{
		return (req, res) -> {
			final EditCreateNoteModel editCreateNoteModel = new EditCreateNoteModel();
			Note note = new Note();
			if (isNewNote(req) == false) {
				// TODO load the note
			}
			
			editCreateNoteModel.setNote(note);

			final ModalModelObject mmo = new ModalModelObject();
			mmo.modalContentName("editCreateNote").formActionName("editCreateNote").modelObject(editCreateNoteModel);
			return new ModalModelAndView(mmo);
		};
	}

	public Note loadNoteFillForm(Request req, EditCreateNoteModel modelObject)
	{
		return null;
	}

	public boolean isNewNote(Request req)
	{
		return req.queryMap("noteId").hasValue();
	}

	@Override
	public BiFunction<Request, Response, ModelAndView> modal()
	{
		return showEditCreateNote();
	}

}
