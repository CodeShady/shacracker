/*

  _____ _    _          _____                _    
 / ____| |  | |   /\   / ____|              | |   
| (___ | |__| |  /  \ | |     _ __ __ _  ___| | __
 \___ \|  __  | / /\ \| |    | '__/ _` |/ __| |/ /
 ____) | |  | |/ ____ \ |____| | | (_| | (__|   < 
|_____/|_|  |_/_/    \_\_____|_|  \__,_|\___|_|\_\            

░░░░░░░░░░░░░░▄▄▄▄▄▄▄▄▄▄▄▄░░░░░░░░░░░░░░
░░░░░░░░░░░░▄████████████████▄░░░░░░░░░░
░░░░░░░░░░▄██▀░░░░░░░▀▀████████▄░░░░░░░░
░░░░░░░░░▄█▀░░░░░░░░░░░░░▀▀██████▄░░░░░░
░░░░░░░░░███▄░░░░░░░░░░░░░░░▀██████░░░░░
░░░░░░░░▄░░▀▀█░░░░░░░░░░░░░░░░██████░░░░
░░░░░░░█▄██▀▄░░░░░▄███▄▄░░░░░░███████░░░
░░░░░░▄▀▀▀██▀░░░░░▄▄▄░░▀█░░░░█████████░░
░░░░░▄▀░░░░▄▀░▄░░█▄██▀▄░░░░░██████████░░
░░░░░█░░░░▀░░░█░░░▀▀▀▀▀░░░░░██████████▄░
░░░░░░░▄█▄░░░░░▄░░░░░░░░░░░░██████████▀░
░░░░░░█▀░░░░▀▀░░░░░░░░░░░░░███▀███████░░
░░░▄▄░▀░▄░░░░░░░░░░░░░░░░░░▀░░░██████░░░
██████░░█▄█▀░▄░░██░░░░░░░░░░░█▄█████▀░░░
██████░░░▀████▀░▀░░░░░░░░░░░▄▀█████████▄
██████░░░░░░░░░░░░░░░░░░░░▀▄████████████
██████░░▄░░░░░░░░░░░░░▄░░░██████████████
██████░░░░░░░░░░░░░▄█▀░░▄███████████████
███████▄▄░░░░░░░░░▀░░░▄▀▄███████████████

	SHACrack - By: CodeShady
		Written In Java.
*/
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

	public static String fileName   = "";
	public static String hashString = "";
	public static String hashType   = "256";
	public static String hexType    = "%064x";
	public static boolean isFile    = false;
	public static File hashFile     = null;
	public static long totalLines   = 0;
	public static boolean quickMode = false;

	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {

		// Create console colors
		ConsoleColors color = new ConsoleColors();
		NumberFormat format = NumberFormat.getInstance(Locale.US);

		System.out.println(logo(color));

		// Run the setup method to get user input
		setup(color);

		// Find the total number of lines in wordlist
		Path path = Paths.get(fileName);
		totalLines = Files.lines(path).count();

		// Create SHA-256 Method
		MessageDigest md = MessageDigest.getInstance("SHA-" + hashType);

		if (!isFile) {
			// User has not entered a file
			crackHash(hashString, md, format, color);
		} else {
			// User wants to crack hashes from file
			if (!hashFile.isDirectory()) {
				Scanner hashFileLines = new Scanner(hashFile);
				while (hashFileLines.hasNext()) {
					String currentLine = hashFileLines.nextLine().toLowerCase();
					crackHash(currentLine, md, format, color);
				}
			} else {
				logError(color, "Error! File Is A Directory!");
				System.exit(1);
			}
		}

	}

	public static void crackHash(String hash, MessageDigest md, NumberFormat format, ConsoleColors color) throws IOException {

		int hashLen = hash.length();

		if (hashLen == 40 || hashLen == 64 || hashLen == 128) {

			// Open file
			File file = new File(fileName);

			if(file.exists() && !file.isDirectory()) { 

		     	Scanner inputFile = new Scanner(file);

		     	int lineCount = 1;
		     	int lineUpdater = 1;

		     	// Capture the current time for later usage
		     	long startTime = System.currentTimeMillis();

		     	// Show progress bar
		     	progressPercentage(color, (int)lineCount, (int)totalLines);
		     	System.out.print("  " + color.WHITE + color.RED_BACKGROUND + " " + lineCount + "/" + totalLines +" \r" + color.RESET);

		     	// Get crackin'
		     	while (inputFile.hasNext()) {

		     		String currentLine = inputFile.nextLine();

		     		if (!inputFile.hasNext()) {
		     			logError(color, "Hash Coundn't Be Found!");
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

		     			System.out.println("\n" + color.RED + hash + color.WHITE + " = " + color.GREEN + currentLine + color.RESET + "\n");

		     			break;

		     		} else {
		     			if (lineUpdater >= 10000) {
		     				// Every 1,000 lines, update the status
		     				back(100);
		     				progressPercentage(color, (int)lineCount, (int)totalLines);
		     				System.out.print("  " + color.WHITE + color.RED_BACKGROUND + " " + lineCount + "/" + totalLines +" \r" + color.RESET);
		     				// Reset the updater
		     				lineUpdater = 0;

		     				long endTime = System.currentTimeMillis();

		     				// Check if the time has exeeded more than 5 seconds
		     				if (quickMode && ((endTime - startTime) / 1000) > 5) {
		     					// This is a quick scan. So if this hash has taken more than
		     					// 5 seconds.. Well... Your Times Up Bud.
		     					logError(color, "Quick Mode - Skipping Hash");
		     					break;
		     				}
		     			}
		     		}
		     		lineUpdater++;
		     		lineCount++;
		     	}

		    } else {
		    	logError(color, "Oops! Your File Was Not Found.");
		    }

		} else {
			logError(color, "Yikes! That's Not a Valid Hash. Skippping!");
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

		if (fileName.contains(" -quick")) {
			quickMode = true;
			log(color, "Quick Mode Selected");
			// Replace the -quick so this don't get no stinkin' errors
			fileName = fileName.replace(" -quick", "");
		} else if (fileName.contains(" -q")) {
			quickMode = true;
			log(color, "Quick Mode Selected");
			// Replace the -q so this don't get no stinkin' errors
			fileName = fileName.replace(" -q", "");
		}

		// Check if reading file exists
		File readFile = new File(fileName);
		if (!readFile.exists()) {
			logError(color, "Oops! Your File Wasn't Found!");
			System.exit(1);
		}

		System.out.print(color.CYAN + "SHA Type (1,256,512): " + color.RESET);
		hashType = userInput.nextLine();

		if (hashType.equals("1")) { hexType = "%032x"; }

		System.out.print(color.CYAN + "SHA-" + hashType + " Hash, Or File: " + color.RESET);
		hashString = userInput.nextLine().toLowerCase();

		hashFile = new File(hashString);

		int hashLen = hashString.length();
		if (hashLen != 40 && hashLen != 64 && hashLen != 128) {
			if (hashFile.exists()) {
				// User has entered a file and not a hash
				log(color, "File Mode Selected");
				isFile = true;
			} else {
				logError(color, "Error! File Not Found!");
				System.exit(1);
			}
		}
	}

	public static void back(int times) {
		if(times > 0){
			System.out.print("\b");
			back(times-1);
		}
	}

	public static void progressPercentage(ConsoleColors color, int remain, int total) {
		// Change color to green first
		System.out.print(color.GREEN);
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

	public static String logo(ConsoleColors color) {
		// Logo
		String color1 = color.CYAN;
		String color2 = color.RED;
		String color3 = color.YELLOW;

		String logo = color.RED + "   _____ _    _    "+color1+"      _____                _             \n"
         + color.RED + "  / ____| |  | |   /\\ "+color1+"  / ____|              | |\n"
         + color.RED + " | (___ | |__| |  /  \\ "+color1+"| |     _ __ __ _  ___| | _____ _ __\n"
         + color.RED + "  \\___ \\|  __  | / /\\ \\"+color1+"| |    | '__/ _` |/ __| |/ / _ \\ '__|\n"
         + color.RED + "  ____) | |  | |/ ____ \\"+color1+" |____| | | (_| | (__|   <  __/ |\n"
         + color.RED + " |_____/|_|  |_/_/    \\_\\"+color1+"_____|_|  \\__,_|\\___|_|\\_\\___|_|\n By "+color2+"CodeShady\n";
         
        return logo;
	}

	public static void logError(ConsoleColors color, String message) {
		System.out.println("\n" + color.WHITE + color.RED_BACKGROUND + " " + message + " " + color.RESET + "\n");
	}

	public static void log(ConsoleColors color, String message) {
		System.out.println("\n" + color.BLACK + color.YELLOW_BACKGROUND + " " + message + " " + "\n");
	}

}