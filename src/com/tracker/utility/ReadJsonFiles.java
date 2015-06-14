package com.tracker.utility;

import com.google.gson.stream.JsonReader;
import com.tracker.model.Issue;
import com.tracker.model.MainMenu;
import com.tracker.model.Make;
import com.tracker.model.Model;
import com.tracker.model.Notes;
import com.tracker.model.Script;
import com.tracker.model.Scripts;
import com.tracker.model.Steps;
import com.tracker.model.SubMenu;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tahmina Khan
 */

//Read Json Files and returns Json Object or Json Array
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
       
        return noteobject;
    }

    
     /**
     *
     *
     * @param jsonFileName
     * @return Scripts that read from the file
     */
    public static Scripts readScriptsJson(String jsonFileName) throws IOException {
        JsonReader reader = new JsonReader(new FileReader(jsonFileName));
        try {
            return readScriptObject(reader);
        } finally {
            reader.close();
        }

    }

    /**
     *
     * @param reader
     * @return
     * @throws IOException
     *
     * reading the actual Script object from the json
     *
     */
    private  static Scripts readScriptObject(JsonReader reader) throws IOException {

        Scripts readScripts = new Scripts();
        String name = "";

        reader.beginObject();
        while (reader.hasNext()) {
            name = reader.nextName();
            if (name.equals("scripts")) {

                // reading menus
                readScripts.setMenus(readMenuList(reader));

            } else {
                reader.skipValue();
            }
        }

        reader.endObject();

        return readScripts;
    }

    private static List<MainMenu> readMenuList(JsonReader reader) throws IOException {

        List<MainMenu> menus = new ArrayList<MainMenu>();
        MainMenu menu = null;
        int id = -1;
        String menuName = "";
        List<SubMenu> submenus = null;

        reader.beginArray();
        while (reader.hasNext()) {
            menus.add(readMainMenus(reader));

        }

        reader.endArray();
        return menus;
    }

    // read each menu,and add to manu list
    private static MainMenu readMainMenus(JsonReader reader) throws IOException {

        MainMenu menu = null;
        int id = -1;
        String menuname = "";
        List<SubMenu> submenus = null;

        reader.beginObject();
        while (reader.hasNext()) {

            String name = reader.nextName();
            if (name.equals("id")) {
                id = reader.nextInt();
            } else if (name.equals("menuname")) {
                menuname = reader.nextString();
            } else if (name.equals("submenus")) {

                submenus = readSubMenus(reader);
            } else {
                reader.skipValue();
            }

        }

        reader.endObject();

        menu = new MainMenu(id, menuname, submenus);

        return menu;

    }

    private static List<SubMenu> readSubMenus(JsonReader reader) throws IOException {

        List<SubMenu> submenu = new ArrayList<SubMenu>();

        reader.beginArray();
        while (reader.hasNext()) {

            submenu.add(readSubMenu(reader));
        }

        reader.endArray();

        return submenu;
    }

    private static SubMenu readSubMenu(JsonReader reader) throws IOException {

        SubMenu submenu = null;
        String submenuname = "";
        String submenutitle = "";
        List<Script> submenuscripts = null;
        String name = "";
        reader.beginObject();
        while (reader.hasNext()) {

            name = reader.nextName();

            if (name.equals("submenuname")) {
                submenuname = reader.nextString();
            } else if (name.equals("submenutitle")) {
                submenutitle = reader.nextString();
            } else if (name.equals("submenusripts")) {
                submenuscripts = readSubMenuScripts(reader);
            } else {
                reader.skipValue();
            }

        }
        reader.endObject();

        submenu = new SubMenu(submenuname, submenutitle, submenuscripts);

        return submenu;

    }

    private static List<Script> readSubMenuScripts(JsonReader reader) throws IOException {

        List<Script> submenus = new ArrayList<Script>();

        reader.beginArray();
        while (reader.hasNext()) {
            submenus.add(readSubmenuScript(reader));
        }

        reader.endArray();

        return submenus;

    }

    private static Script readSubmenuScript(JsonReader reader) throws IOException {

        String title = "";
        String scripttext = "";
        Script script = null;
        String name = "";

        reader.beginObject();
        while (reader.hasNext()) {
            name = reader.nextName();
            if (name.equals("scripttitle")) {
                title = reader.nextString();
            } else if (name.equals("script")) {
                scripttext = reader.nextString();
            } else {
                reader.skipValue();
            }

        }

        reader.endObject();

        return new Script(title, scripttext);

    }
    
    

    public static List<Make> readDevicesJson(String jsonFileName) throws IOException {
        JsonReader reader = new JsonReader(new FileReader(jsonFileName));
        try {
            return readMakesList(reader);
        } finally {
            reader.close();
        }

    }

    private static List<Make> readMakesList(JsonReader reader) throws IOException {

        List<Make> makes = new ArrayList<Make>();

        reader.beginArray();
        while (reader.hasNext()) {
            makes.add(readMake(reader));

        }

        reader.endArray();
        return makes;
    }

    private static Make readMake(JsonReader reader) throws IOException {

        String name = "";

        int id = -1;
        String makeName = "";
        List<Model> models = null;

        reader.beginObject();
        while (reader.hasNext()) {

            name = reader.nextName();

            if (name.equals("id")) {
                id = reader.nextInt();
            } else if (name.equals("make")) {
                makeName = reader.nextString();
            } else if (name.equals("modeloros")) {
                models = readModels(reader);
            } else {
                reader.skipValue();
            }

        }
        reader.endObject();

        return new Make(id, makeName, models);

    }

    private static List<Model> readModels(JsonReader reader) throws IOException {

        List<Model> models = new ArrayList<Model>();

        reader.beginArray();
        while (reader.hasNext()) {
            models.add(readModel(reader));
        }
        reader.endArray();

        return models;

    }

    private static Model readModel(JsonReader reader) throws IOException {

        String name = "";

        String modelName = "";
        List<Issue> issues = null;

        reader.beginObject();
        while (reader.hasNext()) {

            name = reader.nextName();

            if (name.equals("modelorosname")) {
                modelName = reader.nextString();
            } else if (name.equals("issues")) {
                issues = readIssues(reader);
            } else {
                reader.skipValue();
            }

        }
        reader.endObject();

        return new Model(modelName, issues);
    }

    private static List<Issue> readIssues(JsonReader reader) throws IOException {
        List<Issue> issues = new ArrayList<Issue>();

        reader.beginArray();
        while (reader.hasNext()) {
            issues.add(readIssue(reader));
        }

        reader.endArray();

        return issues;
    }

    private static Issue readIssue(JsonReader reader) throws IOException {

        Issue issue = null;

        String name = "";

        String issueName = "";
        List<Steps> steps = new ArrayList<Steps>();

        reader.beginObject();
        while (reader.hasNext()) {

            name = reader.nextName();

            if (name.equals("issue")) {
                issueName = reader.nextString();
            } else if (name.equals("steps")) {
                steps = readSteps(reader);
            } else {
                reader.skipValue();
            }

        }
        reader.endObject();

        return new Issue(issueName, steps);
    }

    private static List<Steps> readSteps(JsonReader reader) throws IOException {
        List<Steps> steps = new ArrayList<Steps>();

        reader.beginArray();
        while (reader.hasNext()) {
            steps.add(readStep(reader));
        }
        reader.endArray();

        return steps;
    }

    private static Steps readStep(JsonReader reader) throws IOException {

        String name = "";
        String stepTitle = "";
        String stepText = "";

        reader.beginObject();

        while (reader.hasNext()) {

            name = reader.nextName();

            if (name.equals("steptitle")) {
                stepTitle = reader.nextString();
            } else if (name.equals("step")) {
                stepText = reader.nextString();
            } else {
                reader.skipValue();
            }

        }
        reader.endObject();

        return new Steps(stepTitle, stepText);
    }

}
