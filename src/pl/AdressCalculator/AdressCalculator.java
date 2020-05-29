package pl.AdressCalculator;

import java.util.Scanner;

public class AdressCalculator {

    public static void main(String[] args){
        System.out.print("Enter IP Adress with short version of mask. For example: 190.230.32.120/23\n");
        // Tworzymy obiekt scanner typu scanner do wprowadzania danych
        Scanner scanner = new Scanner(System.in);
        // Tworzymy obiekt split typu CalculateFunctions do obliczania informacji o wprowadzonym adresie
        CalculateFunctions split = new CalculateFunctions();
        String ipAdress = scanner.next();
        split.breakAdress(ipAdress);
    }
}