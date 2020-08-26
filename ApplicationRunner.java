
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.System.exit;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class ApplicationRunner {

    static String file = System.getProperty("user.dir") + File.separator + "wordlist.txt";   //words file
    static int life = 0;                                                                     //maximum number of tries
    static String wrongLetters = " ";                                                        //string of wrong guesses
    static Scanner scan = new Scanner(System.in);                                            //scanner     
    static String hiddenWord;                                                                //word to guess 

    //method to find the number of lines in the file
    public static int numberOfLines() throws IOException {

        int lines = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.readLine() != null) {
                lines++;
            }
        }
        return lines;
    }

    //method to pick a random word from the file by generating a random integer between 0 and the total number of lines in the file
    public static String hiddenWord() throws IOException {
        Random randomWord = new Random();
        String word = Files.readAllLines(Paths.get(System.getProperty("user.dir") + File.separator + "wordlist.txt")).get(randomWord.nextInt(numberOfLines()));
        return word;
    }

    //the static string (word to guess) becoming a random word from the file and converted to lower case
    static {
        try {
            hiddenWord = hiddenWord().toLowerCase();
        } catch (IOException ex) {
        }
    }
    static String[] wordArray; //an array which is used to generate blanks for the random words

    //method for main menu where the user can select to play or exit the game
    public static void mainMenu() throws IOException {

        //resetting the wrong guesses, life and wordArray for a new game
        wrongLetters = " ";
        life = 0;
        wordArray = new String[hiddenWord.length()];
        Arrays.fill(wordArray, "_ ");

        System.out.print("Play (1) or Exit (0) > ");

        String input = scan.nextLine();

        if (null == input) {
            System.out.println("Please choose 1 or 0");
            mainMenu();
        } else {
            switch (input) {
                case "1": //the game starts and keeps going as long as tbe user has below 10 wrong guesses
                    while (life < 10) {
                        game();
                    }
                    break;
                case "0": //exit program                                             
                    System.out.println("Program closed");
                    exit(0);
                default: //if user enters anything except 1 or 0 the console tells them to choose between 1 or 0
                    System.out.println("Please choose 1 or 0");
                    mainMenu();
                    break;
            }
        }
    }

    //main method
    public static void main(String[] args) {

        //welcome message
        System.out.println("Word Guessing Game");

        //main menu starts
        try {
            mainMenu();
        } catch (IOException ex) {
            System.out.println("Error. File not found");
        }
    }

    //method for the game
    public static void game() throws IOException {

        //displaying the wrong guesses alphabetically by converting them to character array and sorting
        char[] charArray = wrongLetters.toCharArray();
        Arrays.sort(charArray);
        String sortedString = new String(charArray);
        wrongLetters = (sortedString.trim().replace("", " ")); //adding spaces between the letters

        //displaying the hidden word blanks
        for (String x : wordArray) {
            System.out.print(x);
        }

        System.out.println("");
        System.out.println(life + " wrong guesses so far " + "[" + wrongLetters + "]"); //shows number of wrong guesses so far and the wrong letters
        System.out.println("Have a guess (lower case letter or * to give up)");
        System.out.print("> ");
        String userinput = scan.nextLine().toLowerCase(); //user input and is converted to lower case

        //if * is entered during the game the user gives up and the game is over
        if ("*".equals(userinput)) {
            System.out.println("You gave up!");
            gameOver();
            System.out.println("You lose :-(");
            System.out.println("");
            mainMenu(); //goes back to main menu
        }

        if (userinput.length() <= 1) {                            //checks if the user only inputs one character at a time
            if (userinput.matches("[A-Za-z]")) {                      //checks if the input is a letter
                if (!Arrays.toString(wordArray).contains(userinput)) {     //checks if the letter has already been used
                    if (!wrongLetters.contains(userinput)) {                  //checks if the guess has already been attempted

                        //if the hidden word contains the user's guess it replaces the blank(s) at the respective position where the letter matches    
                        if (hiddenWord.contains(userinput)) {

                            for (int i = 0; i <= hiddenWord.length() - 1; i++) {

                                if (hiddenWord.substring(i, i + 1).equals(userinput)) {
                                    Array.set(wordArray, i, (userinput + " "));
                                }
                            }
                        } //if the hidden word doesn't contain the letter its a wrong guess and the letter is added to the string to wrong guesses and number of wrong guesses is increased
                        else {
                            wrongLetters = wrongLetters.concat(userinput);
                            life++;
                        }
                    } else {
                        System.out.println("letter already used");
                    }
                } else {
                    System.out.println("letter already used");
                }
            } else {
                System.out.println("Please enter only letters");
            }
        } else {
            System.out.println("Please enter only one letter at a time");
        }

        //when the user exceeds 10 wrong guesses the game is over
        if (life == 10) {
            gameOver();
            System.out.println("You lose :-(");
            System.out.println("");
            mainMenu();
        }

        //if the hidden word has no more blanks, the user wins
        if (!Arrays.toString(wordArray).contains("_")) {
            gameOver();
            System.out.println("You win :-)");
            System.out.println("");
            mainMenu(); //return the user to the main menu
        }

    }

    //game over method
    public static void gameOver() throws IOException {

        //sorting the wrongwords in alphabetical order again to accomodate the last guess before the game ended
        char[] charArray = wrongLetters.toCharArray();
        Arrays.sort(charArray);
        String sortedString = new String(charArray);
        wrongLetters = (sortedString.trim().replace("", " ")); //adding spaces between the letters

        for (String x : wordArray) {
            System.out.print(x);
        }
        System.out.println("");
        System.out.println(life + " wrong guesses so far " + "[" + wrongLetters + "]");
        {
            System.out.println("");
            System.out.println("The hidden word was " + hiddenWord.toUpperCase());
        } //show the hidden word 
        hiddenWord = hiddenWord().toLowerCase();  //reset the hidden word to another word if in case the user plays the game again

    }
}
