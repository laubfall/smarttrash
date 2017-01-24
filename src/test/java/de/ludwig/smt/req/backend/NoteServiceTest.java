package de.ludwig.smt.req.backend;

import org.junit.Assert;
import org.junit.Test;

import de.ludwig.jodd.JoddPowered;
import de.ludwig.smt.app.AbstractElasticSearchTest;
import de.ludwig.smt.app.data.Hit;
import de.ludwig.smt.app.data.Note;

public class NoteServiceTest extends AbstractElasticSearchTest
{
	private NoteService noteService;

	@Test
	public void saveNote() {
		Note document = new Note();
		document.setContent("hurz");
		String esDocId = noteService.saveDocument(document, null);
		Assert.assertNotNull(esDocId);
		
		Hit<Note> esDoc = noteService.getDocument(esDocId);
		Assert.assertNotNull(esDoc);
		Assert.assertNotNull(esDoc.getDocument());
		Assert.assertEquals("hurz", esDoc.getDocument().getContent());
		
		document = esDoc.getDocument();
		document.setContent("neu");
		
		noteService.saveDocument(document, esDocId);
		esDoc = noteService.getDocument(esDocId);
		Assert.assertEquals("neu", esDoc.getDocument().getContent());
	}
	
	@Override
	public void setup() throws Exception
	{
		noteService = JoddPowered.petite.getBean(NoteService.class);
	}

}
