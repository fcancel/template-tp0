package ar.fiuba.tdd.template.tp0;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

class RegExGenerator {
    private int maxLength;
    private static final int CODE_OF_MAX_NUMBER_OF_CHAR = 255;
    private Random randomGenerator = new Random();
    private int indexOfRegex;
    private String regEx;

    RegExGenerator(int maxLength) {
        this.maxLength = maxLength;
    }



    List<String> generate(String regEx, int numberOfResults) {
        ArrayList<String> listOfStringsWithMatchingRegex = new ArrayList<>();

        setRegex(regEx);
        for (int numberOfGeneratedStrings = 0; numberOfGeneratedStrings < numberOfResults; numberOfGeneratedStrings++) {
            listOfStringsWithMatchingRegex.add(generateOneRandomStringFromRegex(regEx));
        }

        return listOfStringsWithMatchingRegex;
    }


    private String generateOneRandomStringFromRegex(String regEx) {
        StringBuilder buffer = new StringBuilder();
        for (indexReset(); indexGetCurrent() < regEx.length(); indexAdvance()) {
            buffer.append(regExStringFromCurrentIndex());
        }
        return buffer.toString();
    }

    private String regExStringFromCurrentIndex() {
        String currentString = String.valueOf(getRegex().charAt(indexGetCurrent()));
        if (isSet(currentString)) {
            return generatePossibleSet(getRegex());
        }
        if (isLiteral(currentString)) {
            return generatePossibleLiteral(getRegex());
        }
        return generateNormalString(getRegex(), currentString);
    }

    private String generateNormalString(String regEx, String currentChar) {
        if (isWildcard(currentChar)) {
            currentChar = String.valueOf(randomChar());
        }
        return generateStringWithOrWithoutQuantityModifier(currentChar, regEx, indexGetCurrent() + 1);
    }

    private String generatePossibleLiteral(String regEx) {
        indexAdvance();
        if (outOfBounds(regEx,indexGetCurrent())) {
            throw new ArrayIndexOutOfBoundsException();
        }
        String stringLiteral = String.valueOf(regEx.charAt(indexGetCurrent()));
        return generateStringWithOrWithoutQuantityModifier(stringLiteral, regEx, indexGetCurrent() + 1);
    }

    private boolean isLiteral(String currentString) {
        return (currentString.equals("\\"));
    }

    private String generatePossibleSet(String regEx) {
        String stringToUse;
        int firstOccurrenceOfClosingSquareBracketAt = regEx.indexOf(']',indexGetCurrent());
        if (firstOccurrenceOfClosingSquareBracketAt == -1) {
            throw new NoSuchElementException();
        }
        stringToUse = regEx.substring(indexGetCurrent() + 1, firstOccurrenceOfClosingSquareBracketAt);
        stringToUse = stringToUse.replace("\\", "");
        stringToUse = selectOneRandomCharFromSet(stringToUse);
        stringToUse = generateStringWithOrWithoutQuantityModifier(stringToUse, regEx, firstOccurrenceOfClosingSquareBracketAt + 1);

        indexSet(firstOccurrenceOfClosingSquareBracketAt + 1);
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
        return randomCharsFromSet(subString, 0, getMaxLength());
    }

    private String generateStringSetPlus(String subString) {
        return randomCharsFromSet(subString, 1, getMaxLength());
    }

    private String generateStringSetQuestionMark(String subString) {
        return randomCharsFromSet(subString, 0, 1);
    }

    private String randomCharsFromSet(String subString, int minNumberOfTimes, int maxNumberOfTimes) {
        indexAdvance();
        StringBuilder returnValue = new StringBuilder();
        int howManyTimes = randomNextInt(maxNumberOfTimes);
        while (howManyTimes < (maxNumberOfTimes + minNumberOfTimes)) {
            returnValue.append(subString);
            howManyTimes++;
        }
        return returnValue.toString();

    }

    private String selectOneRandomCharFromSet(String subString) {
        int totalOfCharsInSet = subString.length();
        int indexOfRandomChar = randomNextInt(totalOfCharsInSet);
        return String.valueOf(subString.charAt(indexOfRandomChar));
    }

    private Boolean outOfBounds(String regEx, int index) {
        return index >= regEx.length();
    }

    private Boolean isWildcard(String string) {
        return string.equals(".");
    }

    private boolean isSet(String currentChar) {
        return (currentChar.equals("["));
    }

    private char randomChar() {
        return (char) (randomNextInt(CODE_OF_MAX_NUMBER_OF_CHAR));
    }

    private void indexAdvance() {
        this.indexOfRegex++;
    }

    private void indexReset() {
        this.indexOfRegex = 0;
    }

    private int indexGetCurrent() {
        return this.indexOfRegex;
    }

    private void indexSet(int value) {
        this.indexOfRegex = value;
    }

    private void setRegex(String regex) {
        this.regEx = regex;
    }

    private String getRegex() {
        return this.regEx;
    }

    private int getMaxLength() {
        return this.maxLength;
    }

    private int randomNextInt(int max) {
        return this.randomGenerator.nextInt(max);
    }
}