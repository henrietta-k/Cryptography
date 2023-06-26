import java.util.*;
import sun.tools.jar.resources.jar_pt_BR;
import java.lang.*;

public class Cryptography {
    static int lettersPerGroup;
    static String groupifiedText;
    static int lengthOfText;
    static ArrayList<Integer> spaceIndices = new ArrayList<>();
    static ArrayList<Integer> obIndices = new ArrayList<>();

    static Scanner input = new Scanner(System.in);

    //Asks user for their inputs then gives the encrypted and decrypted string
    public static void main(String[] args) {
        System.out.println("\nWelcome to the Cryptography program!");
        System.out.print("\nPlease input the text that you would like to be crypted: ");
        String text = input.nextLine();

        System.out.print("Please choose a random integer. This will be used in the Ceasarify portion of this program. ");
        int shift = input.nextInt();

        System.out.print("Please choose another random integer. This will be used for the Groupify portion of this program. ");
        lettersPerGroup = input.nextInt();

        String encryptedString = encryptString(text, shift, lettersPerGroup);
        System.out.println("\nHere is the encrypted text: " + encryptedString);

        String decryptedText = decryptString(encryptedString,shift);
        System.out.println("\nHere is the decrypted text: " + decryptedText);

        System.out.println("\nThank you for using the Cryptography Program!");
    }

    public static String normalizeText(String text) {
        String noPunctuation = text.replaceAll("\\p{Punct}", "");
        String noLowerCase = noPunctuation.toUpperCase();

        for (int i=0; i < noLowerCase.length(); i++) {
            if (noLowerCase.charAt(i) == ' ') {
                spaceIndices.add(i);
            }
        }

        String normalizedText = noLowerCase.replaceAll(" ","");

        for (int i=0; i< normalizedText.length() - 1; i++) {
            if (normalizedText.charAt(i) == 'O' && normalizedText.charAt(i+1) == 'B') {
                obIndices.add(i);
            }
        }
        return normalizedText;
    }

    public static String obify(String text) {

        String obifiedText = "";

        //Adds "OB" before any vowel
        for (int i=0; i < text.length(); i++) {
            if (text.charAt(i) == 'A' || text.charAt(i) == 'E' || text.charAt(i) == 'I' || text.charAt(i) == 'O' || text.charAt(i) == 'U' || text.charAt(i) == 'Y') {
                obifiedText += "OB";
                obifiedText += text.charAt(i);
            }
            else {
                obifiedText += text.charAt(i);
            }
        }
        return obifiedText;
    }

    //Adds chars to the caesarifiedText string depending on the shiftValue
    public static String caesarify(String text, int shiftValue) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZX";
        String shiftedLetters = shiftAlphabet(shiftValue);
        String caesarifiedText = "";

