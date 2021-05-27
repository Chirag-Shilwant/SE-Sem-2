import java.io.*;
import java.util.Vector;

public class SaveFile {
    private static String SAVE_DAT = "SAVE_STATES.DAT"; // The location of the Saves database

    public static void addSaveState(LaneEvent le){

         Vector allSavedGames = SaveFile.getAllSavedGames();

         if(allSavedGames == null)
             allSavedGames = new Vector<LaneEvent>();
         allSavedGames.add(le);
        try{
            FileOutputStream saveFile = new FileOutputStream(SAVE_DAT);
            ObjectOutputStream save = new ObjectOutputStream(saveFile);
            save.writeObject(allSavedGames);
            save.close();
            saveFile.close();
            System.out.println("Game Saved");
        }
        catch(Exception exc){
            System.out.println("masao" + exc);
        }
    }

    public static Vector<LaneEvent> getAllSavedGames() {
        try {
            FileInputStream fis = new FileInputStream(SAVE_DAT);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Vector<LaneEvent> allSavedGames = (Vector<LaneEvent>)ois.readObject();
            System.out.println(allSavedGames);
            ois.close();
            fis.close();
            return allSavedGames;
        }

        catch (IOException e) {
            System.out.println("masao" + e);
            return null;
        } catch (ClassNotFoundException e) {
            System.out.println("masao" + e);
            return new Vector<LaneEvent>();
        }

    }

    public static Vector<LaneEvent> getSavedGames(String nickName) {
        Vector<LaneEvent> allSavedGames = SaveFile.getAllSavedGames();
        Vector<LaneEvent> savedGames = new Vector<LaneEvent>();
        if (allSavedGames == null)
            return savedGames;
        for(LaneEvent le: allSavedGames){
            for(Object b: le.getParty().getMembers()){
                if(nickName.equals(((Bowler) b).getNickName()))
                {
                    savedGames.add(le);
                    break;
                }
            }
        }
        return savedGames;
    }
}
