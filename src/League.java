
public class League {

	// Instance variables
	private Conference[] conferences;
	private Game[][][] standings;
	private Team[] teams;

	// Constructor
	public League(Conference[] conferences, Team[] teams) {
		this.conferences = conferences;
		standings = new Game[30][30][4];
		this.teams = teams;
	}

	// Getters and setters
	public Conference[] getConferences() {
		return conferences;
	}

	public void setConferences(Conference[] conferences) {
		this.conferences = conferences;
	}

	public Game[][][] getStandings() {
		return standings;
	}

	public void setStandings(Game[][][] standings) {
		this.standings = standings;
	}

	public Team[] getTeams() {
		return teams;
	}

	public void setTeams(Team[] teams) {
		this.teams = teams;
	}

	// Methods

	public void addGameToEverything(Game game) {

		// Add the game to the league standings every time.

		if (game == null) {
			return;
		}
		/*
		 * System.out.println("Attempting to add game:"); game.printGame();
		 */

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

		// Use the conference add method if they're in the same conference. This
		// will add to conference, division and team.
		if (team1.getConfID() == team2.getConfID()) {
			for (Conference c : conferences) {
				if (c.getConfID() == team1.getConfID()) {
					c.addGameToDivTeamConf(game);
				}
			}
		}

		// If they're not in the same conference, they aren't in the same
		// division, so we just need to add it to the teams.
		else {
			team1.addGame(game);
			team2.addGame(game);
		}

		
	}

}
