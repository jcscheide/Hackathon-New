
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Main {

	public static void main(String[] args) {
		
		
		File file = new File("out.txt"); //Your file
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PrintStream ps = new PrintStream(fos);
		System.setOut(ps);
		/*Set up the league*/
		Day [] days = createDays(); //Reads in the excel file, and links each game to a day.
		League league = createLeague(); //Creates teams, divisions, conference and a league. No games played yet.
		
		/*Play the NBA Season*/
		for(Day d: days){ //Loop through each day
			if(d == null){ //Added a max of 365 days, so ignore filler at the end.
				break;
			}
			d.addGamesFromDay(league); //Each day, 'play' the games that day has, and add them to all the teams and the standings
			
			/*Check for Elims*/
			checkForEliminations(league, d); //After playing all the games, check to see if anyone has been eliminated from playoff contention.
		}
		
	/*View Standings*/
	Conference east = league.getConferences()[0];
	StandingsPair[] eastStandings = east.generateStandings(east.calculateWinPercents(), east.getTeams());
	
	Conference west = league.getConferences()[1];
	StandingsPair[] westStandings = west.generateStandings(west.calculateWinPercents(), west.getTeams());
	
	System.out.println("_____________________________________________________");
		System.out.println("\n\nEast Final Standings:");
	for(int i = 0; i < eastStandings.length; i++){
		System.out.printf("%d: %s - %.3f\n" , (i + 1), eastStandings[i].getTeamName(), eastStandings[i].getWinPercent());
		System.out.println();
	}
	System.out.println("_____________________________________________________");
	
	System.out.println("\n\nWest Final Standings:");
	for(int i = 0; i < westStandings.length; i++){
		System.out.printf("%d: %s - %.3f\n" , (i + 1), westStandings[i].getTeamName(), westStandings[i].getWinPercent());
		System.out.println();
	}
	System.out.println("_____________________________________________________");
		
	/*Testing methods, not necessary to run */
		/* CHECK FOR GAMES GOING INTO TEAMS CORRECTLY
		for(Team t: league.getTeams()){
			System.out.println("Games for " + t.getTeamName());
			for(Game g: t.getGames()){
				if(g == null){
					break;
				}
				g.printGame();
			}
		}
		*/
		
		
		/* CHECK FOR GAMES GOING INTO LEAGUE CORRECTLY
		Game[][][] gameArr = league.getStandings();
		for(int k = 0; k < 30; k++){
			for(int l = 0; l < 30; l++){
				System.out.println("Games between " + (k + 1) + " and " + (l + 1));
				for(Game g3:gameArr[k][l]){
					if(g3 == null)
						break;
					g3.printGame();
				}
			}
		}
		*/ 
		
		/* CHECK FOR GAMES GOING INTO CONFERENCES CORRECTLY
		Game[][][] eastConfArr = league.getConferences()[0].getStandings();
		for(int k = 0; k < 15; k++){
			for(int l = 0; l < 15; l++){
				System.out.println("Games between " + (k + 1) + " and " + (l + 1));
				for(Game g3:eastConfArr[k][l]){
					if(g3 == null)
						break;
					g3.printGame();
				}
			}
		}
		
		Game[][][] westConfArr = league.getConferences()[1].getStandings();
		for(int k = 0; k < 15; k++){
			for(int l = 0; l < 15; l++){
				System.out.println("Games between " + (k + 1) + " and " + (l + 1));
				for(Game g3:westConfArr[k][l]){
					if(g3 == null)
						break;
					g3.printGame();
				}
			}
		}
		*/
		
		/* CHECK FOR GAMES GOING INTO DIVISIONS WORKING CORRECTLY
		Game[][][] atlanticArr = league.getConferences()[0].getDivisions()[0].getStandings();
		for(int k = 0; k < 5; k++){
			for(int l = 0; l < 5; l++){
				System.out.println("Games between " + (k + 1) + " and " + (l + 1));
				for(Game g3:atlanticArr[k][l]){
					if(g3 == null)
						break;
					g3.printGame();
				}
			}
		}
		*/
		
	}

	

	private static void checkForEliminations(League league, Day d) {
	
		for(Conference c: league.getConferences()){
			
			//Set up some structures.
			StandingsPair[] eightSeedDivisionStandings = new StandingsPair[5];
			StandingsPair[] tDivisionStandings = new StandingsPair[5];
			StandingsPair[] standings = c.generateStandings(c.calculateWinPercents(), c.getTeams());
			Team eightSeed = standings[7].getTeam();
			
			for(Team t: c.getTeams()){
				
				//If team has been eliminated already, don't check again.
				if(t.getIsEliminated()){
					continue;
				}
				
				//Rule 1: If the 8th seed team has more wins than team x can possibly accrue, they are eliminated from the playoffs.
				
				if(eightSeed.getNumWins(eightSeed.getGames()) - t.getNumWins(t.getGames()) > t.getGamesLeft()){
					//Eliminated!
					System.out.println("The " + t.getTeamName() + " have been eliminated from the playoffs on " + 
				(d.getDate().getMonth() + 1) + "/" + d.getDate().getDate() + "/" + (d.getDate().getYear() + 1900));
					t.setEliminated(true); //Don't check them ever again.
					continue;
				}
				
				//Rule 2: 8th seed team has equal more wins than x can accrue, but 8th seed team has won the season series.
						//Win differential == games left
				else if(eightSeed.getNumWins(eightSeed.getGames()) - t.getNumWins(t.getGames()) == t.getGamesLeft() 
						
						//and 8 seed has more wins
						&& ((eightSeed.getNumWins(eightSeed.getGamesVS(t.getTeamID())) > t.getNumWins(t.getGamesVS(eightSeed.getTeamID())) 
						//and played 4 games
						&& eightSeed.getNumWins(eightSeed.getGamesVS(t.getTeamID())) +  t.getNumWins(t.getGamesVS(eightSeed.getTeamID())) == 4) 		
						//They are division opponents, and the 8 seed holds the TB.
								
						//OR if 2-0 or 2-1		
						|| ((eightSeed.getNumWins(eightSeed.getGamesVS(t.getTeamID())) == 2) 
						//And they are in separate divisions, meaning they only play three games 
						&& (eightSeed.getDivID() != t.getDivID())
						))){
					//Eliminated
					System.out.println("The " + t.getTeamName() + " have been eliminated from the playoffs on " + 
							(d.getDate().getMonth() + 1) + "/" + d.getDate().getDate() + "/" + (d.getDate().getYear() + 1900));
								t.setEliminated(true);
								continue;
				}
				
				//Rule 3: Win differential == games left, above is false, eighth seed is division leader, t is not.
				
				//Create a division standings. As it loops in order, the first element will automatically have the highest win percentage.
				Division eightSeedDivision = new Division(-1);
				for(Division div: c.getDivisions()){
					if(div.getDivID() == eightSeed.getDivID()){
						eightSeedDivision = div;
					}
		
					for(int m = 0; m < standings.length; m++){
						if(standings[m].getTeam().getDivID() == eightSeedDivision.getDivID()){
							for(int n = 0; n < eightSeedDivisionStandings.length; n++){
								if(eightSeedDivisionStandings[n] == null){
									eightSeedDivisionStandings[n] = standings[m];
									break;
								}
							}
						}
					}	
				}
					//Create a division standings for the potentially eliminated team, too.
				
				Division tDivision = new Division(-1);
				for(Division div: c.getDivisions()){
					if(div.getDivID() == t.getDivID()){
						tDivision = div;
					}
					
					for(int m = 0; m < standings.length; m++){
						if(standings[m].getTeam().getDivID() == tDivision.getDivID()){
							for(int n = 0; n < tDivisionStandings.length; n++){
								if(tDivisionStandings[n] == null){
									tDivisionStandings[n] = standings[m];
									break;
								}
							}
						}
					}
					
					
				}
				//Win differential == games left)
				if(eightSeed.getNumWins(eightSeed.getGames()) - t.getNumWins(t.getGames()) == t.getGamesLeft() 
				//Eight seed is division leader
				&& eightSeed.getTeamID() == eightSeedDivisionStandings[0].getTeamID() 
				//T is not division leader
				&& t.getTeamID() != tDivisionStandings[0].getTeamID() 
				//And win diff between t and division leader is too high to eclipse.
				&& t.getGamesLeft() < t.getNumWins(t.getGames()) - tDivisionStandings[0].getTeam().getNumWins(tDivisionStandings[0].getTeam().getGames())
						){
					//Eliminated!
					System.out.println("The " + t.getTeamName() + " have been eliminated from the playoffs on " + 
							(d.getDate().getMonth() + 1) + "/" + d.getDate().getDate() + "/" + (d.getDate().getYear() + 1900));
								t.setEliminated(true);
								continue;
				}
			}
		}
		
		
		
	}

	private static Day[] createDays() {
		//Create days.
		Day[] days = new Day[365];
		int i = 0;
		Day d = null;
		/* READING FROM EXCEL FILE */
		try{
			FileInputStream file = new FileInputStream(new File("Analytics_Attachment.xlsx"));
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(1);
			Iterator<Row> rowIterator = sheet.iterator();
			
			while(rowIterator.hasNext()){
				Row row = rowIterator.next();
				
				Iterator<Cell> cellIterator = row.cellIterator();
				
				while(cellIterator.hasNext()){
					Cell cell = cellIterator.next();
					
					//If we're in the date column
					if(cell.getColumnIndex() == 0){
						
						
							//If no days exist, create a new day.
							if(d == null){
								d = new Day(new Game[15],cell.getDateCellValue());
							}
							//Or else if the game is on a different day to what we have right now
							else if(d.getDate().getDate() != cell.getDateCellValue().getDate()){
								//Add the previous day to the day array, as it's full with games
								days[i] = d;
								i++;
								//And create another new game.
								d = new Day(new Game[15],cell.getDateCellValue());
							}
							
								/*Read in each column on a single row */
								Game game = new Game(cell.getDateCellValue(),0, 0, 0, 0, 0);
								cell = cellIterator.next();
								int homeTeamID = getTeamIDFromName(cell.getStringCellValue());
								cell = cellIterator.next();
								int awayTeamID = getTeamIDFromName(cell.getStringCellValue());
								cell = cellIterator.next();
								int homeScore = (int)cell.getNumericCellValue();
								cell = cellIterator.next();
								int awayScore = (int)cell.getNumericCellValue();
								cell = cellIterator.next();
								
								int winner = 0;
								if(cell.getStringCellValue().equals("Home")){
								winner = homeTeamID;
								} else winner = awayTeamID;
								
								game.setHomeTeamID(homeTeamID);
								game.setAwayTeamID(awayTeamID);
								game.setHomeScore(homeScore);
								game.setAwayScore(awayScore);
								game.setWinner(winner);
								/* Add the game to the day */
								d.addGameToDay(game);			
					}
				}
			}
			file.close();
		} catch (Exception e){
			e.printStackTrace();
		}
		
		
		
		return days;
	}
	
	private static League createLeague() {
		
		/* Hard coding all the set-up variables in */
		
		Conference easternConference = new Conference(1);
		Conference westernConference = new Conference(2);
		
		
		Division atlantic = new Division(1);
		Division central = new Division(2);
		Division southeast = new Division(3);
		Division northwest = new Division(4);
		Division pacific = new Division(5);
		Division southwest = new Division(6);
		
		Team BOS = new Team(1, 1, 1);
		Team BKN = new Team(2, 1, 1);
		Team NYK = new Team(3, 1, 1);
		Team PHI = new Team(4, 1, 1);
		Team TOR = new Team(5, 1, 1);
		
		BOS.setTeamName("Boston Celtics");
		BKN.setTeamName("Brooklyn Nets");
		NYK.setTeamName("New York Knicks");
		PHI.setTeamName("Philadelphia 76ers");
		TOR.setTeamName("Toronto Raptors");
		
		atlantic.setTeams(new Team[]{BOS,BKN,NYK,PHI,TOR});
		
		
		Team CHI = new Team(6, 2, 1);
		Team CLE = new Team(7, 2, 1);
		Team DET = new Team(8, 2, 1);
		Team IND = new Team(9, 2, 1);
		Team MIL = new Team(10, 2, 1);
		
		CHI.setTeamName("Chicago Bulls");
		CLE.setTeamName("Cleveland Cavaliers");
		DET.setTeamName("Detroit Pistons");
		IND.setTeamName("Indiana Pacers");
		MIL.setTeamName("Milwaukee Bucks");
		
		central.setTeams(new Team[]{CHI,CLE,DET,IND,MIL});
		
		Team ATL = new Team(11, 3, 1);
		Team CHA = new Team(12, 3, 1);
		Team MIA = new Team(13, 3, 1);
		Team ORL = new Team(14, 3, 1);
		Team WAS = new Team(15, 3, 1);
		
		ATL.setTeamName("Atlanta Hawks");
		CHA.setTeamName("Charlotte Hornets");
		MIA.setTeamName("Miami Heat");
		ORL.setTeamName("Orlando Magic");
		WAS.setTeamName("Washington Wizards");
		
		southeast.setTeams(new Team[]{ATL,CHA,MIA,ORL,WAS});
		
		Team DEN = new Team(16, 4, 2);
		Team MIN = new Team(17, 4, 2);
		Team OKC = new Team(18, 4, 2);
		Team POR = new Team(19, 4, 2);
		Team UTA = new Team(20, 4, 2);
		
		DEN.setTeamName("Denver Nuggets");
		MIN.setTeamName("Minnesota Timberwolves");
		OKC.setTeamName("Oklahoma City Thunder");
		POR.setTeamName("Portland Trail Blazers");
		UTA.setTeamName("Utah Jazz");
		
		northwest.setTeams(new Team[]{DEN,MIN,OKC,POR,UTA});
		
		Team GSW = new Team(21, 5, 2);
		Team LAC = new Team(22, 5, 2);
		Team LAL = new Team(23, 5, 2);
		Team PHX = new Team(24, 5, 2);
		Team SAC = new Team(25, 5, 2);
		
		GSW.setTeamName("Golden State Warriors");
		LAC.setTeamName("LA Clippers");
		LAL.setTeamName("Los Angeles Lakers");
		PHX.setTeamName("Phoenix Suns");
		SAC.setTeamName("Sacramento Kings");
		
		pacific.setTeams(new Team[]{GSW,LAC,LAL,PHX,SAC});
		
		
		Team DAL = new Team(26, 6, 2);
		Team HOU = new Team(27, 6, 2);
		Team MEM = new Team(28, 6, 2);
		Team NOR = new Team(29, 6, 2);
		Team SAS = new Team(30, 6, 2);
		
		DAL.setTeamName("Dallas Mavericks");
		HOU.setTeamName("Houston Rockets");
		MEM.setTeamName("Memphis Grizzlies");
		NOR.setTeamName("New Orleans Pelicans");
		SAS.setTeamName("San Antonio Spurs");
		
		southwest.setTeams(new Team[]{DAL,HOU,MEM,NOR,SAS});
		
		Conference[] conferences = {easternConference, westernConference};
		Team[] teams = {BOS, BKN, NYK, PHI, TOR, CHI, CLE, DET, IND, MIL, ATL,
				CHA, MIA, ORL, WAS, DEN, MIN, OKC, POR, UTA, GSW, LAC, LAL, PHX,
				SAC, DAL, HOU, MEM, NOR, SAS};
		
		Team[] eastTeams = {BOS, BKN, NYK, PHI, TOR, CHI, CLE, DET, IND, MIL, ATL,
				CHA, MIA, ORL, WAS};
		Team[] westTeams = {DEN, MIN, OKC, POR, UTA, GSW, LAC, LAL, PHX,
				SAC, DAL, HOU, MEM, NOR, SAS};
		
		easternConference.setTeams(eastTeams);
		westernConference.setTeams(westTeams);
		
		Division[] eastDivisions = {atlantic,central,southeast};
		Division[] westDivisions = {northwest,pacific,southwest};
		
		easternConference.setDivisions(eastDivisions);
		westernConference.setDivisions(westDivisions);
		
		
		League league = new League(conferences,teams);
		return league;
		
		
		
		
	}
	//Helper function to give IDs when I only know the name.
	public static int getTeamIDFromName(String teamName){
		switch (teamName){
		case "Boston Celtics": return 1;
		case "Brooklyn Nets": return 2;
		case "New York Knicks": return 3;
		case "Philadelphia 76ers": return 4;
		case "Toronto Raptors": return 5;
		case "Chicago Bulls": return 6;
		case "Cleveland Cavaliers": return 7;
		case "Detroit Pistons": return 8;
		case "Indiana Pacers": return 9;
		case "Milwaukee Bucks": return 10;
		case "Atlanta Hawks": return 11;
		case "Charlotte Hornets": return 12;
		case "Miami Heat": return 13;
		case "Orlando Magic": return 14;
		case "Washington Wizards": return 15;
		case "Denver Nuggets": return 16;
		case "Minnesota Timberwolves": return 17;
		case "Oklahoma City Thunder": return 18;
		case "Portland Trail Blazers": return 19;
		case "Utah Jazz": return 20;
		case "Golden State Warriors": return 21;
		case "LA Clippers": return 22;
		case "Los Angeles Lakers": return 23;
		case "Phoenix Suns": return 24;
		case "Sacramento Kings": return 25;
		case "Dallas Mavericks": return 26;
		case "Houston Rockets": return 27;
		case "Memphis Grizzlies": return 28;
		case "New Orleans Pelicans": return 29;
		case "San Antonio Spurs": return 30;
		

		}
		return -1;
		
			
		
			
			
	}

}
