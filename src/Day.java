import java.util.Date;

public class Day {
	//Instance variables
	private Game[] games;
	private Date date;
	
	//Constructor
	
	public Day(Game[] games, Date date){
		this.games = games;
		this.date = date;
	}
	
	//Getters and setters
	public Game[] getGames() {
		return games;
	}
	public void setGames(Game[] games) {
		this.games = games;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	//Methods
	public void addGamesFromDay(League league){
		for(Game g:games){
			league.addGameToEverything(g);
		}
	}
	
	public void addGameToDay(Game game){
		for (int i = 0; i < games.length; i++){
			if(games[i] == null){
				games[i] = game;
				return;
			}
		}
	}
	
}
