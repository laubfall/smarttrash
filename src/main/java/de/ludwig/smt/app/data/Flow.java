package de.ludwig.smt.app.data;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;

import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.xcontent.XContentBuilder;

import de.ludwig.smt.SmartTrashException;

/**
 * A Flow.
 * 
 * @author daniel
 *
 */
public class Flow {

	private String name;
	
	public Flow(){
		
	}
	
	public Flow(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public BytesReference toElasticSearch(){

		try {
			XContentBuilder builder = jsonBuilder()
			    .startObject()
			        .field("name", name)
			    .endObject();
			return builder.bytes();
		} catch (IOException e) {
			throw new SmartTrashException(e);
		}
	}
}
