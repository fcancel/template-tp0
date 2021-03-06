package ar.fiuba.tdd.template.tp0;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class RegExGenerator {
    private int maxLength;
    private static final int CODE_OF_MAX_NUMBER_OF_CHAR = 255;
    private Random randomGenerator = new Random();
    private int indexOfRegex;
    private String regEx;
    private List<Character> listOfSpecialChars;

    RegExGenerator(int maxLength) {
        this.maxLength = maxLength;
        this.listOfSpecialChars = new ArrayList<>();
        this.listOfSpecialChars.add('+');
        this.listOfSpecialChars.add('*');
        this.listOfSpecialChars.add('?');
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
        int firstOccurrenceOfClosingSquareBracketAt = getClosingBracketIndex(indexGetCurrent(), regEx);

        stringToUse = regEx.substring(indexGetCurrent() + 1, firstOccurrenceOfClosingSquareBracketAt);
        checkForSetExceptions(stringToUse);

        stringToUse = stringToUse.replace("\\", "");
        stringToUse = selectOneRandomCharFromSet(stringToUse);
        stringToUse = generateStringWithOrWithoutQuantityModifier(stringToUse, regEx, firstOccurrenceOfClosingSquareBracketAt + 1);

        indexSet(firstOccurrenceOfClosingSquareBracketAt + 1);
        return stringToUse;
    }

    private void checkForSetExceptions(String stringToUse) {
        if (stringToUse.equals("")) {
            throw new EmptySetException();
        }
        if (setHasSpecialCharactaresWithoutLiteral(stringToUse)) {
            throw new InvalidSetException();
        }
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
        if (subString.equals("")) {
            throw new EmptySetException();
        }
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
        boolean isValid;
        char randomChar;
        do {
            randomChar = (char) (randomNextInt(CODE_OF_MAX_NUMBER_OF_CHAR));
            Pattern pattern = Pattern.compile(".");
            Matcher matcher = pattern.matcher(String.valueOf(randomChar));
            isValid = matcher.matches();
        } while (!isValid);
        return randomChar;
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

    private boolean isSpecialChar(char value) {
        return this.listOfSpecialChars.contains(value);
    }

    private boolean setHasSpecialCharactaresWithoutLiteral(String substringFromSet) {
        boolean previousIsLiteral = false;
        char currentChar;
        for (int index = 0; index < substringFromSet.length(); index++) {
            currentChar = substringFromSet.charAt(index);
            if (isSpecialChar(substringFromSet.charAt(index)) && !previousIsLiteral) {
                return true;
            }
            if (currentChar == '\\') {
                previousIsLiteral = true;
            }
        }
        return false;
    }

    private int getClosingBracketIndex(int index, String regEx) {
        boolean searchingForEndBracket = true;
        int indexBracket;
        do {
            indexBracket = regEx.indexOf(']',index + 1);
            if (indexBracket == -1) {
                throw new NoSuchElementException();
            }
            if (indexBracket > 0 && (regEx.charAt(indexBracket - 1) == '\\')) {
                index = indexBracket;
            } else {
                searchingForEndBracket = false;
            }
        } while (searchingForEndBracket);
        return indexBracket;
    }
}