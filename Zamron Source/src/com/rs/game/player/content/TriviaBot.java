package com.rs.game.player.content;

import java.util.Random;

import com.rs.game.World;
import com.rs.game.player.Player;
import com.rs.utils.Misc;

/**
 * @Author: Justin
 */
public class TriviaBot {
	
	
	private static String songs [][] = { 
		  {"I’m a predator, rapture, I am killin it", "killin it"},
		  {"And we could be together baby As long as skies are blue", "Summer"},
		  {"Hands up and touch the sky, Let’s do this one last time", "Starships"},
		  {"If I'm a bad person, you don't like me, Well I guess I'll make my own way", "Ignorance"},
		  {"Seven a.m., waking up in the morning, Gotta be fresh, gotta go downstairs", "Friday"},
		  {"Hey I just met you, and this is crazy", "Call Me Maybe"},
		  {"I'm gonna pop some tags, Only got twenty dollars in my pocket", "Thrift shop"},
		  {"They say pain is an illusion, This is just a bruise And you are just confused", "Human"},
		  {"It's just a spark. But it's enough to keep me going. And when it's dark out, no one's around. It keeps glowing", "Last Hope"}
		  };
	
	private static String puzzles [][] = { 
			  {"Figure out this anagram: 'Arc O Line'", "Caroline"},
			  {"Figure out this anagram: 'Are Col'", "Oracle"},
			  {"Figure out this anagram: 'El Ow'", "Lowe"},
			  {"Figure out this anagram: 'Halt Us'", "Luthas"},
			  {"Figure out this anagram: 'Lark In Dog'", "King Roland"},
			  {"Figure out this anagram, 'Me if'","Femi"}, 
			  {"Figure out this anagram, 'Peaty Pert'","Party Pete"},
			  {"Figure out this anagram, 'Snah'","Hans"}
			  };
	
	private static String server [][] = { 
		  {"What was the first obsidian weapon Jagex released?", "Dark Dagger"},
		  {"What grants you the Fire Cape?", "Fight Caves"},
		  {"What is the maximum total level you can achieve?", "2496"},
		  {"What gaming genre is Runescape?", "MMORPG"},
		  {"What is the max skill cap?", "120"},
		  {"How many portals are there at clan wars?", "3"},
		  {"What weapon is Tormented Demon's weakness?", "Darklight"},
		  {"How many barrows brothers were there originally?", "6"},
		  {"In what year was the Duplication Glitch in Runescape?", "2003"},
		  {"What is the first ancient spell?", "Smoke Rush"},
		  {"What is the most powerful curse?", "Turmoil"},
		  {"What is the best free to play armour?", "Rune"},
		  {"How much xp is required to achieve 120 Dungeoneering?", "104m"},
		  {"What do you receive when a fire disappears?", "Ashes"},
		  {"What is the name of the kiln cape?", "TokHaar-Kal"},
		  {"What is the maximum amount of xp you can gain per skill?", "200m"},
		  {"What is the strongest Dungeoneering weapon type?", "Primal"},
		  {"What's the first four digits of a max stack?", "2147"},
		  {"What Prayer level do you need for Turmoil?", "95"},
		  {"What's the strongest spirit shield type?", "Divine"},
		  {"How much Mill do you need to make your cash turn Green?", "10m"},
		  {"What is maximum combat level in Zamron?", "138"},
		  {"What skill levels do you need to prestige?", "99"},
		  {"What Rank is a gold Crown?", "Administrator"},
		  {"What Rank is a Silver Crown?", "Moderator"},
		  {"What is the name of this Server?", "Zamron"},
		  {"How many Dungeoneering points are needed to buy a Chaotic?", "150k"},
		  {"How many thieving stalls in total are there at home?", "5"},
		  {"What npc sells skillcapes?", "Wise old man"},
		  {"This server is coded in what language?", "Java"} };
	
