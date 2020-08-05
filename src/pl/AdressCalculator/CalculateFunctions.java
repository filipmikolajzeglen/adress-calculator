package pl.AdressCalculator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalculateFunctions {
    
    // 0. Breaks down information about an ip address and arranges it
    // AS AN ARGUMENT IT IS ADDRESSED E.G. 120.43.233.120/32
    void breakAdress(String ipAdress) {      
        // A. We break the address into 4 octets and a mask
        String[] ip = splitAdress(ipAdress);       
        // B. Using the created methods, calculate the results and save them in variables
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

        // C. Transfer the calculated values to display on the screen and execute
        // checkIfAdressIPisCorrect method to check if the address is correct.
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

    // 1. Breaks down the ip address into octets and netmask
    // AS AN ARGUMENT TAKES THE IP ADDRESS TO BE BREAKDOWN INTO OCTETS AND MASKE
    private String[] splitAdress(String ipAdress) {
        String[] ip = ipAdress.split("[./]");
        return ip;
    }
    
    // 2. Combine broken ip address into one (no shortened mask)
    // AS AN ARGUMENT HAS ADOPTED AN ARRAY OF STRINGS THAT COMBINES INTO ONE CHAIN OF CHARACTERS
    private String linkAdress(String[] adress) {
        String linkAdress = adress[0] + "." + adress[1] + "." + adress[2] + "." + adress[3];
        return linkAdress;
    }

    // 3. Concatenate the broken ip address into one (no shortened mask)
    // AS AN ARGUMENT HAS ADOPTED AN ARRAY OF STRINGS THAT COMBINES INTO ONE CHARACTER (Converted to binary)
    private String makeBin(String[] adress) {
        String linkAdress = decToBin(adress[0]) + "." + decToBin(adress[1]) + "." + decToBin(adress[2]) + "." + decToBin(adress[3]);
        return linkAdress;
    }

    // 4. Convert decimal numbers to binary
    // AS AN ARGUMENT TAKES A SINGLE DECIMAL OCTET AND CONVERSES IT TO A BINARY
    private String decToBin(String ipAdress) {
        int dec = Integer.parseInt(ipAdress);
        String bin = Integer.toBinaryString(dec);
        while (bin.length() != 8) {
            bin = "0" + bin;
        }
        return bin;
    }

    // 5. Convert single binary numbers to decimals
    // AS AN ARGUMENT TAKES A SINGLE OCTET OF A BINARY NUMBER AND CONVERSES IT TO A DECIMAL
    private int binToDec(String ipAdress) {
        int dec = Integer.parseInt(ipAdress, 2);
        return dec;
    }

    // 6.1. Builds the address of the mask based on the truncated mask, eg: / 23
    // AS AN ARGUMENT TAKES A SHORT MASK AND CREATES A BINARY ADDRESS ON THE BASIS OF THIS MASK
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

    // 6.2. Creates an inverted mask address based on a short mask, e.g .: / 23 (Needed to compute the broadcast address)
    // AS AN ARGUMENT TAKES A SHORT MASK AND CREATES A BINARY ADDRESS ON THE BASIS OF THIS MASK
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

    // 7. Builds full mask address as decimal
    // AS AN ARGUMENT TAKES A BINARY VALUE AND ON IT'S BASIS FOR A DECIMAL
    private String makeDecMask(String mask) {
        String[] maskSplit = splitAdress((mask));
        String fullDecMask = binToDec(maskSplit[0]) + "." + binToDec(maskSplit[1]) + "." + binToDec(maskSplit[2]) + "." + binToDec(maskSplit[3]);
        return fullDecMask;
    }


    // 8. Determine the network class
    // AS AN ARGUMENT, IT TAKES A DECIMAL MASK ADDRESS AND DETERMINES THE CLASS OF THE MASK
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

    // 9. Designate the network address
    // AS AN ARGUMENT IT IS ADDRESS BIN IP ADDRESS AND MASK ADDRESS BIN
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
    
    // 10. Designate a broadcast address
    // AS AN ARGUMENT TAKES THE NETWORK ADDRESS AND ADDRESS OF REVERSE BIN MASK
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

    // 11. Designate the first host
    // TAKE A NETWORK ADDRESS AS AN ARGUMENT
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

    // 12. Designate the last host
    // AS AN ARGUMENT TAKES THE PUBLIC ADDRESS
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

    // 13. Designates the NUMBER OF ALL HOSTS
    // AS AN ARGUMENT IS ADOPTED THE SHORT MASK
    private String allHosts(String shortMask) {
        int sMask = Integer.parseInt(shortMask);
        double allhosts = Math.pow(2, 32 - sMask);
        sMask = (int) allhosts - 2;
        String hosts = Integer.toString(sMask);
        return hosts;
    }

    // 14. Checks the correctness of the entered address
    // AS AN ARGUMENT TAKES ANY STRING ENTERED FROM THE KEYPAD. ITS REGULAR EXPRESSION IS ABOUT ITS CORRECTNESS,
    // WHICH CHECKS THE CORRECTNESS OF ENTERED DATA.
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
