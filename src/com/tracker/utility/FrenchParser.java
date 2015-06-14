
package com.tracker.utility;

/**
 *
 * @author Tahmina Khan
 */

/*

Helper class to parse french character to and from HTML


*/

public class FrenchParser {
    
    //Encode french character to html code
    public static String encodeToAcutesHTML(String str) {
                
                str = str.replaceAll("À","&#192;");
                str = str.replaceAll("à","&#224;");
                str = str.replaceAll("Â","&#194;");
                str = str.replaceAll("â","&#226;");
                str = str.replaceAll("æ","&#230;");
                str = str.replaceAll("Ç","&#199;");
                str = str.replaceAll("ç","&#231;");
                str = str.replaceAll("È","&#200;");
                str = str.replaceAll("É","&#201;");
                str = str.replaceAll("é","&#233;");
                str = str.replaceAll("Ê","&#202;");
                str = str.replaceAll("ê","&#234;");
                str = str.replaceAll("Ë","&#203;");
                str = str.replaceAll("ë","&#235;");
                str = str.replaceAll("Î","&#206;");
                str = str.replaceAll("î","&#238;");
                str = str.replaceAll("Ï","&#207;");
                str = str.replaceAll("ï","&#239;");
                str = str.replaceAll("Ô","&#212;");
                str = str.replaceAll("ô","&#244;");
                str = str.replaceAll("Œ","&#140;");
                str = str.replaceAll("œ","&#156;");
                str = str.replaceAll("Ù","&#217;");
                str = str.replaceAll("ù","&#249;");
                str = str.replaceAll("Û","&#219;");
                str = str.replaceAll("û","&#251;");
                str = str.replaceAll("Ü","&#220;");
                str = str.replaceAll("ü","&#252;");
                str = str.replaceAll("«","&#171;");
                str = str.replaceAll("€","&#128;");
                str = str.replaceAll("₣","&#8355;");
                
                str = str.replace("è","&#232;");
                str = str.replace("’","&#8217;");

        return str;
    }
    
    
    //Decode html to french code
    public static String decodeToAcutesHTML(String str) {
                
                str = str.replaceAll("&#192;","À");
                str = str.replaceAll("&#224;","à");
                str = str.replaceAll("&#194;","Â");
                str = str.replaceAll("&#226;","â");
                str = str.replaceAll("&#230;","æ");
                str = str.replaceAll("&#199;","Ç");
                str = str.replaceAll("&#231;","ç");
                str = str.replaceAll("&#200;","È");
                str = str.replaceAll("&#201;","É");
                str = str.replaceAll("&#233;","é");
                str = str.replaceAll("&#202;","Ê");
                str = str.replaceAll("&#234;","ê");
                str = str.replaceAll("&#203;","Ë");
                str = str.replaceAll("&#235;","ë");
                str = str.replaceAll("&#206;","Î");
                str = str.replaceAll("&#238;","î");
                str = str.replaceAll("&#207;","Ï");
                str = str.replaceAll("&#239;","ï");
                str = str.replaceAll("&#212;","Ô");
                str = str.replaceAll("&#244;","ô");
                str = str.replaceAll("&#140;","Œ");
                str = str.replaceAll("&#156;","œ");
                str = str.replaceAll("&#217;","Ù");
                str = str.replaceAll("&#249;","ù");
                str = str.replaceAll("&#219;","Û");
                str = str.replaceAll("&#251;","û");
                str = str.replaceAll("&#220;","Ü");
                str = str.replaceAll("&#252;","ü");
                str = str.replaceAll("&#171;","«");
                str = str.replaceAll("&#128;","€");
                str = str.replaceAll("&#8355;","₣");
                
                str = str.replace("&#232;", "è");
                str = str.replace("&#8217;", "’");

        return str;
    }
}
