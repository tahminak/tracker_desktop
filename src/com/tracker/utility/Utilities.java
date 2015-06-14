package com.tracker.utility;

/**
 *
 * @author Tahmina Khan
 */
/*

  Helper class to extract CTN,CX Info, IMEI etc.


*/

public class Utilities {

    //Extract Customer Name from the notest
    public static String searchCustomerName(String noteText) {
        String customername = "";

        if (noteText.equals("")) {
            return "";
        } else {

            int customernamefirstindex = noteText.lastIndexOf("Customer Name/Nom du Client:");

            if (customernamefirstindex > -1) {

                customername = noteText.substring(customernamefirstindex + 28);
                String[] s = customername.split("\n");

                return s[0];

            }

            customernamefirstindex = noteText.lastIndexOf("Client Name:");

            if (customernamefirstindex > -1) {

                customername = noteText.substring(customernamefirstindex + 12);
                String[] s = customername.split("\n");

                return s[0];

            }

            return "";
        }

    }

    //get the CTN-(Customer Telephone Number ) from the notes
    public static String searchCtn(String noteText) {
        String ctn = "";
        String formatedPhonenumber = "";
        if (noteText.equals("")) {
            return "";
        } else {

            int ctntindex = noteText.lastIndexOf("PhoneNumber:");

            if (ctntindex > -1) {

                ctn = noteText.substring(ctntindex + 13);
                String[] s = ctn.split("\n");
                formatedPhonenumber = s[0].replaceAll("[^0-9]", "");
                return formatedPhonenumber;
            }

            ctntindex = noteText.lastIndexOf("CTN/Phone:");

            if (ctntindex > -1) {

                ctn = noteText.substring(ctntindex + 12);
                String[] s = ctn.split("\n");
                formatedPhonenumber = s[0].replaceAll("[^0-9]", "");
                return formatedPhonenumber;

            }

            return "";
        }
    }

    //Extract IMEI number
    public static String searchImei(String noteText) {

        String imei = "";

        if (noteText.equals("")) {
            return "";
        } else {
            int imeiindex = noteText.lastIndexOf("IMEI:");

            if (imeiindex > -1) {
                imei = noteText.substring(imeiindex + 6);
                String[] s = imei.split("\n");

                return s[0];

            }
            return "";
        }
    }

    public static String fomateVoiceMailRetrivalNumber(String text) {

        String[] result = text.split("-");

        String resultedNumber = "";

        for (int x = 0; x < result.length; x++) {
            resultedNumber += result[x];
        }

        return resultedNumber;

    }

    //Extract APNs.

    public static String searchAPNs(String text) {
        String APNs = "";

        if (text.equals("")) {
            return "";
        } else {
            int APNsindex = text.lastIndexOf("APNs:");

            if (APNsindex > -1) {

                APNs = text.substring(APNsindex + 5);
                String[] s = APNs.split("\n");

                return s[0].replaceAll("\n", "");

            }
            return "";
        }
    }

    //Valifate IMEI from the notes
    public static String validateIMEI(String IMEI) {

        String validIMei = "";

        int sum = 0;

        if (IMEI.length() < 16) {
            return IMEI;
        } else {
            boolean errorflag = false;
            int[] imeiArray = new int[15];

            String[] charArray = IMEI.split("");

            for (int i = 0; i < 14; i++) {

                //getting ascii value for each character
                imeiArray[i] = Integer.parseInt(charArray[i + 1]);
                validIMei += imeiArray[i] + "";

                if (i % 2 != 0) {
                    imeiArray[i] = imeiArray[i] * 2;
                }

                while (imeiArray[i] > 9) {
                    imeiArray[i] = (imeiArray[i] % 10) + (imeiArray[i] / 10);
                }

            }

            int totalValue = 0;
            for (int j = 0; j < 14; j++) {
                totalValue += imeiArray[j];
           
            }
     
            if (0 == totalValue % 10) {
                imeiArray[14] = 0;
            } else {

                imeiArray[14] = (10 - (totalValue % 10));
            }

           
            return validIMei + imeiArray[14];
        }

    }

    public static String formateDeviceInformations(String text) {

        String deviceInformations = "";

        deviceInformations = "\nDevice:" + searchDevice(text) + "\nDevice Type:" + checkDeviceType(text) + "\nIMEI: " + validateIMEI(Utilities.searchImei(text)) + "\nIMSI:" + searchIMSI(text) + "\nAPNs:" + searchAPNs(text) + "\n---------------------------------------------------------------";
        return deviceInformations;
    }

    public static String searchIMSI(String text) {

        String IMSI = "";

        if (text.equals("")) {
            return "";
        } else {
            int IMSIindex = text.lastIndexOf("IMSI:");

            if (IMSIindex > -1) {
                IMSI = text.substring(IMSIindex + 5);
                String[] s = IMSI.split("\n");

                return s[0];

            }
            return "";
        }

    }

    public static String searchDevice(String text) {

        String device = "";

        if (text.equals("")) {
            return "";
        } else {
            int deviceindex = text.lastIndexOf("Device:");

            if (deviceindex > -1) {
                device = text.substring(deviceindex + 7);
                String[] s = device.split("\n");

                return s[0];

            }
            return "";
        }

    }

    public static String checkDeviceType(String text) {

        String devicetype = "";

        if (text.equals("")) {
            return "";
        } else {
            int devicetypeindex = text.lastIndexOf("Warning:");

          
            if (devicetypeindex > -1) {
                devicetype = text.substring(devicetypeindex + 8);
                String[] s = devicetype.split("\n");
                return " **** NON Rogers Device ******";
              

            }
            return "";
        }
    }

    public static String formateCustomerInformations(String text) {

        String customerInfromations = "Wireless Live Chat\n";

        customerInfromations = customerInfromations + "Session ID:" + searchSessionID(text) + "\n" + "Client Name: " + Utilities.searchCustomerName(text)
                + "\nPhoneNumber: " + searchCtn(text) + "\nPostal Code:" + searchPostalCode(text) + "\nChannel:" + searchQueue(text) + "\n---------------------------------------------------------------";

        return customerInfromations;

    }

    public static String searchSessionID(String text) {

        String sessionId = "";
        if (text.equals("")) {
            return "";
        } else {

            int sessionIdIndex = text.lastIndexOf("Session ID:");
            if (sessionIdIndex > -1) {
                sessionId = text.substring(sessionIdIndex + 11);
                String[] s = sessionId.split("\n");
                return s[0];
            }
            return "";

        }

    }

    public static String searchPostalCode(String text) {

        String postalcode = "";

        if (text.equals("")) {
            return "";
        } else {
            int postalcodeindex = text.lastIndexOf("Postal Code:");
          
            if (postalcodeindex > -1) {
                postalcode = text.substring(postalcodeindex + 12);
                String[] s = postalcode.split("\n");

                return s[0].toUpperCase();

            }
            return "";
        }

    }

    public static String searchQueue(String text) {

        String Queue = "";

        if (text.equals("")) {
            return "";
        } else {
            int Queueindex = text.lastIndexOf("Channel:");
            if (Queueindex > -1) {
                Queue = text.substring(Queueindex + 8);
                String[] s = Queue.split("\n");

                return s[0];

            }
            return "";
        }

    }

}
