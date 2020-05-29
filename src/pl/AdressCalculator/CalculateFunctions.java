package pl.AdressCalculator;

// ZMIENNE POSIADAJĄ KOŃCÓWKI NAZW BIN I DEC W ZALEŻNOŚCI OD TEGO CZY PRZECHOWUJĄ WARTOŚCI
// BINARNE CZY DZIESIĘTNE

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalculateFunctions {

    // 0. Rozbija informacje o adresie ip i je porządkuje
    // JAKO ARGUMENT PRZYJMUJE ADRES IP NP. 120.43.233.120/32
    void breakAdress(String ipAdress) {
        // A. Rozbijamy adres na 4 oktety i maskę
        String[] ip = splitAdress(ipAdress);
        //lelelelle
        // B. Wykorzystując utworzone metody, obliczamy wyniki i zapisujemy w zmiennych
        String ipAdressBin = makeBin(ip);
        String ipAdressDec = linkAdress(ip);
        String maskBin = makeBinMask(ip[4]);
        String maskDec = makeDecMask(maskBin);
        String networkAdressBin = adressNetwork(ipAdressBin, maskBin);
        String networkAdressDec = makeDecMask(networkAdressBin);
        String shortMask = ip[4];
        String rMaskBin = makeReverseBinMask(ip[4]);
        String classAdress = classAdress(makeDecMask(makeBinMask(ip[4])));
        String broadcastAdress = broadcastAdress(networkAdressDec, rMaskBin);
        String firstHost = firstAdressOfHost(networkAdressDec);
        String lastHost = lastAdressOfHost(broadcastAdress);
        String allHosts = allHosts(ip[4]);

        // C. Przekazujemy obliczone wartości do wyświetlenia na ekranie i wywołujemy
        // metodę checkIfAdressIPisCorrect do sprawdzenia poprawności adresu.
        checkIfIpAdressIsCorrect(ipAdress);
        System.out.println("\n\n---- ALL IP (DEC) DATA ------------------------------" +
                "\nIP ADRESS: " + ipAdressDec +
                "\nSHORT MASK: " + "/" + shortMask +
                "\nFULL MASK: " + maskDec +
                "\nCLASS ADRESS: " + classAdress +
                "\nNETWORK ADRESS: " + networkAdressDec +
                "\nBROADCAST ADRESS: " + broadcastAdress +
                "\nFIRST HOST: " + firstHost +
                "\nLAST HOST: " + lastHost +
                "\nALL HOSTS: " + allHosts +
                "\n\n---- ALL IP (BIN) DATA ------------------------------" +
                "\nIP ADRESS: " + ipAdressBin +
                "\nNETWORK ADRESS: " + networkAdressBin +
                "\nFULL MASK: " + maskBin +
                "\n-----------------------------------------------------\n");

    }

    // 1. Rozbija adress ip na oktety i maskę sieci
    // JAKO ARGUMENT PRZYJMUJE ADRES IP DO ROZBICIA NA OKTETY I MASKE
    private String[] splitAdress(String ipAdress) {
        String[] ip = ipAdress.split("[./]");
        return ip;
    }

    // 2. Łączy rozbity adress ip w jedno (bez skróconej maski)
    // JAKO ARGUMENT PRZYJMUJE TABLICĘ STRINGÓW, KTÓRA ŁĄCZY SIĘ W JEDEN ŁAŃCUCH ZNAKÓW
    private String linkAdress(String[] adress) {
        String linkAdress = adress[0] + "." + adress[1] + "." + adress[2] + "." + adress[3];
        return linkAdress;
    }

    // 3. Łączy rozbity adress ip w jedno (bez skróconej maski)
    // JAKO ARGUMENT PRZYJMUJE TABLICĘ STRINGÓW, KTÓRA ŁĄCZY SIĘ W JEDEN ŁAŃCUCH ZNAKÓW (Zamiana na postać binarną)
    private String makeBin(String[] adress) {
        String linkAdress = decToBin(adress[0]) + "." + decToBin(adress[1]) + "." + decToBin(adress[2]) + "." + decToBin(adress[3]);
        return linkAdress;
    }

    // 4. Zamienia liczby dziesiętne na binarne
    // JAKO ARGUMENT PRZYJMUJE POJEDYNCZY OKTET LICZBY DZIESIĘTNEJ I ZAMIENIA GO NA LICZBĘ BINARNĄ
    private String decToBin(String ipAdress) {
        int dec = Integer.parseInt(ipAdress);
        String bin = Integer.toBinaryString(dec);
        while (bin.length() != 8) {
            bin = "0" + bin;
        }
        return bin;
    }

    // 5. Zamienia pojedyncze liczby binarne na dziesiętne
    // JAKO ARGUMENT PRZYJMUJE POJEDYNCZY OKTET LICZBY BINARNEJ I ZAMIENIA GO NA LICZBĘ DZIESIĘTNĄ
    private int binToDec(String ipAdress) {
        int dec = Integer.parseInt(ipAdress, 2);
        return dec;
    }

    // 6. Tworzy adres maski w oparciu o skróconą maskę np.: /23
    // JAKO ARGUMENT PRZYJMUJE SKRÓCONĄ MASKĘ I NA JEJ PODSTAWIE TWORZY ADRES BINARNY
    private String makeBinMask(String shortMask) {
        String maskBin = "1";
        int parseShortMask = Integer.parseInt(shortMask);
        for (int i = 1; i <= 31; i++) {
            if (i % 8 == 0) {
                maskBin += ".";
            }
            if (i < parseShortMask) {
                maskBin = maskBin + "1";
            } else {
                maskBin = maskBin + "0";
            }
        }
        return maskBin;
    }

    // 6. Tworzy odwrócony adres maski w oparciu o skróconą maskę np.: /23 (Potrzebne do obliczenia adresu rozgłoszeniowgo)
    // JAKO ARGUMENT PRZYJMUJE SKRÓCONĄ MASKĘ I NA JEJ PODSTAWIE TWORZY ADRES BINARNY
    private String makeReverseBinMask(String shortMask) {
        String maskBin = "0";
        int parseShortMask = Integer.parseInt(shortMask);
        for (int i = 1; i <= 31; i++) {
            if (i % 8 == 0) {
                maskBin += ".";
            }
            if (i < parseShortMask) {
                maskBin = maskBin + "0";
            } else {
                maskBin = maskBin + "1";
            }
        }
        return maskBin;
    }

    // 7. Tworzy pełen adres maski w postaci dziesiętnej
    // JAKO ARGUMENT PRZYJMUJE BINARNĄ WARTOSC I NA JEJ PODSTAWIE TWORZY DZIESIĘTNĄ
    private String makeDecMask(String mask) {
        String[] maskSplit = splitAdress((mask));
        String fullDecMask = binToDec(maskSplit[0]) + "." + binToDec(maskSplit[1]) + "." + binToDec(maskSplit[2]) + "." + binToDec(maskSplit[3]);
        return fullDecMask;
    }

    // 8. Wyznacza klasę sieci
    // JAKO ARGUMENT PRZYJMUJE ADRES MASKI W POSTACI DZIESIĘTNEJ I WYZNACZA KLASĘ MASKI
    private String classAdress(String ipAdress) {
        String[] adressSplit = splitAdress(ipAdress);
        String[] adressClasses = {"A", "B", "C", "D"};
        String adressClass = "";
        int[] parseSplit = new int[4];
        for (int i = 0; i < 4; i++) {
            parseSplit[i] = Integer.parseInt(adressSplit[i]);
            if (parseSplit[i] == 255) {
                adressClass = adressClasses[i];
            }
        }
        return adressClass;
    }

    // 9. Wyznacza adres sieciowy
    // JAKO ARGUMENT PRZYJMUJE ADRES IP BIN i ADRES MASKI BIN
    private String adressNetwork(String ipBin, String maskBin) {
        String[] octetsIp = splitAdress(ipBin);
        String[] octetsMask = splitAdress(maskBin);
        String networkAdress = "";
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 8; j++) {
                if (octetsIp[i].charAt(j) != '0' && octetsIp[i].charAt(j) == octetsMask[i].charAt(j)) {
                    networkAdress = networkAdress + "1";
                } else {
                    networkAdress = networkAdress + "0";
                }
            }
            if (i < 3) {
                networkAdress += ".";
            }
        }
        return networkAdress;
    }

    // 10. Wyznacza adres rozgłoszeniowy
    // JAKO ARGUMENT PRZYJMUJE ADRES SIECIOWY i ADRES ODWROCONEJ MASKI BIN
    private String broadcastAdress(String adressNetwork, String reverseMaskBin) {
        String[] reverseMask = splitAdress(makeDecMask(reverseMaskBin));
        String[] adressNet = splitAdress(adressNetwork);
        int[] split = new int[4];
        int[] adressSplit = new int[4];
        for (int i = 0; i < 4; i++) {
            split[i] = Integer.parseInt(reverseMask[i]);
            adressSplit[i] = Integer.parseInt(adressNet[i]);
            split[i] += adressSplit[i];
            adressNet[i] = Integer.toString(split[i]);
        }
        String broadcastAdress = adressNet[0] + "." + adressNet[1] + "." + adressNet[2] + "." + adressNet[3];
        return broadcastAdress;
    }

    // 11. Wyznacza pierwszego hosta
    // JAKO ARGUMENT PRZYJMUJE ADRES SIECIOWY
    private String firstAdressOfHost(String networkAdress) {
        String[] netAdress = splitAdress(networkAdress);
        String firstHost;
        int[] splitNet = new int[2];
        splitNet[1] = Integer.parseInt(netAdress[3]);
        splitNet[1] += 1;
        firstHost = Integer.toString(splitNet[1]);
        firstHost = netAdress[0] + "." + netAdress[1] + "." + netAdress[2] + "." + firstHost;
        return firstHost;
    }

    // 12. Wyznacza ostatniego hosta
    // JAKO ARGUMENT PRZYJMUJE ADRES ROZGŁOSZENIOWY
    private String lastAdressOfHost(String broadcastAdress) {
        String[] netAdress = splitAdress(broadcastAdress);
        String lastHost;
        int[] splitNet = new int[2];
        splitNet[1] = Integer.parseInt(netAdress[3]);
        splitNet[1] -= 1;
        lastHost = Integer.toString(splitNet[1]);
        lastHost = netAdress[0] + "." + netAdress[1] + "." + netAdress[2] + "." + lastHost;
        return lastHost;
    }

    // 13. Wyznacza LICZBĘ WSZYSTKICH HOSTÓW
    // JAKO ARGUMENT PRZYJMUJE SKRÓCONĄ MASKĘ
    private String allHosts(String shortMask) {
        int sMask = Integer.parseInt(shortMask);
        double allhosts = Math.pow(2, 32 - sMask);
        sMask = (int) allhosts - 2;
        String hosts = Integer.toString(sMask);
        return hosts;
    }

    // 14. Sprawdza poprawność wprowadzonego adresu
    // JAKO ARGUMENT PRZYJMUJE DOWOLNY STRING WPROWADZONY Z KLAWIATURY. O JEGO PRAWIDŁOWOŚCI MÓWI WYRAŻENIE REGULARNE,
    // KTÓRE SPRAWDZA POPRAWNOŚĆ WPROWADZONYCH DANYCH.
    private void checkIfIpAdressIsCorrect(String ipAdress) {
        Pattern compiledPattern = Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\/([1-9]|[1-2][0-9]|3[0-2])$");
        Matcher matcher = compiledPattern.matcher(ipAdress);
        System.out.println("\n---- IS THE IP ADRESS VALID? ------------------------");
        if (matcher.matches() == true) {
            System.out.print("IP ADRESS: " + ipAdress + " is CORRECT");
        } else {
            System.out.print("IP ADRESS: " + ipAdress + " is INCORRECT!\n");
        }
    }
}