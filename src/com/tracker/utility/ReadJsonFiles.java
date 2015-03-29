package com.tracker.utility;

import com.google.gson.stream.JsonReader;
import com.tracker.model.Notes;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tahmina Khan
 */

//Read Json Files
public class ReadJsonFiles {

    public ReadJsonFiles() {

    }

    public static List<Notes> readNotesJson(String noteJsonFile) throws IOException {
        JsonReader reader = new JsonReader(new FileReader(noteJsonFile));
        try {
            return readNotesArray(reader);
        } finally {
            reader.close();
        }
    }

    public static List<Notes> readNotesArray(JsonReader reader) throws IOException {
        List<Notes> localnotes = new ArrayList<Notes>();

        reader.beginArray();
        while (reader.hasNext()) {
            localnotes.add(readNote(reader));
        }
        reader.endArray();
        return localnotes;
    }

    public static Notes readNote(JsonReader reader) throws IOException {
        int id = -1;
        String notetitle = "";
        String notes = "";
        Notes noteobject;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id")) {
                id = reader.nextInt();
            } else if (name.equals("title")) {
                notetitle = reader.nextString();

            } else if (name.equals("notes")) {
                notes = reader.nextString();

            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        noteobject = new Notes(id, notetitle, notes);
        //System.out.println(noteobject.toString());
        return noteobject;
    }

}
