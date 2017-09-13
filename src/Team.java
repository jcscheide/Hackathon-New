
public class Team {

	// Instance variables
	private Game[] games;
	private int teamID; // Team ID must be > 0;
	private int divID; // Div ID must be > 0;
	private int confID; // Conf ID must be 1 or 2
	private String teamName;
	private boolean isEliminated;

	// Constructor
	public Team(int teamID, int divID, int confID) {
		games = new Game[82];
		this.teamID = teamID;
		this.divID = divID;
		this.confID = confID;
		isEliminated = false;
	}

	// Getters and Setters
	public Game[] getGames() {
		return games;
	}

	public void setGames(Game[] games) {
		this.games = games;
	}

	public int getTeamID() {
		return teamID;
	}

	public void setTeamID(int teamID) {
		this.teamID = teamID;
	}

	public int getDivID() {
		return divID;
	}

	public void setDivID(int divID) {
		this.divID = divID;
	}

	public int getConfID() {
		return confID;
	}

	public void setConfID(int confID) {
		this.confID = confID;
	}

	public boolean getIsEliminated() {
		return isEliminated;
	}

	public void setEliminated(boolean b) {
		isEliminated = b;
	}

	// Methods

	// Returns an array of the games the team has played against a given team.
	public Game[] getGamesVS(int teamVSID) {
		Game[] gamesVS = new Game[4];
		for (Game g : games) {
			if (g == null) {
				break;
			}
			if (g.getAwayTeamID() == teamVSID || g.getHomeTeamID() == teamVSID) {
				for (int i = 0; i < gamesVS.length; i++) {
					if (gamesVS[i] == null) {
						gamesVS[i] = g;
						break;
					}
				}
			}
		}
		return gamesVS;
	}

	// Calculate the Win Percentage for any given array of games. Typically use
	// the games array, but can use any (ie. gamesVS)
	public double getWinPercentage(Game[] games) {
		double wins = 0;
		double losses = 0;
		for (Game g : games) {
			// Check if the game has not yet been added.
			if (g == null) {
				break;
			}

			if (g.getWinner() == this.teamID) {
				wins = wins + 1.0;
			} else
				losses = losses + 1.0;
		}
		return wins / (wins + losses);
	}

	// Add an individual game to the season
	public void addGame(Game newGame) {
		for (int i = 0; i < games.length; i++) {
			if (games[i] == null) {
				games[i] = newGame;
				return;
			}
		}
	}

	// Add an array of games to the season
	public void addGames(Game[] newGames) {
		for (int j = 0; j < newGames.length; j++) {
			if (newGames[j] == null)
				break;
			addGame(newGames[j]);
		}
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	// Returns number of wins a team has had.
	public int getNumWins(Game[] games) {
		int wins = 0;
		for (Game g : games) {
			if (g == null) {
				return wins;
			}
			if (g.getWinner() == teamID) {
				wins++;
			}
		}
		return wins;
	}

	// Returns number of games a team has left in their season.
	public int getGamesLeft() {
		int gamesPlayed = 0;
		for (int i = 0; i < games.length; i++) {
			if (games[i] != null) {
				gamesPlayed++;
			} else
				break;
		}
		return 81 - gamesPlayed;
	}

}