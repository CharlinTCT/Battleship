import java.util.Scanner;

public class Battleship {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Battleship!");

        char[][] playerOneShipBoard = createEmptyBoard();
        char[][] playerTwoShipBoard = createEmptyBoard();
        char[][] playerOneTargetBoard = createEmptyBoard();
        char[][] playerTwoTargetBoard = createEmptyBoard();

        System.out.println("PLAYER 1, ENTER YOUR SHIPS' COORDINATES.");
        enterShipCoordinates(1, playerOneShipBoard, scanner);
        printBattleShip(playerOneShipBoard);
        System.out.println();
        clearConsole();

        System.out.println("PLAYER 2, ENTER YOUR SHIPS' COORDINATES.");
        enterShipCoordinates(2, playerTwoShipBoard, scanner);
        printBattleShip(playerTwoShipBoard);
        System.out.println();
        clearConsole();

        int currentPlayer = 1;
        boolean gameWon = false;

        while (!gameWon) {
            int opponentPlayer = currentPlayer == 1 ? 2 : 1;

            System.out.printf("Player %d, enter hit row/col:%n", currentPlayer);
            int[] coordinates = enterFireCoordinates(scanner, playerOneTargetBoard, playerTwoTargetBoard,
                    currentPlayer);
            int row = coordinates[0];
            int column = coordinates[1];

            if (currentPlayer == 1) {
                checkFireResult(row, column, playerTwoShipBoard, playerOneTargetBoard, currentPlayer, opponentPlayer);
                gameWon = checkGameOver(playerTwoShipBoard);
            } else {
                checkFireResult(row, column, playerOneShipBoard, playerTwoTargetBoard, currentPlayer, opponentPlayer);
                gameWon = checkGameOver(playerOneShipBoard);
            }

            printBattleShip(currentPlayer == 1 ? playerOneTargetBoard : playerTwoTargetBoard);

            clearConsole();
            System.out.println();

            if (gameWon) {

                System.out.printf("PLAYER %d WINS! YOU SUNK ALL OF YOUR OPPONENT'S SHIPS!%n", currentPlayer);
                System.out.println("\n");
                System.out.println("Final boards:");
                char[][] finalBoardPlayerOne = combineBoards(playerOneShipBoard, playerTwoTargetBoard);
                printBattleShip(finalBoardPlayerOne);
                System.out.println("\n");
                char[][] finalBoardPlayerTwo = combineBoards(playerTwoShipBoard, playerOneTargetBoard);
                printBattleShip(finalBoardPlayerTwo);
            }

            currentPlayer = opponentPlayer;
        }

        scanner.close();
    }

    public static void enterShipCoordinates(int player, char[][] shipBoard, Scanner scanner) {
        for (int i = 0; i < 5; i++) {
            int row, column;
            boolean validCoordinates;
            boolean validCoordinatesSpace;

            do {
                System.out.printf("Enter ship %d location:", i + 1);
                System.out.println();
                row = scanner.nextInt();
                column = scanner.nextInt();
                validCoordinates = validateCoordinates(row, column);
                validCoordinatesSpace = validateEmptySpace(row, column, shipBoard);
                if (!validCoordinates) {
                    System.out.println("Invalid coordinates. Choose different coordinates.");
                }
                if (!validCoordinatesSpace) {
                    System.out.println("You already have a ship there. Choose different coordinates.");
                }
            } while (!validCoordinates || !validCoordinatesSpace);

            shipBoard[row][column] = '@';
        }
    }

    public static boolean validateCoordinates(int row, int column) {
        return row >= 0 && row < 5 && column >= 0 && column < 5;
    }

    public static boolean validateEmptySpace(int row, int column, char[][] board) {
        return board[row][column] != '@';
    }

    public static boolean validateFireCoordinates(int row, int column, char[][] board) {
        return board[row][column] != 'X' && board[row][column] != 'O';
    }

    public static int[] enterFireCoordinates(Scanner scanner, char[][] playerOneTargetBoard,
            char[][] playerTwoTargetBoard, int currentPlayer) {
        int[] coordinates;
        boolean validCoordinates;
        boolean validFiredCoordinates;

        do {
            int row = scanner.nextInt();
            int column = scanner.nextInt();
            validCoordinates = validateCoordinates(row, column) && validateEmptySpace(row, column,
                    currentPlayer == 1 ? playerOneTargetBoard : playerTwoTargetBoard);
            validFiredCoordinates = validateCoordinates(row, column) && validateFireCoordinates(row, column,
                    currentPlayer == 1 ? playerOneTargetBoard : playerTwoTargetBoard);
            if (!validCoordinates) {
                System.out.println("Invalid coordinates. Choose different coordinates.");
            } else if (!validFiredCoordinates) {
                System.out.println("You already fired on this spot. Choose different coordinates.");
            } else {
                coordinates = new int[] { row, column };
                return coordinates;
            }
        } while (true);
    }

    public static void checkFireResult(int row, int column, char[][] shipBoard, char[][] targetBoard, int currentPlayer,
            int opponentPlayer) {
        if (shipBoard[row][column] == '@') {
            shipBoard[row][column] = 'X';
            targetBoard[row][column] = 'X';
            System.out.printf("Player %d HIT Player %d's SHIP!%n", currentPlayer, opponentPlayer);
        } else {
            targetBoard[row][column] = 'O';
            System.out.printf("Player %d MISSED!%n", currentPlayer);
        }
    }

    public static boolean checkGameOver(char[][] shipBoard) {
        for (int i = 0; i < shipBoard.length; i++) {
            for (int j = 0; j < shipBoard[i].length; j++) {
                if (shipBoard[i][j] == '@') {
                    return false;
                }
            }
        }
        return true;
    }

    public static char[][] createEmptyBoard() {
        char[][] board = new char[5][5];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = '-';
            }
        }
        return board;
    }

    // Use this method to print game boards to the console.
    private static void printBattleShip(char[][] player) {
        System.out.print("  ");
        for (int row = -1; row < 5; row++) {
            if (row > -1) {
                System.out.print(row + " ");
            }
            for (int column = 0; column < 5; column++) {
                if (row == -1) {
                    System.out.print(column + " ");
                } else {
                    System.out.print(player[row][column] + " ");
                }
            }
            System.out.println("");
        }
    }

    public static char[][] combineBoards(char[][] shipBoard, char[][] targetBoard) {
        char[][] combinedBoard = new char[5][5];

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (targetBoard[i][j] == 'X' || targetBoard[i][j] == 'O') {
                    combinedBoard[i][j] = targetBoard[i][j];
                } else {
                    combinedBoard[i][j] = shipBoard[i][j];
                }
            }
        }

        return combinedBoard;
    }

    public static void clearConsole() {
        // Clear console by printing multiple empty lines
        for (int i = 0; i < 100; i++) {
            System.out.println("\n");
        }
    }
}