	private static String general [][] = { 
		  {"Is a tomato a fruit or a vegetable?", "Fruit"},
		  {"How many legs does a spider have?", "8"},
		  {"What music genre goes wub wub wub?", "Dubstep"},
		  {"How many continents are there.", "7"},
		  {"How many bones are in a fully grown human?", "206"},
		  {"How Many wisdom teeth can grow in your mouth?", "4"},
		  {"Which popular singer was called 'The King of Rock 'N' Roll","Elvis Presley"},
		  {"Who directed Jaws?","Steven Spielberg"},
		  {"Who is the founder of Apple?","Steve Jobs"},
		  {"First one to answer 'with 1', wins!", "With 1"},
		  {"What is the largest State in the USA?", "Alaska"},
		  {"Which spirit is typically mixed with orange juice to create the cocktail known as 'screwdriver'?", "Vodka"},
		  {"What is a barracuda?", "A fish"},
		  {"Who had a hit with 'Firework' in 2010?", "Katy Perry"},
		  {"What is the name of the planet that Superman comes from?", "Krypton"},
		  {"What colour is a New York taxi?", "Yellow"},
		  {"What is the biggest man-made structure on Earth?", "The Great Wall of China"},
		  {"Where does the president of the United States of America reside?", "The White House"},
		  {"Which country's flag features a maple leaf?", "Canada"},
		  {"In Roman numerals, what amount does the letter M equal?", "1000"},
		  {"What language is spoken in Austria?", "German"},
		  {"What is a Vixen?", "A female fox"},
		  {"How many cards are there in a pack of cards?", "52"} };
	
	private static String movies [][] = { 
		  {"Oh no, Leonardo is sinking!", "Titanic"},
		  {"Wait, it's a dream?!?", "Inception"},
		  {"Pie, Boobs, and more!", "American Pie"},
		  {"This movie gives me a nightmare!", "Nightmare on Elm Street"},
		  {"Here's Johnny!", "The Shining"},
		  {"Retired and Extremely Dangerous.", "RED"},
		  {"Oh no, it's Jason with a machete!", "Friday the 13th"},
		  {"You don't want to swim in Cape Cod anymore.", "Jaws"},
		  {"So after all this, I'm the missing patient?", "Shutter Island"},
		  {"The greatest hiests of Boston.", "Town"},
		  {"The greatest parody including the movie Narnia.", "Epic Movie"},
		  {"The series of scary movie parodies.", "Scary Movie"},
		  {"*flicks lighter in attic*", "The Grudge"},
		  {"Too fast for you?", "Fast and Furious"},
		  {"The world is ending!", "2012"},
		  {"Tsunami survival and reuniting.", "The Impossible"},
		  {"Good old James Bond.", "007"} };
	
	private static String categories [][][] = { songs, puzzles, server, general, movies };
	
	public static int questionid = -1;
	public static int round = 0;
	public static boolean victory = false;
	public static int answers = 0;
	public static int category;

	public TriviaBot() {
		//TODO
	}
	
	public static void Run() {
		category = Misc.random(0, 4);
		int rand = RandomQuestion(category);
		questionid = rand;
		answers = 0;
		victory = false;
		String title = "Trivia";
		if (category == 0)
			title = "Name the Song";
		else if (category == 1)
			title = "Puzzles";
		else if (category == 2)
			title = "RuneScape/Server";
		else if (category == 3)
			title = "General Trivia";
		else if (category == 4)
			title = "Name the Movie";
		for(Player participant : World.getPlayers()) {
			if(participant == null)
				continue;
				participant.hasAnswered = false;
				participant.getPackets().sendGameMessage("<col=56A5EC>["+title+"] "+categories[category][rand][0]+"</col>");
		}
	}
	
	public static void sendRoundWinner(String winner, Player player) {
		for(Player participant : World.getPlayers()) {
			if(participant == null)
				continue;
			if (answers <= 3) {
				answers++;
				if (answers == 3)
					victory = true;
				player.TriviaPoints++;
				player.getPackets().sendGameMessage("<col=56A5EC>[Trivia] "+winner+", you now have "+player.TriviaPoints+" Trivia Points.</col>");
				player.hasAnswered = true;
				World.sendWorldMessage("<col=56A5EC>[Winner] <col=FF0000>"+ winner +"</col><col=56A5EC> answered the question correctly ("+answers+"/3)!</col>", false);
				return;
			}
		}
	}
	
	public static void verifyAnswer(final Player player, String answer) {
		if(victory) {
			player.getPackets().sendGameMessage("That round has already been won, wait for the next round.");
		} else if (player.hasAnswered) {
			player.getPackets().sendGameMessage("You have already answered this question.");
		} else if(categories[category][questionid][1].equalsIgnoreCase(answer)) {
			round++;
			sendRoundWinner(player.getDisplayName(), player);
		} else {
			player.getPackets().sendGameMessage("That answer wasn't correct, please try it again.");
		}
	}
	
	public static int RandomQuestion(int i) {
		int random = 0;
		Random rand = new Random();
		random = rand.nextInt(categories[i].length);
		return random;
	}
	
	public static boolean TriviaArea(final Player participant) {
		if(participant.getX() >= 2630 && participant.getX() <= 2660 && participant.getY() >= 9377 && participant.getY() <= 9400) {
			return true;
		}
		return false;
	}
}
