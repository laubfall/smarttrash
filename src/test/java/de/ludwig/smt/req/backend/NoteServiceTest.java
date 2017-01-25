package de.ludwig.smt.req.backend;

import java.util.List;
import java.util.function.Consumer;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import de.ludwig.jodd.JoddPowered;
import de.ludwig.smt.app.AbstractElasticSearchTest;
import de.ludwig.smt.app.config.FlowId;
import de.ludwig.smt.app.data.Hit;
import de.ludwig.smt.app.data.Note;
import jodd.vtor.Violation;

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
		String esDocId = noteService.saveDocument(document, null, vtorConsumer);

		Mockito.verify(vtorConsumer, Mockito.times(1)).accept(Mockito.anyList());
		Assert.assertNull(esDocId);
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
