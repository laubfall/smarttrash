package smt.model;

import java.util.Date;

import jodd.json.JsonParser;
import jodd.json.JsonSerializer;
import jodd.vtor.constraint.NotNull;
import smt.app.config.FlowId;

/**
 * A Note.
 * 
 * @author daniel
 *
 */
public class Note
{
	/**
	 * Content of this note.
	 */
	private String content;
	
	/**
	 * The flow this note belongs to.
	 */
	@NotNull(message="note.flow.notnull")
	private FlowId flow;

	@NotNull(message="note.created.notnull")
	private Date createdat;
	
	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public FlowId getFlow()
	{
		return flow;
	}

	public void setFlow(FlowId flow)
	{
		this.flow = flow;
	}
	
	public Date getCreatedat()
	{
		return createdat;
	}

	public void setCreatedat(Date createdat)
	{
		this.createdat = createdat;
	}

	public static String toJson(final Note note)
	{
		JsonSerializer js = new JsonSerializer();
		return js.serialize(note);
	}

	public static Note fromJson(String ref)
	{
		JsonParser jsonParser = JsonParser.create();
		return jsonParser.parse(ref, Note.class);
	}
}
