package ar.fiuba.tdd.template.tp0;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RegExGenerator {
    private int maxLength;
    static final int CODE_OF_MAX_NUMBER_OF_CHAR = 255;

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
            Random randomGenerator = new Random();
            for(int indexOfChar = 0; indexOfChar < regEx.length(); indexOfChar++){
                currentCharOfRegex = regEx.charAt(indexOfChar);

                switch (currentCharOfRegex){
                    case '.':   buffer.append(randomChar());
                                break;
                    case '\\':  buffer.append(regEx.charAt(indexOfChar+1));
                                indexOfChar++;
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
    }

    private char randomChar(){

        Random randomGenerator = new Random();
        return (char) (randomGenerator.nextInt(CODE_OF_MAX_NUMBER_OF_CHAR));
    }


}