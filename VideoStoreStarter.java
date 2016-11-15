import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;


public class VideoStoreStarter implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5297122086844283601L;
	private static Member CC = null;
	private static final String[] MAIN_MENU = {
			"log in", //0
			"log out", //1
			"list all titles", //2
			"list available titles", //3
			"list my rented titles", //4
			"rent title", //5
			"return title", //6
			"exit" //7
	};

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		/*Read in files to make sure current runtime is most recent */
		Scanner kybd = new Scanner(System.in);
		int userChoice = -1;
		while (userChoice != 7) {
			userChoice = Utils.userChoose(kybd, MAIN_MENU, "Make Your Selection: ");
			System.out.println("Choice is " + MAIN_MENU[userChoice]);
			if (userChoice == 0) {
				login(kybd);
			} else if (userChoice == 1) {
				System.out.println("Logging Out...");
				CC = null;
			} else if (userChoice == 2) {
				Utils.listing(Title.getAllTitles());
			} else if (userChoice == 3) {	
				Utils.listing(Title.getAvailableTitles());
			} else if (userChoice == 4) {
				Utils.listing(CC.getMyRented());
			} else if (userChoice == 5) {
				pickTitle(kybd);
			} else if (userChoice == 6) {
	
			}
			
		}
	}
	public static void pickTitle(Scanner kybd)
	{
		ArrayList<Title> l = (ArrayList<Title>)Title.getAllTitles();
		int i = Utils.userChoose(kybd, l, "Choose Your Movie");
	}
	public static void login(Scanner kybd)
	{
		ArrayList<Member> l = (ArrayList<Member>) Member.getAll();
		CC = l.get(Utils.userChoose(kybd, l, "Choose The Member"));
		System.out.println(CC);
	}
}
