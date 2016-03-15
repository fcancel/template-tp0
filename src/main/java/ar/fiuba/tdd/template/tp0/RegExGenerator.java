package ar.fiuba.tdd.template.tp0;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RegExGenerator {
    private int maxLength;
    static final int CODE_OF_MAX_NUMBER_OF_CHAR = 256;

    public RegExGenerator(int maxLength) {

        this.maxLength = maxLength;
    }



    public List<String> generate(String regEx, int numberOfResults) {

        ArrayList<String> listOfStringsWithMatchingRegex = new ArrayList<String>();

        for (int numberOfGeneratedStrings = 0; numberOfGeneratedStrings < numberOfResults; numberOfGeneratedStrings++){
            listOfStringsWithMatchingRegex.add(generateOneRandomStringFromRegex(regEx));
        }

        return listOfStringsWithMatchingRegex;
    }


    private String generateOneRandomStringFromRegex(String regEx) {
        if(this.maxLength < regEx.length()){
            return "";
        }
        else{
            char currentCharOfRegex;
            StringBuffer buffer = new StringBuffer();

            for(int indexOfChar = 0; indexOfChar < regEx.length(); indexOfChar++){
                currentCharOfRegex = regEx.charAt(indexOfChar);

                buffer.append( appendAccordingToCharInRegex(currentCharOfRegex) );

            }
            String stringToReturn = buffer.toString();
            System.out.println(stringToReturn);
            return stringToReturn;
        }
    }

    //TODO: complete functionality
    private String appendAccordingToCharInRegex(char currentCharOfRegex) {
       String stringToReturn;

        switch (currentCharOfRegex){
            case '.':   stringToReturn = String.valueOf(randomChar());
                    break;
            default:    stringToReturn = String.valueOf(currentCharOfRegex);
                    break;
        }
        return stringToReturn;
    }

    private char randomChar(){

        Random randomGenerator = new Random();
        return (char) (randomGenerator.nextInt(CODE_OF_MAX_NUMBER_OF_CHAR));
    }


}