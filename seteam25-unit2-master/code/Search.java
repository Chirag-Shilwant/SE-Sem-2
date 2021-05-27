import java.beans.beancontext.BeanContext;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class Search {
//    Vector<Score> scores;

    Vector<String> bowlers;

    public Search(){
        try {
//            scores = ScoreHistoryFile.getAllScores();
            bowlers = BowlerFile.getBowlers();
        }
        catch (Exception e) {
            System.out.println("Error..." + e);
        }
    }

    public Score findTopScore() {
        // Made the process buffered so that it is memory safe
        int  topScore = -1;
        Score bestScore = null;

        Score bestPlayerScore;
        for (String nick : bowlers) {

            bestPlayerScore = findTopPlayerScore(nick);
            if(Integer.parseInt(bestPlayerScore.getScore()) > topScore) {
                topScore = Integer.parseInt(bestPlayerScore.getScore());
                bestScore = bestPlayerScore;
            }
        }
        return bestScore;
    }

    public Vector<Object> findTopPlayer() {
        HashMap<String, Double> playerScores = new HashMap<String, Double>();
        Vector<Score> scores;
        for(String nick : bowlers) {
            try {
                scores = ScoreHistoryFile.getScores(nick);
                double avg = 0.0;
                for(Score s: scores){
                    avg +=  Integer.parseInt(s.getScore());
                }
                if(scores.size() != 0)
                    avg /= scores.size();
                playerScores.put(nick, avg);
            }
            catch (Exception e) {
                System.out.println("Error..." + e);
            }
        }

        String key = playerScores.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();

        Vector<Object> v = new Vector<>();
        v.add(key);
        v.add(playerScores.get(key));

        return v;
    }

    public Score findLowScore() {
        // Made the process buffered so that it is memory safe
        int  lowScore = 400;
        Score bestScore = null;

        Score bestPlayerScore;
        for (String nick : bowlers) {
            bestPlayerScore = findLowPlayerScore(nick);
            if(Integer.parseInt(bestPlayerScore.getScore()) < lowScore) {
                lowScore = Integer.parseInt(bestPlayerScore.getScore());
                bestScore = bestPlayerScore;
            }
        }
        return bestScore;
    }

    public Score findTopPlayerScore(String nick) {
        int topScore = -1;
        Score bestScore = null;
        try {
            Vector<Score> scores = ScoreHistoryFile.getScores(nick);
            for(Score s: scores) {
                if(Integer.parseInt(s.getScore()) > topScore){
                    topScore = Integer.parseInt(s.getScore());
                    bestScore = s;
                }
            }
        }
        catch (Exception e) {
            System.out.println("Error..." + e);
        }
        return bestScore;
    }

    public Score findLowPlayerScore(String nick) {
        int lowScore = 400;
        Score bestScore = null;
        try {
            Vector<Score> scores = ScoreHistoryFile.getScores(nick);
            for(Score s: scores) {
                if(Integer.parseInt(s.getScore()) < lowScore){
                    lowScore = Integer.parseInt(s.getScore());
                    bestScore = s;
                }
            }
        }
        catch (Exception e) {
            System.out.println("Error..." + e);
        }
        return bestScore;
    }

    public List<Score> getLastScores(String nick) {
        try {
            Vector<Score> scores = ScoreHistoryFile.getScores(nick);
            return scores.subList(Math.max(scores.size() - 6, 0), scores.size() - 1);
        }
        catch (Exception e) {
            System.out.println("Err..."  + e);
        }
        return null;
    }


}
