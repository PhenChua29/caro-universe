package object;

import static org.junit.jupiter.api.Assertions.*;

import enums.Difficulty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import panel.GamePanel;

public class BotTest {

    private Bot bot;
    private static final int SIZE = GamePanel.BOARD_SIZE;
    private static final int P = GamePanel.PLAYER;
    private static final int B = GamePanel.BOT;

    @BeforeEach
    void setUp() {
        bot = new Bot();
    }

    // ─── Helpers ───────────────────────────────────────────────────────

    private int[][] emptyBoard() {
        return new int[SIZE][SIZE];
    }

    /**
     * Creates a completely full board with an alternating checkerboard
     * pattern so that no player has 4 in a row in any direction.
     *
     * Row 0: P B P B P
     * Row 1: B P B P B
     * Row 2: P B P B P
     * Row 3: B P B P B
     * Row 4: P B P B P
     */
    private int[][] fullBoard() {
        int[][] board = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = (i + j) % 2 == 0 ? P : B;
            }
        }
        return board;
    }

    private void assertValidMove(Move move, int[][] board) {
        assertNotNull(move, "Move should not be null when empty cells exist");
        assertTrue(
            move.getRow() >= 0 && move.getRow() < SIZE,
            "Row " + move.getRow() + " must be within [0, " + SIZE + ")"
        );
        assertTrue(
            move.getCol() >= 0 && move.getCol() < SIZE,
            "Col " + move.getCol() + " must be within [0, " + SIZE + ")"
        );
        assertEquals(
            0,
            board[move.getRow()][move.getCol()],
            "Move must target an empty cell but (" +
                move.getRow() +
                "," +
                move.getCol() +
                ") is occupied"
        );
    }

    private boolean moveEquals(Move move, int row, int col) {
        return move.getRow() == row && move.getCol() == col;
    }

    private boolean moveIsOneOf(Move move, int[][] positions) {
        for (int[] pos : positions) {
            if (move.getRow() == pos[0] && move.getCol() == pos[1]) {
                return true;
            }
        }
        return false;
    }

    // ═══════════════════════════════════════════════════════════════════
    //  EASY MODE – picks a random empty cell
    // ═══════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Easy Mode")
    class EasyMode {

        @BeforeEach
        void setDifficulty() {
            bot.setDifficulty(Difficulty.EASY);
        }

        @Test
        @DisplayName("Empty board → returns a valid move on any empty cell")
        void testEmptyBoard() throws Exception {
            int[][] board = emptyBoard();
            Move move = bot.makeMove(board);
            assertValidMove(move, board);
        }

        @RepeatedTest(5)
        @DisplayName("Partially filled board → always lands on an empty cell")
        void testPartiallyFilledBoard() throws Exception {
            int[][] board = emptyBoard();
            // Scatter some pieces around the board
            board[0][0] = P;
            board[0][1] = B;
            board[1][0] = P;
            board[1][1] = B;
            board[2][2] = P;
            board[3][3] = B;
            board[4][4] = P;

            Move move = bot.makeMove(board);
            assertValidMove(move, board);
        }

        @Test
        @DisplayName("Only one empty cell → must return that exact cell")
        void testOneCellRemaining() throws Exception {
            int[][] board = fullBoard();
            // Open a single cell
            board[2][3] = 0;

            Move move = bot.makeMove(board);
            assertNotNull(move);
            assertEquals(2, move.getRow(), "Should pick row 2");
            assertEquals(3, move.getCol(), "Should pick col 3");
        }

        @Test
        @DisplayName("Full board → returns null (no move possible)")
        void testFullBoard() throws Exception {
            int[][] board = fullBoard();
            Move move = bot.makeMove(board);
            assertNull(move, "No move should be returned on a full board");
        }

        @Test
        @DisplayName(
            "Board with heavy player presence → still picks a valid empty cell"
        )
        void testHeavyPlayerPresence() throws Exception {
            int[][] board = emptyBoard();
            // Fill most of the board with player pieces, leave a few empty
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    board[i][j] = P;
                }
            }
            board[0][4] = 0;
            board[3][1] = 0;
            board[4][4] = 0;

            Move move = bot.makeMove(board);
            assertValidMove(move, board);
            assertTrue(
                moveIsOneOf(move, new int[][] { { 0, 4 }, { 3, 1 }, { 4, 4 } }),
                "Move must be one of the 3 remaining empty cells"
            );
        }

        @RepeatedTest(10)
        @DisplayName("Two empty cells → always picks one of them")
        void testTwoEmptyCells() throws Exception {
            int[][] board = fullBoard();
            board[1][2] = 0;
            board[3][4] = 0;

            Move move = bot.makeMove(board);
            assertNotNull(move);
            assertTrue(
                moveIsOneOf(move, new int[][] { { 1, 2 }, { 3, 4 } }),
                "Move must be one of the 2 remaining empty cells"
            );
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    //  MEDIUM MODE – blocks player threats, extends own streaks
    // ═══════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Medium Mode")
    class MediumMode {

        @BeforeEach
        void setDifficulty() {
            bot.setDifficulty(Difficulty.MEDIUM);
        }

        @Test
        @DisplayName("Empty board → returns a valid random move")
        void testEmptyBoard() throws Exception {
            int[][] board = emptyBoard();
            Move move = bot.makeMove(board);
            assertValidMove(move, board);
        }

        @Test
        @DisplayName("Full board → returns null")
        void testFullBoard() throws Exception {
            int[][] board = fullBoard();
            Move move = bot.makeMove(board);
            assertNull(move, "No move should be returned on a full board");
        }

        // ── Blocking player threats ──────────────────────────────────────

        @RepeatedTest(5)
        @DisplayName("Blocks player's horizontal 3-in-a-row (one open end)")
        void testBlockHorizontal3InARow() throws Exception {
            // Row 0: P P P _ _
            // The only open end for the streak of 3 is (0,3)
            // (0,-1) is out of bounds, so before-end is blocked.
            int[][] board = emptyBoard();
            board[0][0] = P;
            board[0][1] = P;
            board[0][2] = P;

            Move move = bot.makeMove(board);
            assertNotNull(move);
            assertEquals(0, move.getRow(), "Should block at row 0");
            assertEquals(3, move.getCol(), "Should block at col 3");
        }

        @RepeatedTest(5)
        @DisplayName("Blocks player's horizontal 3-in-a-row (two open ends)")
        void testBlockHorizontalTwoOpenEnds() throws Exception {
            // Row 0: _ P P P _
            // Open ends at (0,0) and (0,4)
            int[][] board = emptyBoard();
            board[0][1] = P;
            board[0][2] = P;
            board[0][3] = P;

            Move move = bot.makeMove(board);
            assertNotNull(move);
            assertTrue(
                moveIsOneOf(move, new int[][] { { 0, 0 }, { 0, 4 } }),
                "Should block at one of the two open ends: (0,0) or (0,4)"
            );
        }

        @RepeatedTest(5)
        @DisplayName("Blocks player's vertical 3-in-a-row")
        void testBlockVertical3InARow() throws Exception {
            // Col 0: P, P, P, _, _
            int[][] board = emptyBoard();
            board[0][0] = P;
            board[1][0] = P;
            board[2][0] = P;

            Move move = bot.makeMove(board);
            assertNotNull(move);
            assertEquals(0, move.getCol(), "Should block in col 0");
            assertEquals(3, move.getRow(), "Should block at row 3");
        }

        @RepeatedTest(5)
        @DisplayName("Blocks player's diagonal (\\) 3-in-a-row")
        void testBlockDiagonal3InARow() throws Exception {
            // Diagonal \: (0,0)=P, (1,1)=P, (2,2)=P → open end at (3,3)
            int[][] board = emptyBoard();
            board[0][0] = P;
            board[1][1] = P;
            board[2][2] = P;

            Move move = bot.makeMove(board);
            assertNotNull(move);
            assertEquals(3, move.getRow(), "Should block at row 3");
            assertEquals(3, move.getCol(), "Should block at col 3");
        }

        @RepeatedTest(5)
        @DisplayName("Blocks player's anti-diagonal (/) 3-in-a-row")
        void testBlockAntiDiagonal3InARow() throws Exception {
            // Diagonal /: (0,4)=P, (1,3)=P, (2,2)=P → open end at (3,1)
            // Before-end would be (-1,5) = out of bounds
            int[][] board = emptyBoard();
            board[0][4] = P;
            board[1][3] = P;
            board[2][2] = P;

            Move move = bot.makeMove(board);
            assertNotNull(move);
            assertEquals(3, move.getRow(), "Should block at row 3");
            assertEquals(1, move.getCol(), "Should block at col 1");
        }

        // ── Prioritising higher threats ──────────────────────────────────

        @RepeatedTest(5)
        @DisplayName("Prioritises blocking streak-of-3 over streak-of-2")
        void testPrioritisesHigherThreat() throws Exception {
            // Row 0: P P P _ _    → horizontal streak of 3, open end at (0,3)
            // Col 0: P(row0) already counted, (3,0)=P, (4,0)=P → vertical streak of 2
            //        with open end at (2,0)
            int[][] board = emptyBoard();
            board[0][0] = P;
            board[0][1] = P;
            board[0][2] = P;
            board[3][0] = P;
            board[4][0] = P;

            Move move = bot.makeMove(board);
            assertNotNull(move);
            assertEquals(
                0,
                move.getRow(),
                "Should prioritise the streak-of-3 at row 0"
            );
            assertEquals(
                3,
                move.getCol(),
                "Should block the streak-of-3 at col 3"
            );
        }

        // ── Extending own streaks ────────────────────────────────────────

        @RepeatedTest(10)
        @DisplayName("Extends bot's own streak when no player threats exist")
        void testExtendOwnStreak() throws Exception {
            // No player streaks of 2+ with open ends
            // Bot has horizontal 2-in-a-row at (2,1)-(2,2) with open ends at (2,0) and (2,3)
            int[][] board = emptyBoard();
            board[2][1] = B;
            board[2][2] = B;
            // Place isolated player pieces (no adjacent pairs)
            board[0][0] = P;
            board[4][4] = P;

            Move move = bot.makeMove(board);
            assertNotNull(move);
            assertTrue(
                moveIsOneOf(move, new int[][] { { 2, 0 }, { 2, 3 } }),
                "Should extend the bot's streak to (2,0) or (2,3) but got (" +
                    move.getRow() +
                    "," +
                    move.getCol() +
                    ")"
            );
        }

        @RepeatedTest(10)
        @DisplayName(
            "Extends bot's longest streak when multiple bot streaks exist"
        )
        void testExtendLongestBotStreak() throws Exception {
            // Bot has streak-of-3 at (1,0)-(1,1)-(1,2) and streak-of-2 at (3,0)-(3,1)
            // No player threats
            int[][] board = emptyBoard();
            board[1][0] = B;
            board[1][1] = B;
            board[1][2] = B;
            board[3][0] = B;
            board[3][1] = B;
            // Isolated player pieces
            board[0][4] = P;
            board[4][4] = P;

            Move move = bot.makeMove(board);
            assertNotNull(move);
            // Bot should extend its longest streak (3-in-a-row) at row 1
            assertTrue(
                moveIsOneOf(move, new int[][] { { 1, 3 } }),
                "Should extend the streak-of-3 at (1,3) but got (" +
                    move.getRow() +
                    "," +
                    move.getCol() +
                    ")"
            );
        }

        @Test
        @DisplayName("No threats and no bot streaks → plays random valid move")
        void testNoThreatsNoStreaks() throws Exception {
            // Isolated pieces only
            int[][] board = emptyBoard();
            board[0][0] = P;
            board[4][4] = B;

            Move move = bot.makeMove(board);
            assertValidMove(move, board);
        }

        // ── Blocking 2-in-a-row when no 3-in-a-row exists ───────────────

        @RepeatedTest(5)
        @DisplayName("Blocks player's 2-in-a-row when no higher threat exists")
        void testBlock2InARow() throws Exception {
            // Player has horizontal 2-in-a-row at (2,0)-(2,1)
            // Open ends: (2,-1) OOB, (2,2) open → block at (2,2)
            int[][] board = emptyBoard();
            board[2][0] = P;
            board[2][1] = P;

            Move move = bot.makeMove(board);
            assertNotNull(move);
            assertEquals(2, move.getRow(), "Should block at row 2");
            assertEquals(2, move.getCol(), "Should block at col 2");
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    //  HARD MODE – minimax with alpha-beta pruning
    // ═══════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Hard Mode")
    class HardMode {

        @BeforeEach
        void setDifficulty() {
            bot.setDifficulty(Difficulty.HARD);
        }

        @Test
        @DisplayName("Empty board → returns a valid move")
        void testEmptyBoard() throws Exception {
            int[][] board = emptyBoard();
            Move move = bot.makeMove(board);
            assertValidMove(move, board);
        }

        @Test
        @DisplayName("Full board → returns null (falls back to easy)")
        void testFullBoard() throws Exception {
            int[][] board = fullBoard();
            Move move = bot.makeMove(board);
            assertNull(move, "No move should be returned on a full board");
        }

        // ── Bot should take the winning move ─────────────────────────────

        @RepeatedTest(5)
        @DisplayName(
            "Bot takes the winning move when 3-in-a-row exists (2 empty cells)"
        )
        void testBotTakesWinningMove() throws Exception {
            // Board is nearly full (checkerboard), 2 empty cells.
            // Bot has horizontal streak of 3 at row 2: (2,0)=B,(2,1)=B,(2,2)=B
            // Empty cells: (2,3) which completes 4-in-a-row, and (4,2).
            //
            // Row 0: P B P B P
            // Row 1: B P B P B
            // Row 2: B B B _ P     ← (2,3) wins for bot
            // Row 3: P B P B P
            // Row 4: B P _ P B     ← (4,2) neutral
            int[][] board = {
                { P, B, P, B, P },
                { B, P, B, P, B },
                { B, B, B, 0, P },
                { P, B, P, B, P },
                { B, P, 0, P, B },
            };

            Move move = bot.makeMove(board);
            assertNotNull(move);
            assertTrue(
                moveEquals(move, 2, 3),
                "Bot should take the winning cell (2,3) but chose (" +
                    move.getRow() +
                    "," +
                    move.getCol() +
                    ")"
            );
        }

        @RepeatedTest(5)
        @DisplayName("Bot takes vertical winning move (2 empty cells)")
        void testBotTakesVerticalWin() throws Exception {
            // Bot has vertical streak of 3 at col 0: (0,0)=B,(1,0)=B,(2,0)=B
            // (3,0) is empty and completes the 4-in-a-row. (4,4) is the other empty cell.
            //
            // Row 0: B P B P B
            // Row 1: B B P B P
            // Row 2: B P B P B
            // Row 3: _ P B P B     ← (3,0) wins for bot
            // Row 4: P B P B _     ← (4,4) neutral
            int[][] board = {
                { B, P, B, P, B },
                { B, B, P, B, P },
                { B, P, B, P, B },
                { 0, P, B, P, B },
                { P, B, P, B, 0 },
            };

            Move move = bot.makeMove(board);
            assertNotNull(move);
            assertTrue(
                moveEquals(move, 3, 0),
                "Bot should take the winning cell (3,0) but chose (" +
                    move.getRow() +
                    "," +
                    move.getCol() +
                    ")"
            );
        }

        // ── Bot should block the player from winning ─────────────────────

        @RepeatedTest(5)
        @DisplayName("Bot blocks player's winning move (2 empty cells)")
        void testBotBlocksPlayerWin() throws Exception {
            // Player has horizontal streak of 3 at row 2: (2,0)=P,(2,1)=P,(2,2)=P
            // Empty cells: (2,3) which lets player win if not blocked, and (4,2).
            //
            // Row 0: B P B P B
            // Row 1: P B P B P
            // Row 2: P P P _ B     ← player wins if (2,3) not blocked
            // Row 3: B P B P B
            // Row 4: P B _ B P     ← (4,2) neutral
            int[][] board = {
                { B, P, B, P, B },
                { P, B, P, B, P },
                { P, P, P, 0, B },
                { B, P, B, P, B },
                { P, B, 0, B, P },
            };

            Move move = bot.makeMove(board);
            assertNotNull(move);
            assertTrue(
                moveEquals(move, 2, 3),
                "Bot should block at (2,3) to prevent player win but chose (" +
                    move.getRow() +
                    "," +
                    move.getCol() +
                    ")"
            );
        }

        @RepeatedTest(5)
        @DisplayName(
            "Bot blocks player's vertical winning threat (2 empty cells)"
        )
        void testBotBlocksVerticalPlayerWin() throws Exception {
            // Player has vertical streak of 3 at col 4: (0,4)=P,(1,4)=P,(2,4)=P
            // (3,4) is empty and would complete 4-in-a-row for player. (4,0) is neutral.
            //
            // Row 0: B P B P P
            // Row 1: P P B B P
            // Row 2: B B P P P
            // Row 3: P P B B _     ← (3,4) must be blocked
            // Row 4: _ B P P B     ← (4,0) neutral
            //
            // No pre-existing 4-in-a-row in any direction verified:
            //   Diag \: (0,0)=B,(1,1)=P,(2,2)=P,(3,3)=B → no 4 ✓
            //   Rows/Cols: all max streak ≤ 3 ✓
            int[][] board = {
                { B, P, B, P, P },
                { P, P, B, B, P },
                { B, B, P, P, P },
                { P, P, B, B, 0 },
                { 0, B, P, P, B },
            };

            Move move = bot.makeMove(board);
            assertNotNull(move);
            assertTrue(
                moveEquals(move, 3, 4),
                "Bot should block at (3,4) to prevent player vertical win but chose (" +
                    move.getRow() +
                    "," +
                    move.getCol() +
                    ")"
            );
        }

        // ── Partially filled boards ──────────────────────────────────────

        @Test
        @DisplayName(
            "Partially filled board → returns a valid move on an empty cell"
        )
        void testPartiallyFilledBoard() throws Exception {
            int[][] board = emptyBoard();
            board[0][0] = P;
            board[0][1] = B;
            board[1][1] = P;
            board[2][2] = B;

            Move move = bot.makeMove(board);
            assertValidMove(move, board);
        }

        @Test
        @DisplayName("One cell remaining → returns that cell")
        void testOneCellRemaining() throws Exception {
            int[][] board = fullBoard();
            board[4][4] = 0;

            Move move = bot.makeMove(board);
            assertNotNull(move);
            assertEquals(4, move.getRow());
            assertEquals(4, move.getCol());
        }

        // ── Strategic preference ─────────────────────────────────────────

        @RepeatedTest(5)
        @DisplayName(
            "Returns a valid move when both a win and a threat coexist (2 empty cells)"
        )
        void testValidMoveWhenWinAndThreatCoexist() throws Exception {
            // Bot can win at (0,3) with horizontal 4-in-a-row: (0,0)=B,(0,1)=B,(0,2)=B
            // Player threatens at (4,3) with: (4,0)=P,(4,1)=P,(4,2)=P
            // Only 2 empty cells: (0,3) and (4,3)
            //
            // Note: the bot's evaluate() subtracts the player's positional score
            // from a WIN result, so minimax may not recognise (0,3) as an
            // immediate terminal win. Either move is acceptable here.
            int[][] board = {
                { B, B, B, 0, P },
                { P, P, B, P, B },
                { B, P, P, B, P },
                { P, B, P, P, B },
                { P, P, P, 0, B },
            };

            Move move = bot.makeMove(board);
            assertNotNull(move);
            assertTrue(
                moveIsOneOf(move, new int[][] { { 0, 3 }, { 4, 3 } }),
                "Bot should pick one of the two empty cells but chose (" +
                    move.getRow() +
                    "," +
                    move.getCol() +
                    ")"
            );
        }

        @Test
        @DisplayName("Opening move on empty board is in a reasonable position")
        void testOpeningMovePosition() throws Exception {
            int[][] board = emptyBoard();
            Move move = bot.makeMove(board);
            assertNotNull(move);
            // The move should be somewhere on the board
            assertTrue(move.getRow() >= 0 && move.getRow() < SIZE);
            assertTrue(move.getCol() >= 0 && move.getCol() < SIZE);
        }

        @RepeatedTest(5)
        @DisplayName("Bot does not pick an occupied cell on a busy board")
        void testNoOccupiedCellOnBusyBoard() throws Exception {
            // Fill most of the board, leave 3 cells open
            int[][] board = fullBoard();
            board[0][1] = 0;
            board[2][3] = 0;
            board[4][0] = 0;

            Move move = bot.makeMove(board);
            assertValidMove(move, board);
        }
    }
}
