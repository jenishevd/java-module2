import java.util.*;

class Ship {
    private List<String> positions;
    private int hitCount;

    public Ship(int size) {
        this.positions = new ArrayList<>(size);
        this.hitCount = 0;
    }

    public void placeShip(List<String> positions) {
        this.positions.addAll(positions);
    }

    public boolean checkHit(String shot) {
        return positions.contains(shot);
    }

    public void registerHit() {
        hitCount++;
    }

    public boolean isSunk() {
        return hitCount == positions.size();
    }
}

class Board {
    public static final int SIZE = 8;
    private char[][] grid;

    public Board() {
        this.grid = new char[SIZE][SIZE];
        resetBoard();
    }

    public void resetBoard() {
        for (int i = 0; i < SIZE; i++) {
            Arrays.fill(grid[i], ' ');
        }
    }

    public void printBoard() {
        System.out.println();
        System.out.print("  ");
        for (int i = 0; i < SIZE; i++) {
            System.out.print((char) ('A' + i) + " ");
        }
        System.out.println();

        for (int i = 0; i < SIZE; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < SIZE; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void markHit(int row, int col) {
        grid[row][col] = 'U';
    }

    public void markMiss(int row, int col) {
        grid[row][col] = 'o';
    }

    public void markSunk(int row, int col) {
        grid[row][col] = 'X';

        // Mark surrounding cells as 'o'
        if (row > 0 && grid[row - 1][col] != 'X') {
            grid[row - 1][col] = 'o';
        }
        if (row < SIZE - 1 && grid[row + 1][col] != 'X') {
            grid[row + 1][col] = 'o';
        }
        if (col > 0 && grid[row][col - 1] != 'X') {
            grid[row][col - 1] = 'o';
        }
        if (col < SIZE - 1 && grid[row][col + 1] != 'X') {
            grid[row][col + 1] = 'o';
        }
    }
}

class Game {
    private List<Ship> ships;
    private Board board;
    private Set<Long> results;

    public Game() {
        this.ships = new ArrayList<>();
        this.board = new Board();
        this.results = new HashSet<>();
        createShips();
        placeShips();
    }

    public void play() {
        Scanner scanner = new Scanner(System.in);

        board.resetBoard();
        boolean gameOver = false;
        long startTime = System.currentTimeMillis();

        while (!gameOver) {
            System.out.println("\n--- Игровое поле ---");
            board.printBoard();
            System.out.print("Куда стреляем (например, A2): ");
            String shot = scanner.nextLine().toUpperCase();

            if (shot.equals("EXIT")) {
                gameOver = true;
            } else if (shot.length() == 2 && Character.isLetter(shot.charAt(0)) && Character.isDigit(shot.charAt(1))) {
                int row = shot.charAt(1) - '1';
                int col = shot.charAt(0) - 'A';

                if (row >= 0 && row < Board.SIZE && col >= 0 && col < Board.SIZE) {
                    Ship hitShip = null;

                    for (Ship ship : ships) {
                        if (ship.checkHit(shot)) {
                            hitShip = ship;
                            break;
                        }
                    }

                    if (hitShip != null) {
                        System.out.println("Попадание!");
                        hitShip.registerHit();
                        if (hitShip.isSunk()) {
                            System.out.println("Корабль потоплен!");
                            board.markSunk(row, col);
                            ships.remove(hitShip);
                            if (ships.isEmpty()) {
                                gameOver = true;
                                long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
                                results.add(elapsedTime);
                                System.out.println("\nПоздравляю! Вы победили!");
                                System.out.println("Прошло времени: " + elapsedTime + " сек");
                            }
                        } else {
                            board.markHit(row, col);
                        }
                    } else {
                        System.out.println("Мимо!");
                        board.markMiss(row, col);
                    }
                } else {
                    System.out.println("Неверные координаты. Попробуйте снова.");
                }
            } else {
                System.out.println("Неверный формат ввода. Попробуйте снова.");
            }
        }

        System.out.println("\nИгра завершена.");
    }

    private void createShips() {
        ships.add(new Ship(1));
        ships.add(new Ship(2));
        ships.add(new Ship(2));
    }

    private void placeShips() {
        List<String> ship1Positions = Arrays.asList("A1");
        List<String> ship2Positions = Arrays.asList("C3", "C4");
        List<String> ship3Positions = Arrays.asList("F7", "G7");

        ships.get(0).placeShip(ship1Positions);
        ships.get(1).placeShip(ship2Positions);
        ships.get(2).placeShip(ship3Positions);
    }

    public void showResults() {
        System.out.println("\n--- Топ 3 результатов ---");
        List<Long> sortedResults = new ArrayList<>(results);
        Collections.sort(sortedResults);
        int count = Math.min(3, sortedResults.size());
        for (int i = 0; i < count; i++) {
            System.out.println((i + 1) + ". " + sortedResults.get(i) + " сек");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Game game = new Game();

        while (true) {
            System.out.println("\n--- Морской бой ---");
            System.out.println("1. Новая игра");
            System.out.println("2. Результаты");
            System.out.println("3. Выход");
            System.out.print("Выберите действие: ");
            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                game.play();
            } else if (choice.equals("2")) {
                game.showResults();
            } else if (choice.equals("3")) {
                break;
            } else {
                System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }

        System.out.println("Программа завершена.");
    }
}
