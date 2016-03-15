package ar.fiuba.tdd.template.tp0;

import java.util.ArrayList;
import java.util.List;

public class RegExGenerator {
    private int maxLength;

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
                if(isCharSpecial(currentCharOfRegex)){
                    //TODO: act accordingly
                }
                else{
                    buffer.append(currentCharOfRegex);
                }
            }
            String stringToReturn = buffer.toString();
            return stringToReturn;
        }
    }

    //TODO: complete functionality
    private boolean isCharSpecial(char currentCharOfRegex) {
        return false;
    }


}