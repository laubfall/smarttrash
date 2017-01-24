package de.ludwig.smt.req.frontend.tec;

import de.ludwig.smt.app.data.Note;
import de.ludwig.smt.tec.frontend.ModalFormResult;

/**
 * 
 * @author Daniel
 *
 */
public class EditCreateNoteModel extends ModalFormResult
{
	/**
	 * The serial version uid.
	 */
	private static final long serialVersionUID = 1944606169245025267L;

	private Note note;
	
	private String esDocumentId;

	public Note getNote()
	{
		return note;
	}

	public void setNote(Note note)
	{
		this.note = note;
	}

	public String getEsDocumentId()
	{
		return esDocumentId;
	}

	public void setEsDocumentId(String esDocumentId)
	{
		this.esDocumentId = esDocumentId;
	}
}
