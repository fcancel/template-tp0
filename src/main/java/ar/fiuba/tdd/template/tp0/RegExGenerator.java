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

        return new ArrayList<String>() {
            {
                add("a");
                add("b");
                add("c");
            }
        };
    }

    private String generateOneRandomStringFromRegex(String regEx) {
        if(this.maxLength < regEx.length()){
            return "";
        }
        else{
            return regEx;
        }
    }


}