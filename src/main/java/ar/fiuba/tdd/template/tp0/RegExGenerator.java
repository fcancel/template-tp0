package ar.fiuba.tdd.template.tp0;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

public class RegExGenerator {
    private int maxLength;
    static final int CODE_OF_MAX_NUMBER_OF_CHAR = 255;
    private Random randomGenerator = new Random();
    private int indexOfRegex;

    public RegExGenerator(int maxLength) {
        this.maxLength = maxLength;
    }



    public List<String> generate(String regEx, int numberOfResults) {

        ArrayList<String> listOfStringsWithMatchingRegex = new ArrayList<>();

        for (int numberOfGeneratedStrings = 0; numberOfGeneratedStrings < numberOfResults; numberOfGeneratedStrings++) {
            listOfStringsWithMatchingRegex.add(generateOneRandomStringFromRegex(regEx));
        }

        return listOfStringsWithMatchingRegex;
    }


    private String generateOneRandomStringFromRegex(String regEx) {
        StringBuilder buffer = new StringBuilder();
        String currentString;
        for (this.indexOfRegex = 0; this.indexOfRegex < regEx.length(); this.indexOfRegex++) {
            currentString = String.valueOf(regEx.charAt(this.indexOfRegex));
            if (isSet(currentString)) {
                buffer.append(generatePossibleSet(regEx));
                continue;
            }
            if (isLiteral(currentString)) {
                buffer.append(generatePossibleLiteral(regEx));
                continue;
            }
            buffer.append(generateNormalString(regEx, currentString));
        }
        return buffer.toString();
    }

    private Boolean outOfBounds(String regEx, int index) {
        return index >= regEx.length();
    }

    private Boolean isWildcard(String string) {
        return string.equals(".");
    }

    private String generateNormalString(String regEx, String currentChar) {
        if (isWildcard(currentChar)) {
            currentChar = String.valueOf(randomChar());
        }
        return generateStringWithOrWithoutQuantityModifier(currentChar, regEx, this.indexOfRegex + 1);
    }

    private String generatePossibleLiteral(String regEx) {
        this.indexOfRegex += 1;
        if (outOfBounds(regEx,this.indexOfRegex)) {
            throw new ArrayIndexOutOfBoundsException();
        }
        String stringLiteral = String.valueOf(regEx.charAt(this.indexOfRegex));
        return generateStringWithOrWithoutQuantityModifier(stringLiteral, regEx, this.indexOfRegex + 1);
    }

    private boolean isLiteral(String currentString) {
        return (currentString.equals("\\"));
    }

    private String generatePossibleSet(String regEx) {
        String stringToUse;
        int firstOccurrenceOfClosingSquareBracketAt = regEx.indexOf(']',this.indexOfRegex);
        if (firstOccurrenceOfClosingSquareBracketAt == -1) {
            throw new NoSuchElementException();
        }
        stringToUse = regEx.substring(this.indexOfRegex + 1, firstOccurrenceOfClosingSquareBracketAt);
        stringToUse = selectOneRandomCharFromSet(stringToUse);
        stringToUse = generateStringWithOrWithoutQuantityModifier(stringToUse, regEx, firstOccurrenceOfClosingSquareBracketAt + 1);

        this.indexOfRegex = firstOccurrenceOfClosingSquareBracketAt + 1;
        return stringToUse;
    }

    private String generateStringWithOrWithoutQuantityModifier(String subString, String regex, int whereIsTheSpecialModifier) {
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
        StringBuilder returnValue = new StringBuilder();
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


    private boolean isSet(String currentChar) {
        return (currentChar.equals("["));
    }

    private char randomChar() {
        return (char) (this.randomGenerator.nextInt(CODE_OF_MAX_NUMBER_OF_CHAR));
    }
}