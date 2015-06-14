/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tracker.utility;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Tahmina Khan
 */
public class UtilitiesTest extends TestCase {

    private String notes;

    public UtilitiesTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        notes = "Session ID: 211111111\n"
                + "Customer Name/Nom du Client: Jhon Doe\n"
                + "Status: Active\n"
                + "Time: 01:27\n"
                + "Total Time: 01:32\n"
                + "Platform: Windows 7 Google Chrome\n"
                + "Channel: NTSD General Support - Wireless\n"
                + "Type: Instant Chat\n"
                + "Postal Code: A1A1A1\n"
                + "CTN/Phone: (999) 999-999\n"
                + "\n"
                + "\n"
                + "Status: white\n"
                + "Service: Rogers_devices\n"
                + "Creation: 2013-09-03T16:51:20Z\n"
                + "MSISDN: 19999999999\n"
                + "IMSI: 123456789012345\n"
                + "IMEI: 1212121212121212\n"
                + "Device: Apple iPhone 5\n"
                + "Device pic adress : http://172.16.224.34:8002/webcmd/devicepic?brand=BlackBerry&model=9700\n"
                + "Device: Apple iPhone 5\n"
                + "Device pic adress : http://172.16.224.34:8002/webcmd/devicepic?brand=Apple&model=iPhone 5\n"
                + "Groups: rogers\n"
                + "OTASupport: true\n"
                + "APNs: blackberry.net,goam.com,internet.com,isp.apn,ltedata.apn,lteinternet.apn,ltemobile.apn,media.com,rogers-core-appl1.apn\n"
                + "DeviceCapa:  Ok\n"
                + "Single Shot Provisioning: ext1,yes,2012-11-04T12:07:52Z\n"
                + "Settings: \n"
                + "History";
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSearchCustomerName() {

        assertTrue("Error: Failure to search Customer's name", Utilities.searchCustomerName("").equals(""));
        assertTrue("Error: Failure to search Customer's name", Utilities.searchCustomerName(notes) != null);
        assertEquals("Error: Failure to search Customer's name", Utilities.searchCustomerName(notes), " Jhon Doe");

    }

    @Test
    public void testSearchCTN() {

        assertTrue("Error: Failure to search CTN", Utilities.searchCtn("").equals(""));
        assertTrue("Error: Failure to Failure to search CTN", Utilities.searchCtn(notes) != null);
        assertEquals("Error: Failure to Failure to search CTN", Utilities.searchCtn(notes), "999999999");

    }

    @Test
    public void testSearchIMEI() {

        assertTrue("Error: Failure to search IMEI", Utilities.searchImei("").equals(""));

        
        assertTrue("Error: Failure to Failure to search IMEI", Utilities.searchImei(notes) != null);
        assertEquals("Error: Failure to Failure to search IMEI", Utilities.searchImei(notes), "1212121212121212");

    }
    
    
    @Test
    public void testFormateVoiceMailRetrivalNumber() {

        assertTrue("Error: Failure to search IMEI", Utilities.fomateVoiceMailRetrivalNumber("").equals(""));

        
      //  assertTrue("Error: Failure to Failure to search IMEI", Utilities.searchImei(notes) != null);
      //  assertEquals("Error: Failure to Failure to search IMEI", Utilities.searchImei(notes), "1212121212121212");

    }
}
