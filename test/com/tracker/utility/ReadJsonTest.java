package com.tracker.utility;

import com.tracker.model.Notes;
import com.tracker.model.Scripts;
import com.tracker.model.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import com.tracker.utility.ReadJsonFiles;
import java.io.IOException;
import java.util.List;
import junit.framework.TestCase;

/**
 *
 * @author Tahmina Khan
 */
public class ReadJsonTest extends TestCase {

    private ReadJsonFiles readJsonFiles;

    public ReadJsonTest() {
    }

    @BeforeClass
    public void setUpClass() {

        readJsonFiles = new ReadJsonFiles();

    }

    @AfterClass
    public void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testReadNotesJason() throws IOException {

        List<Notes> notes = ReadJsonFiles.readNotesJson("notes.json");

        assertEquals(notes, notes);
        // Verify  got correct size of the notes.
        assertEquals("Error: Failure to get corrrect size", notes.size(), 20);
        assertEquals("Error: Failure to get corrrect title of the note", notes.get(1).getNotestitle(), "xfer to ICC");

    }

    @Test
    public void testReadScriptObject() throws IOException {

        Scripts scripts = ReadJsonFiles.readScriptsJson("scripts.json");

        //Check that it obtain an access to scripts
        assertNotNull("Error: Failure to get script object", scripts);
        assertEquals("Error: Failure to get corrrect size of scripts", scripts.getMenus().size(), 16);
      
    }
}
