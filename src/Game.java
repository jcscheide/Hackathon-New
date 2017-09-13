import java.util.Date;

public class Game {

	// Instance variables
	private int homeScore; // Points scored by home team
	private int awayScore; // Points scored by away team
	private Date date;
	private int winner; // The team ID of the winning team
	private int homeTeamID; // The team ID of the home team
	private int awayTeamID; // The team ID of the away team

	// Constructor
	public Game(Date date, int homeTeamID, int awayTeamID, int winner, int homeScore, int awayScore) {
		this.homeScore = homeScore;
		this.awayScore = awayScore;
		this.date = date;
		this.winner = winner;
		this.homeTeamID = homeTeamID;
		this.awayTeamID = awayTeamID;
	}

	// Getters and Setters
	public int getHomeScore() {
		return homeScore;
	}

	public void setHomeScore(int homeScore) {
		this.homeScore = homeScore;
	}

	public int getAwayScore() {
		return awayScore;
	}

	public void setAwayScore(int awayScore) {
		this.awayScore = awayScore;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getWinner() {
		return winner;
	}

	public void setWinner(int winner) {
		this.winner = winner;
	}

	public int getHomeTeamID() {
		return homeTeamID;
	}

	public void setHomeTeamID(int homeTeamID) {
		this.homeTeamID = homeTeamID;
	}

	public int getAwayTeamID() {
		return awayTeamID;
	}

	public void setAwayTeamID(int awayTeamID) {
		this.awayTeamID = awayTeamID;
	}

	// Methods

	public void printGame() {
		System.out.println(homeTeamID + " VS " + awayTeamID + " on " + (date.getMonth() + 1) + "/" + date.getDate()
				+ ", " + (date.getYear() + 1900) + "\n Score: " + homeScore + " - " + awayScore);
	}

}
