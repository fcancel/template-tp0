package ar.fiuba.tdd.template.tp0;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

public class RegExGenerator {
    private int maxLength;
    static final int CODE_OF_MAX_NUMBER_OF_CHAR = 255;

    public RegExGenerator(int maxLength) {

        this.maxLength = maxLength;
    }



    public List<String> generate(String regEx, int numberOfResults) {

        ArrayList<String> listOfStringsWithMatchingRegex = new ArrayList<String>();

        for (int numberOfGeneratedStrings = 0; numberOfGeneratedStrings < numberOfResults; numberOfGeneratedStrings++) {
            listOfStringsWithMatchingRegex.add(generateOneRandomStringFromRegex(regEx));
        }

        return listOfStringsWithMatchingRegex;
    }


    private String generateOneRandomStringFromRegex(String regEx) {
        String result = "";
        StringBuffer buffer = new StringBuffer();
        for(int indexOfRegex = 0; indexOfRegex < regEx.length(); indexOfRegex++) {
            if(isSet(regEx.charAt(indexOfRegex))){
                buffer.append(generatePossibleSet(regEx, indexOfRegex));
            }
        }
        return result;
    }

    private String generatePossibleSet(String regEx, int indexOfRegex) {
        String result;
        int firstOccurenceAt = regEx.indexOf(']',indexOfRegex);
        if (firstOccurenceAt == -1){
            throw new NoSuchElementException();
        }
        String subString = regEx.substring(indexOfRegex + 1, firstOccurenceAt);
        //Now we have to see if it has a quantity modifier
        if (isQuantityModifier(regEx.charAt(firstOccurenceAt + 1))){
            result = generateStringFromQuantity(subString, firstOccurenceAt + 1);
        }
        else {
            result = selectOneRandomCharFromSet(subString);
        }
    }

    private boolean isQuantityModifier(char c) {
        return (c == '?' || c == '+' || c == '*');
    }


    private boolean isSet(char c) {
        return (c == '[');
    }
    /*
    private String generateOneRandomStringFromRegex(String regEx) {
            char currentCharOfRegex;
            StringBuffer buffer = new StringBuffer();
            Random randomGenerator = new Random();
            ArrayList<Character> charsInSet = new ArrayList<Character>();

            for(int indexOfChar = 0; indexOfChar < regEx.length(); indexOfChar++){
                currentCharOfRegex = regEx.charAt(indexOfChar);

                switch (currentCharOfRegex){
                    case '.':   buffer.append(randomChar());
                        break;
                    case '\\':  buffer.append(regEx.charAt(indexOfChar+1));
                                indexOfChar++;
                        break;
                    case '[':   while(regEx.charAt(indexOfChar+1) != ']'){
                                    charsInSet.add(regEx.charAt(indexOfChar+1));
                                    indexOfChar++;
                                }
                        break;
                    case '+':   if(this.maxLength < 10);
                        break;
                    case '?':   if(randomGenerator.nextInt(1) == 1){ //We will generate a randomness to add or not the char
                                    if(indexOfChar > 0){            //So that it will not try to access negative values
                                        buffer.append(regEx.charAt(indexOfChar-1));
                                    }
                                }
                        break;
                    default:    buffer.append(currentCharOfRegex);
                        break;
                }
            }

            String stringToReturn = buffer.toString();
            System.out.println(stringToReturn);
            return stringToReturn;
    }
*/
    private char randomChar() {
        Random randomGenerator = new Random();
        return (char) (randomGenerator.nextInt(CODE_OF_MAX_NUMBER_OF_CHAR));
    }


}