        for (int i=0; i < text.length();i++) {
            char letter = text.charAt(i);
            int letterIndex = alphabet.indexOf(String.valueOf(letter));

            caesarifiedText += shiftedLetters.charAt(letterIndex);
        }
        return caesarifiedText;
    }

    public static String shiftAlphabet(int shift) {
        int start = 0;
        if (shift < 0) {
            start = (int) 'Z' + shift + 1;
        } else {
            start = 'A' + shift;
        }

        String result = "";
        char currChar = (char) start;
        for (; currChar <= 'Z'; ++currChar) {
            result = result + currChar;
        }
        if (result.length() < 26) {
            for(currChar = 'A'; result.length() < 26; ++currChar) {
                result = result + currChar;
            }
        }
        return result;
    }

    //Groups the letters in the string
    public static String groupify(String text, int numbersPerGroup) {
        while (text.length() % numbersPerGroup != 0 ) {
            text += "x";
        }
        groupifiedText = "";
        for (int i=0; i < text.length(); i++) {
            groupifiedText += text.charAt(i);
            if ((groupifiedText.substring(0,i+1)).length() % numbersPerGroup== 0 && i >=1) {
                groupifiedText += " ";
            }
        }
        return groupifiedText;
    }

    //Regroups the first groupified method using 2D arrays
    public static String groupifyPartTwo(String text, int lettersPerColumn) {
        String newText= text.trim();
        String [] splitString = newText.trim().split("\\s+");

        int lettersPerRow = lengthOfText / lettersPerColumn;

        char [][] array = new char[splitString.length][];
        for (int i = 0; i < splitString.length; i++) {
            array[i] = splitString[i].toCharArray();
        }

        String regroupifiedText = "";

        for (int i = 0; i < lettersPerColumn; i++) {
            for (int j = 0; j<lettersPerRow; j++) {
                regroupifiedText += array[j][i];
            }
        }
        return regroupifiedText;
    }

    //Converts each letter of the input string into two numbers and adds it to a new string
    public static String polybiusSquareCipher(String text) {
        Map< String, String> squareMap = new HashMap < String,String> ();

        squareMap.put("D","11"); squareMap.put("C","21"); squareMap.put("O","31"); squareMap.put("E","41");
        squareMap.put("A","51"); squareMap.put("B","12"); squareMap.put("F","22"); squareMap.put("G","32");
        squareMap.put("H","42"); squareMap.put("I","52"); squareMap.put("K","13"); squareMap.put("L","23");
        squareMap.put("M","33"); squareMap.put("N","43"); squareMap.put("P","53"); squareMap.put("Q","14");
        squareMap.put("R","24"); squareMap.put("S","34"); squareMap.put("T","44"); squareMap.put("U","54");
        squareMap.put("V","15"); squareMap.put("W","25"); squareMap.put("X","35"); squareMap.put("Y","45");
        squareMap.put("Z","55"); squareMap.put("J","00");

        String polybiusSquareText = "";

        for (int i=0; i < text.length(); i++) {
            char charLetter = text.charAt(i);
            String stringLetter = String.valueOf(charLetter);
            if (squareMap.containsKey(stringLetter)) {
                polybiusSquareText += squareMap.get(stringLetter);
            }
        }
        String trimmedText = polybiusSquareText.trim();
        lengthOfText = trimmedText.length();
        return polybiusSquareText;
    }

    //Puts together the encrypted String
    public static String encryptString(String text, int shiftValue, int codeGroupSize) {

        String normText = normalizeText(text);
        String obiText = obify(normText);
        String caesarifiedText = caesarify(obiText, shiftValue);
        String polybiusText = polybiusSquareCipher(caesarifiedText);
        String groupifiedPartOne = groupify (polybiusText,codeGroupSize);
        String groupifiedPartTwo = groupifyPartTwo(groupifiedPartOne, codeGroupSize);
        String encryptedString = groupify(groupifiedPartTwo, codeGroupSize);
        return encryptedString;
    }

    //Gets rid of spaces in between the string to ungroupify it
    public static String ungroupify(String text) {
        String ungroupifiedText = text.replaceAll(" ","");
        return ungroupifiedText;
    }

    //Gets rid of all OB in the string to deobify it
    public static String deobify(String text) {
        String deobifiedText = text.replaceAll("OB","");
        return deobifiedText;
    }

    //Undoes the groupify pt. two method
    public static String ungroupifyPartTwo(String text, int lettersPerGroup) {
        String newText = text.replaceAll(" ","");
        String trimmedText = newText.trim();
        String groupifiedText = groupify(trimmedText, trimmedText.length()/lettersPerGroup);
        String [] splitString = groupifiedText.trim().split("\\s+");

        int lettersPerColumn = lengthOfText / lettersPerGroup;
        char [][] array = new char[splitString.length][];
        for (int i = 0; i < splitString.length; i++) {
            array[i] = splitString[i].toCharArray();
        }

        String ungroupifiedText = "";

        for (int i = 0; i < lettersPerColumn; i++) {
            for (int j = 0; j<lettersPerGroup; j++) {
                ungroupifiedText += array[j][i];
            }
        }
        return ungroupifiedText;
    }

    //Takes in a String of numbers and returns a string of letters
    public static String unpolybiusCipher(String text) {

    //Keys and values of this map have been switched around
        Map< String, String> squareMap = new HashMap < String,String> ();
        squareMap.put("11","D"); squareMap.put("21","C"); squareMap.put("31","O"); squareMap.put("41","E");
        squareMap.put("51","A"); squareMap.put("12","B"); squareMap.put("22","F"); squareMap.put("32","G");
        squareMap.put("42","H"); squareMap.put("52","I"); squareMap.put("13","K"); squareMap.put("23","L");
        squareMap.put("33","M"); squareMap.put("43","N"); squareMap.put("53","P"); squareMap.put("14","Q");
        squareMap.put("24","R"); squareMap.put("34","S"); squareMap.put("44","T"); squareMap.put("54","U");
        squareMap.put("15","V"); squareMap.put("25","W"); squareMap.put("35","X"); squareMap.put("45","Y");
        squareMap.put("55","Z"); squareMap.put("00","J");


        String newText = text.trim();
        String [] splitString = newText.split("\\s+");

        String unpolybiusSquareText = "";

        for (int i=0; i < splitString.length; i++) {
            String letters = splitString[i];
            if (squareMap.containsKey(letters)) {
                unpolybiusSquareText += squareMap.get(letters);
            }
        }
        return unpolybiusSquareText;
    }

    //Adds the original spaces back to the decrypted text
    public static String addingSpaces(String text) {

        int spaceIndex = 0;
        int i=0;

        StringBuffer strBuf = new StringBuffer(text);

        while (spaceIndex < spaceIndices.size()) {
            if (i == spaceIndices.get(spaceIndex)) {
                strBuf.insert(i, " ");
                spaceIndex++;
            }
            i++;
        }
        return strBuf.toString(); //Returning a String
    }

    //Sometimes be "ob" in the original input text
    public static String addingOB(String text) {
        int obIndex = 0;
        int i=0;

        StringBuffer strBuf = new StringBuffer(text);

        while (obIndex < obIndices.size()) {
            if (i == obIndices.get(obIndex)) {
                strBuf.insert(i,"OB");
                obIndex++;
            }
            i++;
        }
        return strBuf.toString();
    }

    //Puts together the decrypted string
    public static String decryptString(String text, int shift) {
        String ungroupifiedStringPart2 = ungroupifyPartTwo(text, lettersPerGroup);
        String groupifyForPolybius = groupify(ungroupifiedStringPart2, 2);
        String unpolybiusCiphered = unpolybiusCipher(groupifyForPolybius);
        String uncaesarifiedString = caesarify(unpolybiusCiphered, -(shift));
        String deobifiedString = deobify(uncaesarifiedString);
        String addedOB = addingOB(deobifiedString);
        String decryptedString = addingSpaces(addedOB);
        return decryptedString;
    }
    
}
