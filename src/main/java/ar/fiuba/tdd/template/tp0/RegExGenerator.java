package ar.fiuba.tdd.template.tp0;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.function.BooleanSupplier;

public class RegExGenerator {
    private int maxLength;
    static final int CODE_OF_MAX_NUMBER_OF_CHAR = 255;
    private Random randomGenerator = new Random();
    private int indexOfRegex;

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
        String currentChar;
        for (this.indexOfRegex = 0; this.indexOfRegex < regEx.length(); this.indexOfRegex++) {
            currentChar = String.valueOf(regEx.charAt(this.indexOfRegex));
            if (isSet(currentChar)) {
                buffer.append(generatePossibleSet(regEx));
                continue;
            }
            if (isLiteral(regEx.charAt(this.indexOfRegex))) {
                buffer.append(generatePossibleLiteral(regEx));
                continue;
            }
            buffer.append(generateNormalChar(regEx, currentChar));
            continue;
        }
        result = buffer.toString();
        System.out.println("Me dio: " + result);
        return result;
    }

    private Boolean outOfBounds(String regEx, int index) {
        return index >= regEx.length();
    }

    private String generateNormalChar(String regEx, String currentChar) {
        if (currentChar.equals(".")) {
            currentChar = String.valueOf(randomChar());
        }
        if (this.indexOfRegex + 1 < regEx.length()) {
            String stringWithQuantity = generateStringSetFromQuantity(currentChar, regEx, this.indexOfRegex + 1);
            return stringWithQuantity;
        } else {
            return currentChar;
        }
    }

    private String generatePossibleLiteral(String regEx) {
        this.indexOfRegex += 1;
        if (outOfBounds(regEx,this.indexOfRegex)) {
            throw new ArrayIndexOutOfBoundsException();
        }
        String stringLiteral = String.valueOf(regEx.charAt(this.indexOfRegex));
        if (outOfBounds(regEx, this.indexOfRegex + 1)) {
            return stringLiteral;
        } else {
            String stringWithQuantity = generateStringSetFromQuantity(stringLiteral, regEx, this.indexOfRegex + 1);
            return stringWithQuantity;
        }
    }

    private boolean isLiteral(char currentChar) {
        return (currentChar == '\\');
    }

    private String generatePossibleSet(String regEx) {
        String result;
        int firstOccurenceAt = regEx.indexOf(']',this.indexOfRegex);
        if (firstOccurenceAt == -1) {
            throw new NoSuchElementException();
        }
        String subString = regEx.substring(this.indexOfRegex + 1, firstOccurenceAt);
        //Now we have to see if it has a quantity modifier
        if (isQuantityModifier(regEx.charAt(firstOccurenceAt + 1))) {
            result = generateStringSetFromQuantity(subString, regEx, firstOccurenceAt + 1);
        } else {
            result = selectOneRandomCharFromSet(subString);
        }
        this.indexOfRegex = firstOccurenceAt;
        return result;
    }

    private String generateStringSetFromQuantity(String subString, String regex, int whereIsTheSpecialModifier) {
        if (outOfBounds(regex, whereIsTheSpecialModifier)) {
            return subString;
        }
        switch (regex.charAt(whereIsTheSpecialModifier)) {
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

    private String randomCharsFromSet(String subString, int minNumberOfTimes, int maxNumberOfTimes) {
        this.indexOfRegex += 1;
        StringBuffer returnValue = new StringBuffer();
        int howManyTimes = this.randomGenerator.nextInt(maxNumberOfTimes);
        while (howManyTimes < (maxNumberOfTimes + minNumberOfTimes)) {
            returnValue.append(subString);
            howManyTimes++;
        }
        return returnValue.toString();

    }

    private String selectOneRandomCharFromSet(String subString) {
        int totalOfCharsInSet = subString.length();
        int indexOfRandomChar = this.randomGenerator.nextInt(totalOfCharsInSet);
        return String.valueOf(subString.charAt(indexOfRandomChar));
    }

    private boolean isQuantityModifier(char character) {
        return (character == '?' || character == '+' || character == '*');
    }


    private boolean isSet(String currentChar) {
        return (currentChar.equals("["));
    }

    private char randomChar() {
        Random randomGenerator = new Random();
        return (char) (randomGenerator.nextInt(CODE_OF_MAX_NUMBER_OF_CHAR));
    }
}