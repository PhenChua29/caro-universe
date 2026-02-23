package data;

import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTest {

    @Test
    public void testSetTotalScoreWithNegativeValue() {
        Record record = new Record("Test");
        record.setTotalScore(-1);
        assertEquals(0, record.getTotalScore());
    }

    @Test
    public void testSetTotalScoreWithZero() {
        Record record = new Record("Test");
        record.setTotalScore(0);
        assertEquals(0, record.getTotalScore());
    }

    @Test
    public void testSetTotalScoreWithPositiveValue() {
        Record record = new Record("Test");
        record.setTotalScore(1);
        assertEquals(1, record.getTotalScore());
    }

    @Test
    public void testSetTotalGamesWithNegativeValue() {
        Record record = new Record("Test");
        record.setTotalGames(-1);
        assertEquals(0, record.getTotalGames());
    }

    @Test
    public void testSetTotalGamesWithZero() {
        Record record = new Record("Test");
        record.setTotalGames(0);
        assertEquals(0, record.getTotalGames());
    }

    @Test
    public void testSetTotalGamesWithPositiveValue() {
        Record record = new Record("Test");
        record.setTotalGames(1);
        assertEquals(1, record.getTotalGames());
    }
}
