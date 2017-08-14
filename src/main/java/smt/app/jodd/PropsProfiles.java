package smt.app.jodd;

/**
 * Known Props-Profiles.
 * @author daniel
 *
 */
public enum PropsProfiles {
	APPLICATION("application"),
	JUNIT("junit"),
	;
	
	private String profileName;
	
	private PropsProfiles(String profileName){
		this.profileName = profileName;
	}

	public String getProfileName() {
		return profileName;
	}
}
