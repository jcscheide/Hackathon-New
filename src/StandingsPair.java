
public class StandingsPair implements Comparable<StandingsPair> {

	private double winPercent;
	private Team team;
	private int teamID;
	private String teamName;
	
	public StandingsPair(double winPercent, int teamID, String teamName){
		this.winPercent = winPercent;
		this.teamID = teamID;
		this.teamName = teamName;
		
	}
	
	public StandingsPair(double d, Team team) {
		this.winPercent = d;
		this.team = team;
		this.teamID = team.getTeamID();
		this.teamName = team.getTeamName();
	}

	public double getWinPercent(){
		return this.winPercent;
	}
	public void setWinPercent(double newWinPercent){
		this.winPercent = newWinPercent;
	}
	
	public int getTeamID(){
		return this.teamID;
	}
	public void setTeamID(int newTeamID){
		this.teamID = newTeamID;
	}
	public String getTeamName(){
		return this.teamName;
	}
	public void setTeamName(String newTeamName){
		this.teamName = newTeamName;
	}
	public Team getTeam(){
		return this.team;
	}
	public void setTeam(Team t){
		team = t;
	}
	
	@Override
	public int compareTo(StandingsPair other) {
	
		return -1 * Double.valueOf(this.winPercent).compareTo(other.getWinPercent());
	}
	

}
