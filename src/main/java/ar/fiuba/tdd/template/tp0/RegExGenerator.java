package ar.fiuba.tdd.template.tp0;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

public class RegExGenerator {
    private int maxLength;
    static final int CODE_OF_MAX_NUMBER_OF_CHAR = 255;
    private Random randomGenerator;
    private int indexOfRegex;

    public RegExGenerator(int maxLength) {
        this.maxLength = maxLength;
        Random randomGenerator = new Random();
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
        String currentChar;
        for(this.indexOfRegex = 0; this.indexOfRegex < regEx.length(); this.indexOfRegex++) {
            currentChar = String.valueOf(regEx.charAt(this.indexOfRegex));
            if(isSet(currentChar)) {
                buffer.append(generatePossibleSet(regEx));
                continue;
            }
            if(isLiteral(regEx.charAt(this.indexOfRegex))) {                ;
                buffer.append(generatePossibleLiteral(regEx));
                continue;
            }
            buffer.append(generateNormalChar(regEx, currentChar));
            continue;
        }
        result = buffer.toString();
        return result;
    }

    private String generateNormalChar(String regEx, String currentChar) {
        if (this.indexOfRegex + 1 < regEx.length()){
            String stringWithQuantity = generateStringSetFromQuantity(currentChar, regEx, this.indexOfRegex + 1);
            return stringWithQuantity;
        }
        else return currentChar;
    }

    private String generatePossibleLiteral(String regEx) {
        this.indexOfRegex += 2;
        String stringLiteral = String.valueOf(regEx.charAt(this.indexOfRegex));
        String stringWithQuantity = generateStringSetFromQuantity(stringLiteral, regEx, this.indexOfRegex + 1);
        return "@";
    }

    private boolean isLiteral(char currentChar) {
        return (currentChar == '\\');
    }

    private String generatePossibleSet(String regEx) {
        String result;
        int firstOccurenceAt = regEx.indexOf(']',this.indexOfRegex);
        if (firstOccurenceAt == -1){
            throw new NoSuchElementException();
        }
        String subString = regEx.substring(this.indexOfRegex + 1, firstOccurenceAt);
        //Now we have to see if it has a quantity modifier
        if (isQuantityModifier(regEx.charAt(firstOccurenceAt + 1))){
            result = generateStringSetFromQuantity(subString, regEx, firstOccurenceAt + 1);
        }
        else {
            result = selectOneRandomCharFromSet(subString);
        }
        this.indexOfRegex = firstOccurenceAt;
        return result;
    }

    private String generateStringSetFromQuantity(String subString, String regex, int whereIsTheSpecialModifier) {
        switch(regex.charAt(whereIsTheSpecialModifier)){
            case '+':   return generateStringSetPlus(subString);
            case '*':   return generateStringSetAsterisk(subString);
            case '?':   return generateStringSetQuestionMark(subString);
            default:    return subString;
        }
    }

    private String generateStringSetAsterisk(String subString) {
        return randomCharsFromSet(subString, 0, this.maxLength);
    }

    private String generateStringSetPlus(String subString) {
        return randomCharsFromSet(subString, 1, this.maxLength);
    }

    private String generateStringSetQuestionMark(String subString) {
        return randomCharsFromSet(subString, 0, 1);
    }

    private String randomCharsFromSet (String subString, int minNumberOfTimes, int maxNumberOfTimes){
        StringBuffer returnValue = new StringBuffer();
        String stringToAdd = "";
        int howManyTimes = this.randomGenerator.nextInt(maxNumberOfTimes);
        while (howManyTimes<maxNumberOfTimes + minNumberOfTimes){
            stringToAdd = String.valueOf(subString.charAt(this.randomGenerator.nextInt(subString.length())));
            returnValue.append(stringToAdd);
        }
        return returnValue.toString();
    }

    private String selectOneRandomCharFromSet(String subString) {
        int totalOfCharsInSet = subString.length();
        int indexOfRandomChar = this.randomGenerator.nextInt(totalOfCharsInSet);
        return String.valueOf(subString.charAt(indexOfRandomChar));
    }

    private boolean isQuantityModifier(char c) {
        return (c == '?' || c == '+' || c == '*');
    }


    private boolean isSet(String currentChar) {
        return (currentChar == "[");
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