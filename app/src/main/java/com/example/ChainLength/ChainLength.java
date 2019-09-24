package com.example.ChainLength;

public class ChainLength {

    // numberChainringsLargestDiscChainset - Largest Chainset Disc Chainrings Number
    // numberChainringsLargestDiscCassette - Largest Cassette Disc Chainrings Number
    // lengthBottomBracketRearHub - Bottom Bracket To Rear Hub Length [cm]

    private final double constNumber = 0.635;          // Constant number
    private int meanChainrings;                        // meanChainrings = (numberChainringsLargestDiscChainset + numberChainringsLargestDiscCassette) / 2
    private double divisionLength;                     // divisionLength = (int) Math.round(lengthBottomBracketRearHub / constNumber)
    private double sumFunction;                        // sumFunction = meanChainrings + divisionLength + 2
    public int functionCalculated;                     // Chain Link Number: functionCalculated = (int) sumFunction or functionCalculated = (int) sumFunction + 1

    public void calculateChainLength(int numberChainringsLargestDiscChainset,
                                     int numberChainringsLargestDiscCassette,
                                     int lengthBottomBracketRearHub){
        // Temporary functions
        meanChainrings = (numberChainringsLargestDiscChainset + numberChainringsLargestDiscCassette) / 2;
        divisionLength = (int) Math.round(lengthBottomBracketRearHub / constNumber);
        sumFunction = meanChainrings + divisionLength + 2;

        // Result - Chain Link Number
        if (sumFunction % 2 == 0) {
            functionCalculated = (int) sumFunction;
        }
        else {
            functionCalculated = (int) sumFunction + 1;
        }

        this.functionCalculated = functionCalculated;
    }

}
