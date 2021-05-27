import java.util.Vector;

public class ScoreCalculator {
    public ScoreCalculator() {
    }

    /**
     * getScore()
     * <p>
     * Method that calculates a bowlers score
     * <p>
     * //	 * @param Cur		The bowler that is currently up
     *
     * @param frame The frame the current bowler is on
     * @return The bowlers total score
     */
    int[] getScore(int frame, int[] curScore, int bowlIndex, int ball) {
//		int[] curScore;
//		int strikeballs = 0;
        int totalScore = 0;
        int[] cumulScore = new int[10];
//		curScore = (int[]) scores.get(Cur);
        for (int i = 0; i != 10; i++) {
            cumulScore[i] = 0;
        }
        int current = 2 * (frame - 1) + ball - 1;
//		int test_current = 2 * (frame  - 1 ) + ball;
//		System.out.println("current");
//		System.out.println(current);
//		System.out.println("Test Current");
//		System.out.println(test_current);
//		if (current != -1) {
//			System.out.println("Frame");
//			System.out.println(frame);
//			System.out.println("Ball");
//			System.out.println(ball);
//			System.out.println("Score of the turn");
//			System.out.println(curScore[current]);
//		}
//		System.out.println("All the scores");
//		for(int x: curScore) {
//			System.out.print(x);
//			System.out.print(" ");
//		}
//		System.out.println("");
        //Iterate through each ball until the current one.
//		score_1(curScore, current);

        if (current == -1) {
            cumulScore[0] = curScore[0];
            return cumulScore;
        }

        Vector<Integer> vals = new Vector<Integer>();
        for (int i = 0; i <= current; i++) {
            if (curScore[i] != -1)
                vals.add(curScore[i]);
        }
        if (ball == 1 || vals.lastElement().equals(10)) {
            vals.add(0);
        }
        vals.add(0);

        int totalFrames = Math.min(current / 2 + 1, 10);
//		if(totalFrames > 10) {
//			totalFrames = 10;
//		}
//        System.out.println("Total Frames");
//        System.out.println(totalFrames);

//		int i = 0;

//		System.out.println("Vals size");
//		System.out.println(vals.size());

        getTotalScore(totalScore, vals, totalFrames, cumulScore);

        return cumulScore;
    }

    int[] getTotalScore(int totalScore, Vector<Integer> vals, int totalFrames, int[] cumulScore) {
        int roll;
        int i = 0;
        for (int frames = 0; frames < totalFrames; frames++) {
            if (vals.elementAt(i).equals(10)) {
                roll = 10 + vals.elementAt(i + 1) + vals.elementAt(i + 2);
                totalScore += roll;
                cumulScore[frames] = totalScore;
                i += 1;
            } else {
                roll = vals.elementAt(i) + vals.elementAt(i + 1);
                if (roll == 10) {
                    roll += vals.elementAt(i + 2);
                }
                totalScore += roll;
                cumulScore[frames] = totalScore;
                i += 2;
            }
        }
        return cumulScore;
    }
}