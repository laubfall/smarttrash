package smt.repo.es;

import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import org.joda.time.DateTimeUtils;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import jodd.datetime.JDateTime;
import jodd.vtor.Violation;
import smt.app.AbstractElasticSearchTest;
import smt.app.config.FlowId;
import smt.app.jodd.JoddPowered;
import smt.model.Hit;
import smt.model.Note;
import smt.repo.es.NoteService;

/**
 * Tests the {@link NoteService}.
 * 
 * @author Daniel
 *
 */
public class NoteServiceTest extends AbstractElasticSearchTest
{
	private NoteService noteService;

	@Test
	public void saveNote()
	{
		final VTorConsumer vtorConsumer = Mockito.spy(new VTorConsumer());

		Note document = new Note();
		document.setFlow(new FlowId());
		document.setContent("hurz");
		document.setCreatedat(new Date());
		String esDocId = noteService.saveDocument(document, null, vtorConsumer);

		Mockito.verify(vtorConsumer, Mockito.never()).accept(Mockito.anyList());

		Assert.assertNotNull(esDocId);

		Hit<Note> esDoc = noteService.getDocument(esDocId);
		Assert.assertNotNull(esDoc);
		Assert.assertNotNull(esDoc.getDocument());
		Assert.assertEquals("hurz", esDoc.getDocument().getContent());

		document = esDoc.getDocument();
		document.setContent("neu");

		noteService.saveDocument(document, esDocId, null);
		esDoc = noteService.getDocument(esDocId);
		Assert.assertEquals("neu", esDoc.getDocument().getContent());
	}

	@Test
	public void saveWithValidationErrors()
	{
		final VTorConsumer vtorConsumer = Mockito.spy(new VTorConsumer());

		Note document = new Note();
		document.setContent("hurz");
		document.setCreatedat(new Date());
		String esDocId = noteService.saveDocument(document, null, vtorConsumer);

		// expect some validation errors
		Mockito.verify(vtorConsumer, Mockito.times(1)).accept(Mockito.anyList());
		Assert.assertNull(esDocId);
	}

	@Test
	public void searchLatestNotes()
	{
		Note n = new Note();
		n.setFlow(new FlowId());
		n.setContent("1");
		n.setCreatedat(new Date());
		
		noteService.saveDocument(n, null, validation -> {});
		
		
		n = new Note();
		n.setFlow(new FlowId());
		n.setContent("2");
		n.setCreatedat(new Date());
		
		noteService.saveDocument(n, null, validation -> {});
		
		List<Note> loadYoungestNotes = noteService.loadYoungestNotes();
		Assert.assertNotNull(loadYoungestNotes);
		Assert.assertFalse(loadYoungestNotes.isEmpty());
		
		Assert.assertEquals("2", loadYoungestNotes.get(0).getContent());
		Assert.assertEquals("1", loadYoungestNotes.get(1).getContent());
	}

	@Override
	public void setup() throws Exception
	{
		noteService = JoddPowered.petite.getBean(NoteService.class);
	}

	class VTorConsumer implements Consumer<List<Violation>>
	{
		@Override
		public void accept(List<Violation> t)
		{
			t.stream().forEach(v -> System.out.println(v.getName()));
		}
	}
}
