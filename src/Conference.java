import java.util.Arrays;

public class Conference {

	// Instance variables
	private Game[][][] standings;
	private Division[] divisions;
	private Team[] teams;
	private int confID; // Must be 1 or 2;

	// Constructor
	public Conference(int confID) {
		standings = new Game[15][15][4]; // 15 team grid with a max of 4 games
											// vs. each other.
		divisions = new Division[3];
		this.confID = confID;
		this.teams = new Team[15];
	}

	// Getters and Setters
	public Game[][][] getStandings() {
		return standings;
	}

	public void setStandings(Game[][][] standings) {
		this.standings = standings;
	}

	public Division[] getDivisions() {
		return divisions;
	}

	public void setDivisions(Division[] divisions) {
		this.divisions = divisions;
	}

	public Team[] getTeams() {
		return teams;
	}

	public void setTeams(Team[] teams) {
		this.teams = teams;
	}

	public int getConfID() {
		return confID;
	}

	public void setConfID(int confID) {
		this.confID = confID;
	}

	// Methods

	// Return an array of all the games a specific team have played against
	// Conference opponents.
	public Game[] getConferenceGamesRow(int teamID) {
		Game[] gamesAgainstConference = new Game[52]; // Teams play 52 games
														// against their
														// conference opponents.
		for (int i = 0; i < 15; i++) { // For every array of games vs.
										// Conference opponent
			for (Game g : standings[teamID][i]) { // Loop through each
													// individual game
				if (g.getAwayScore() == 0) { // If the game hasn't been played
												// yet, skip (or the cell is x
												// vs. x)
					break;
				}
				for (Game g2 : gamesAgainstConference) { // Loop through the
															// result array
					if (g2.getAwayScore() == 0) { // If the game hasn't been
													// filled yet
						g2 = g; // Fill it with the game
						break; // And break.
					}
				}

			}
		}
		return gamesAgainstConference; // This array will have empty games on
										// the end.

	}

	// Add division to divisions array
	public void addDivision(Division div) {
		for (Division d : divisions) {
			if (d == null) {
				d = div;
				return;
			}
		}
	}

	// Add game to division,team and conference. If this is called, they are in
	// the same conference as the check has been made in the league add.

	public void addGameToDivTeamConf(Game game) {
		Team team1 = null;
		Team team2 = null;
		for (Team t : teams) {
			if (t.getTeamID() == game.getAwayTeamID()) {
				team1 = t;
			}
			if (t.getTeamID() == game.getHomeTeamID()) {
				team2 = t;
			}
		}

		// First, add the game to the team's season.
		team1.addGame(game);
		team2.addGame(game);

		// Then, add the game to the division's standings, if they're in the
		// same division.
		Division team1Division = null;
		Division team2Division = null;

		for (Division d : divisions) {
			if (team1.getDivID() == d.getDivID()) {
				team1Division = d;
			}
			if (team2.getDivID() == d.getDivID()) {
				team2Division = d;
			}
		}
		if (team1Division == team2Division) {
			team1Division.addGameToStandings(game, team1.getTeamID(), team2.getTeamID());
		}
		// Lastly, add the game to the conference's standings.
		addGameToStandings(game);

	}

	// Adds a game to only the conference standings.
	private void addGameToStandings(Game game) {

		int team1ID = game.getAwayTeamID();
		int team2ID = game.getHomeTeamID();
		int team1StandingLocation = -1;
		int team2StandingLocation = -1;
		Team team1 = null;
		Team team2 = null;

		for (Team t : teams) {
			if (t.getTeamID() == game.getAwayTeamID()) {
				team1 = t;
			}
			if (t.getTeamID() == game.getHomeTeamID()) {
				team2 = t;
			}
		}

		// Find the location of the teams in the standings grid
		for (int i = 0; i < teams.length; i++) {
			if (teams[i].getTeamID() == team1ID) {
				team1StandingLocation = i;
			}
			if (teams[i].getTeamID() == team2ID) {
				team2StandingLocation = i;
			}
		}
		// Use the standing locations to find the correct game array
		Game[] placeToPut1 = standings[team1StandingLocation][team2StandingLocation];
		Game[] placeToPut2 = standings[team2StandingLocation][team1StandingLocation];

		// Find the first empty game to fill in, and add.
		for (int j = 0; j < placeToPut1.length; j++) {
			if (placeToPut1[j] == null) {
				standings[team1StandingLocation][team2StandingLocation][j] = game;
				break;
			}
		}
		// Keep the 2D array consistent by doing the same thing for the second
		// location.
		for (int k = 0; k < placeToPut2.length; k++) {
			if (placeToPut2[k] == null) {
				standings[team2StandingLocation][team1StandingLocation][k] = game;
				break;
			}
		}

	}

	public double[] calculateWinPercents() {
		double[] winPercents = new double[teams.length];
		int i = 0;
		for (Team t : teams) {
			winPercents[i] = t.getWinPercentage(t.getGames());
			i++;
		}
		return winPercents;
	}

	public StandingsPair[] generateStandings(double[] winPercents, Team[] teams) {
		StandingsPair[] standings = new StandingsPair[15];
		for (int i = 0; i < standings.length; i++) {
			StandingsPair s = new StandingsPair(winPercents[i], teams[i]);
			standings[i] = s;

		}
		Arrays.sort(standings);

		return standings;
	}

}
