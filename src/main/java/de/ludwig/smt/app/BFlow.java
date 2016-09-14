package de.ludwig.smt.app;

import de.ludwig.rdd.Requirement;
import de.ludwig.smt.tec.ElasticSearch;
import jodd.petite.meta.PetiteBean;
import jodd.petite.meta.PetiteInject;

/**
 * 
 * @author daniel
 *
 */
@Requirement
@PetiteBean
public class BFlow {
	
	@PetiteInject
	protected ElasticSearch es;
	
	public void loadFlows(){
		
//		es.esClient()..
	}
	
	public void createFlow(){
		
	}
}
