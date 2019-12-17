
// Import For Hashing
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.nio.file.*;
import java.nio.charset.StandardCharsets;

import java.math.BigInteger;

import java.io.IOException;
import java.io.File;

import java.util.Locale;
import java.util.Date;
import java.util.TimeZone;
import java.util.Scanner;

import java.text.NumberFormat;

public class SHACrack {

	public static String fileName = "";
	public static String hash     = "";
	public static String hashType = "256";
	public static String hexType  = "%064x";

	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {

		// Create console colors
		ConsoleColors color = new ConsoleColors();
		NumberFormat format = NumberFormat.getInstance(Locale.US);


		// Logo
		String color1 = color.CYAN;
		String color2 = color.RED;

		String logo = color.RED + "   _____ _    _    "+color1+"      _____                _             \n"
         + color.RED + "  / ____| |  | |   /\\ "+color1+"  / ____|              | |\n"
         + color.RED + " | (___ | |__| |  /  \\ "+color1+"| |     _ __ __ _  ___| | _____ _ __\n"
         + color.RED + "  \\___ \\|  __  | / /\\ \\"+color1+"| |    | '__/ _` |/ __| |/ / _ \\ '__|\n"
         + color.RED + "  ____) | |  | |/ ____ \\"+color1+" |____| | | (_| | (__|   <  __/ |\n"
         + color.RED + " |_____/|_|  |_/_/    \\_\\"+color1+"_____|_|  \\__,_|\\___|_|\\_\\___|_|\n";

		System.out.println(logo);

		// Run the setup method to get user input
		setup(color);

		// Create SHA-256 Method
		MessageDigest md = MessageDigest.getInstance("SHA-" + hashType);

		// Open file
		File file = new File(fileName);

		if(file.exists() && !file.isDirectory()) { 

	     	Scanner inputFile = new Scanner(file);

	     	int lineCount = 1;
	     	int lineUpdater = 1;


	     	// First get file lines
			Path path = Paths.get(fileName);
			long totalLines = Files.lines(path).count();

	     	// Capture the current time for later usage
	     	long startTime = System.currentTimeMillis();

	     	while (inputFile.hasNext()) {

	     		String currentLine = inputFile.nextLine();

	     		if (!inputFile.hasNext()) {
	     			System.out.println(color.WHITE + color.RED_BACKGROUND + "\n Hash Coundn't Be Found! ");
	     		}

	     		if(encrypt256(md, currentLine).equals(hash)) {
	     			// Get the new time
	     			long endTime = System.currentTimeMillis();
	     			long totalTime = endTime - startTime;

	     			System.out.println(color.YELLOW + "\n---------- Hash Found ----------");

	     			if (totalTime > 1000) {
	     				totalTime = totalTime / 1000;
	     				System.out.println(color.YELLOW + "Time  : " + color.GREEN + totalTime + color.YELLOW + " Seconds");
	     			} else {
	   					System.out.println(color.YELLOW + "Time  : " + color.GREEN + totalTime + color.YELLOW + " Milliseconds");
	     			}

	     			System.out.println(color.YELLOW + "File  : " + color.GREEN + fileName);
	     			System.out.println(color.YELLOW + "Type  : " + color.GREEN + "SHA-" + hashType);
	     			System.out.println(color.YELLOW + "Line  : " + color.GREEN + lineCount);
	     			System.out.println(color.YELLOW + "Hash  : " + color.RED + hash);
	     			System.out.println(color.YELLOW + "Value : " + color.GREEN + currentLine);

	     			System.out.println("\n" + color.RED + hash + color.WHITE + " = " + color.GREEN + currentLine);

	     			break;

	     		} else {
	     			if (lineUpdater >= 10000) {
	     				// Every 1,000 lines, update the status
	     				back(100);
	     				progressPercentage(color, (int)lineCount, (int)totalLines);
	     				System.out.print("  " + color.WHITE + color.RED_BACKGROUND + " " + lineCount + "/" + totalLines +" \r" + color.RESET);
						// System.out.print(color.WHITE + color.RED_BACKGROUND + " Currently On Line : " + format.format(lineCount) + " ");
	     				// Reset the updater
	     				lineUpdater = 0;
	     			}
	     		}
	     		lineUpdater++;
	     		lineCount++;
	     	}

	    } else {
	    	System.out.println(color.WHITE+color.RED_BACKGROUND + " Oops! Your file was not found. ");
	    }

	}

	public static String encrypt256(MessageDigest md, String text) {
	    md.update(text.getBytes(StandardCharsets.UTF_8));
	    byte[] digest = md.digest();
	    String hex = String.format(hexType, new BigInteger(1, digest));
	    return hex;
	}

	public static void setup(ConsoleColors color) {
		// Grab user input
		Scanner userInput = new Scanner(System.in);

		System.out.print(color.CYAN + "File Name: " + color.RESET);
		fileName = userInput.nextLine();

		System.out.print(color.CYAN + "SHA Type (1,256,512): " + color.RESET);
		hashType = userInput.nextLine();

		if (hashType.equals("1")) {
			hexType = "%032x";
		}

		System.out.print(color.CYAN + "SHA-" + hashType + " Hash: " + color.RESET);
		hash = userInput.nextLine().toLowerCase();
	}

	public static void back(int times) {
		if(times > 0){
			System.out.print("\b");
			back(times-1);
		}
	}

	public static void progressPercentage(ConsoleColors color, int remain, int total) {
	    if (remain > total) {
	        throw new IllegalArgumentException();
	    }
	    int maxBareSize = 10; // 10unit for 100%
	    int remainProcent = ((100 * remain) / total) / maxBareSize;
	    char defaultChar = ' ';
	    String icon = color.GREEN + "#" + color.RESET;
	    String bare = new String(new char[maxBareSize]).replace('\0', defaultChar) + "]";
	    StringBuilder bareDone = new StringBuilder();
	    bareDone.append("[");
	    for (int i = 0; i < remainProcent; i++) {
	        bareDone.append(icon);
	    }
	    String bareRemain = bare.substring(remainProcent, bare.length());
	    System.out.print("\r" + bareDone + bareRemain + " " + remainProcent * 10 + "%");
	    if (remain == total) {
	        System.out.print("\n");
	    }
	}

}