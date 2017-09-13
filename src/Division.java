
public class Division {

	//Instance variables
	private Team[] teams; //Array of teams in the division. The first team in the array corresponds to the first team in the standings array.
	private int divID; //Identifier
	private Game[] [][] standings; // A 2 dimensional array which will hold an array of games that two teams play against one another.
	/* example
	 *  	1	 	2	 	3	 	4	 	5
	 * 	1	/  	  [0,1]   [0,2]	  [0,3]   [0,4]
	 * 	2	[1,0]	/  	  [1,2]   [1,3]   [1,4]
	 */	
	
	//Constructor
	
	public Division(int divID){
		teams = new Team[5];
		this.divID = divID;
		standings = new Game[5][5][4]; //Five team grid, max of 4 games vs. division opponents.
		
	}
	
	
	//Getters and Setters
	public Team[] getTeams() {
		return teams;
	}
	public void setTeams(Team[] teams) {
		this.teams = teams;
	}
	public int getDivID() {
		return divID;
	}
	public void setDivID(int divID) {
		this.divID = divID;
	}
	public Game[] [][] getStandings() {
		return standings;
	}
	public void setStandings(Game[] [][] standings) {
		this.standings = standings;
	}
	
	//Methods
	
	//Add a team to the division
	public void addTeam(Team team){
		
		for(int i = 0; i < teams.length; i++){
			if(teams[i] == null){ 
				teams[i] = team;
				return;
			}
		}
	}
	
	//Add an array of teams to the division
	public void addTeams(Team[] teams){
		for(Team t: teams){
			if(t == null){
				break;
			}
			addTeam(t);
		}
		return;
	}
	
	
	//Return an array of all the games a specific team have played against division opponents.
	public Game[] getDivisionGamesRow(int teamID){
		Game[] gamesAgainstDivision = new Game[16];
		for(int i = 0; i < 5; i++){ //For every array of games vs. Division opponent
			for(Game g: standings[teamID][i]){ //Loop through each individual game
				if (g.getAwayScore() == 0){ //If the game hasn't been played yet, skip (or the cell is x vs. x)
					break;
				}
				for(Game g2: gamesAgainstDivision){ //Loop through the result array
					if(g2.getAwayScore() == 0){ //If the game hasn't been filled yet
						g2 = g; //Fill it with the game
						break; //And break.
					}
				}
				
			}
		}
		return gamesAgainstDivision; //This array will have empty games on the end.
		
	}
	
	public void addGameToStandings(Game newGame, int team1ID, int team2ID){
		int team1StandingLocation = -1;
		int team2StandingLocation = -1;
		//Find the location of the teams in the standings grid
		for(int i = 0; i < teams.length; i++){
			
			if(teams[i].getTeamID() == team1ID){
				team1StandingLocation = i;
			}
			if(teams[i].getTeamID() == team2ID){
				team2StandingLocation = i;
			}
		}
		//Use the standing locations to find the correct game array
		Game [] placeToPut1 = standings[team1StandingLocation][team2StandingLocation];
		Game [] placeToPut2 = standings[team2StandingLocation][team1StandingLocation];
		
		//Find the first empty game to fill in, and add.
		for(int j = 0; j < placeToPut1.length; j++){
			if(placeToPut1[j] == null){
				standings[team1StandingLocation][team2StandingLocation][j] = newGame;
				break;
			}
		}
		//Keep the 2D array consistent by doing the same thing for the second location.
		for(int k = 0; k < placeToPut2.length; k++){
			if(placeToPut2[k] == null){
				standings[team2StandingLocation][team1StandingLocation][k] = newGame;
				break;
			}
		}
		
		
	}

	
	
	
}
