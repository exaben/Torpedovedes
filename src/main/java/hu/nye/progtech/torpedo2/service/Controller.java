package hu.nye.progtech.torpedo2.service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import hu.nye.progtech.torpedo2.model.Field;
import hu.nye.progtech.torpedo2.model.Ocean;
import hu.nye.progtech.torpedo2.pers.InsertApp;
import hu.nye.progtech.torpedo2.pers.SelectApp;
import hu.nye.progtech.torpedo2.pers.UpdateApp;
import hu.nye.progtech.torpedo2.ui.Ui;
import org.springframework.stereotype.Repository;

@Repository
public class Controller {
    /**
     * Mezok
     */
    private Player player1;
    private Player player2;
    private Scanner reader = new Scanner(System.in);
    private boolean turnOne = true;


    /**
     * Jatekosok konf
     */
    public void setupPlayers() {
        while (player1 == null) player1 = setupPlayer("1");
        while (player2 == null) player2 = setupPlayer("2");
    }

    /**
     * Nev bekerese
     *
     * @param id separate player to the user
     * @return created player
     */
    private Player setupPlayer(String id) {
        Ui.printf("%s. Jatekos: ", id);
        String name = reader.next();
        if (name.isEmpty()) return null;
        return new Player(name);
    }

    /**
     * A ket jatekosnak a palya felallitasa
     */
    public void setupBoards() {
        setupBoard(player1);
        setupBoard(player2);
    }

    /**
     * Jatekosnak a palya beallitasa
     *
     * @param player to setup battlefield
     */
    private void setupBoard(Player player) {
        Ui.println("========================================");
        Ui.printf(Ui.ANSI_YELLOW + "[%s] Tabla keszitese: %n" + Ui.ANSI_RESET, player.getName());
        player.initBoard();
        printBoard(player, false);
        setupShips(player);
        Ui.clear();
        sleep(2000);
    }

    /**
     * Tabla kiiratasa
     *
     * @param player board to be printed
     * @param enemy  view
     */
    private void printBoard(Player player, boolean enemy) {
        Field[][] board = player.getBoard();
        int sizeX = board.length;
        for (int i = 0; i <= sizeX; i++) {
            Ui.print(i == 0 ? "[ ]" : "[" + i + "]");
        }
        Ui.println("");
        for (int i = 0; i < sizeX; i++) {
            char c = (char) ((int) 'a' + i);
            Ui.print("[" + c + "]");
            int sizeY = board[i].length;
            for (int j = 0; j < sizeY; j++) {
                //Ui.print("["+ i + "|" + j +"]");
                Field field = board[i][j];
                if (enemy) {
                    printFieldEnemy(field);
                } else {
                    printField(field);
                }
            }
            Ui.println("");
        }
    }

    /**
     * Mezok kiiratasa
     *
     * @param field to be printed
     */
    private void printField(Field field) {
        if (field instanceof Ocean) {
            Ui.print(Ui.ANSI_BLUE + "[~]" + Ui.ANSI_RESET);
        } else if (field instanceof ShipComponent) {
            Ui.print(Ui.ANSI_GREEN + "[*]" + Ui.ANSI_RESET);
        }
    }

    /**
     * Ellenseg mezeinek mutatasa
     *
     * @param field to be printed
     */
    private void printFieldEnemy(Field field) {
        if (field instanceof Ocean) {
            if (field.getFieldState().isBombed()) {
                Ui.print(Ui.ANSI_CYAN + "[X]" + Ui.ANSI_RESET);
            } else {
                Ui.print(Ui.ANSI_BLUE + "[~]" + Ui.ANSI_RESET);
            }
        } else if (field instanceof ShipComponent) {
            if (field.getFieldState().isBombed()) {
                Ui.print(Ui.ANSI_RED + "[+]" + Ui.ANSI_RESET);
            } else {
                Ui.print(Ui.ANSI_BLUE + "[~]" + Ui.ANSI_RESET);
            }
        }
    }

    /**
     * Hajok felallitasa
     *
     * @param player ships to be placed
     */
    private void setupShips(Player player) {
        Ship[] ships = player.getShips();
        printShips(ships);
        for (Ship ship : ships) {
            setupShip(player, ship);
            printBoard(player, false);
        }
    }

    /**
     * Elhelyezese
     *
     * @param player
     * @param ship
     */
    private void setupShip(Player player, Ship ship) {
        Ui.print("Helyezd el " + ship.getName() + " (" + ship.getLength() + ") :");
        String inputPosition = reader.next();
        int[] pos = inputToPosition(inputPosition);
        if (!isPositionInRange(pos, player.getBoard())) {
            //Hibas pozicio bevitel, ujra kell kerdezni
            Ui.println(Ui.ANSI_YELLOW + "Nem megfelelo pozicio, probald ujra" + Ui.ANSI_RESET);
            setupShip(player, ship);
            return;
        }
        List<String> directions = new ArrayList<>();
        if (player.canPlaceShip(pos[0], pos[1], Board.UP, ship.getLength())) {
            directions.add("Fel");
        }
        if (player.canPlaceShip(pos[0], pos[1], Board.RIGHT, ship.getLength())) {
            directions.add("Jobb");
        }
        if (player.canPlaceShip(pos[0], pos[1], Board.DOWN, ship.getLength())) {
            directions.add("Le");
        }
        if (player.canPlaceShip(pos[0], pos[1], Board.LEFT, ship.getLength())) {
            directions.add("Bal");
        }

        boolean isPlaced = false;
        if (directions.size() > 0) {
            while (!isPlaced) {
                for (String dir : directions) {
                    Ui.printf("- %s %n", dir);
                }
                Ui.print("Irany: ");
                String inputDirection = reader.next();
                if (player.canPlaceShip(pos[0], pos[1], inputToDirection(inputDirection), ship.getLength())) {
                    //Hajo elhelyezese
                    player.placeShip(pos[0], pos[1], inputToDirection(inputDirection), ship);
                    Ui.println(Ui.ANSI_GREEN + "Ugyes vagy" + Ui.ANSI_RESET);
                    isPlaced = true;
                } else {
                    Ui.println(Ui.ANSI_RED + "Ervenytelen bevitel" + Ui.ANSI_RESET);
                }
            }
        } else {
            Ui.println(Ui.ANSI_YELLOW + "Nincs itt hely" + Ui.ANSI_RESET);
            setupShip(player, ship);
        }
    }

    /**
     * Elerheto hajok kiirasa
     *
     * @param ships
     */
    private void printShips(Ship[] ships) {
        for (Ship ship : ships) {
            Ui.println(" (" + ship.getLength() + ") " + ship.getName());
        }
    }

    /**
     * Beviteli adat atalakitasa poziciora
     *
     * @param input
     * @return position
     */
    private int[] inputToPosition(String input) {
        int[] pos = {-1, -1};
        char posY = input.charAt(0);
        String posX = input.substring(1);
        pos[0] = (int) posY - (int) 'a';
        try {
            pos[1] = Integer.parseInt(posX) - 1;
        } catch (NumberFormatException ex) {
            Ui.println(Ui.ANSI_RED + "A kovetkezo a megfelelo beviteli mod: " + Ui.ANSI_YELLOW + "[a-j][1-10]" + Ui.ANSI_RESET);
        }
        return pos;
    }

    /**
     * Itt ellenorizzuk, h a felhasznalo esetleg a palyan kivuli koordinatat adott e meg
     *
     * @param pos   position
     * @param board
     * @return if is within range
     */
    private boolean isPositionInRange(int[] pos, Field[][] board) {
        int y = pos[0];
        int x = pos[1];
        if (y >= 0 && y < board.length
                && x >= 0 && x < board[y].length) return true;
        return false;
    }

    /**
     * Hajok elhelyezesenek iranyai
     *
     * @param input
     * @return direction
     */
    private int inputToDirection(String input) {
        switch (input.toLowerCase()) {
            case "f":
            case "fel":
                return Board.UP;
            case "j":
            case "jobb":
                return Board.RIGHT;
            case "l":
            case "le":
                return Board.DOWN;
            case "b":
            case "bal":
                return Board.LEFT;
            default:
                return -1;
        }
    }

    /**
     * A jatekosok meg elnek-e vagy mar nincs hajojuk azt ellenorizzuk itt
     *
     * @return if is still battling
     */
    public boolean isGameOnGoing() {
        return isPlayerStillLiving(player1) && isPlayerStillLiving(player2);
    }

    /**
     * Egyenekent nezzuk meg, h meg van a jatekosnak lehetosege
     *
     * @param player to be checked
     * @return if player is alive
     */
    private boolean isPlayerStillLiving(Player player) {
        for (Ship ship : player.getShips()) {
            if (ship.getHealth() > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Jatekosok kozotti valtas
     */
    public void play() {
        Ui.println("========================================");
        if (turnOne) {
            printBoard(player2, true);
            play(player1, player2);
            printBoard(player2, true);
        } else {
            printBoard(player1, true);
            play(player2, player1);
            printBoard(player1, true);
        }
        turnOne = !turnOne;
        Ui.clear();
        sleep(2000);
    }

    /**
     * Jatekos sorra kerul
     *
     * @param player
     * @param enemy
     */
    private void play(Player player, Player enemy) {
        Ui.printf(Ui.ANSI_YELLOW + "[%s] Add meg a loves koordinatait: " + Ui.ANSI_RESET, player.getName());
        String inputPosition = reader.next();
        int[] pos = inputToPosition(inputPosition);
        if (isPositionInRange(pos, player.getBoard())) {
            Ui.println(enemy.hit(pos));
        } else {
            Ui.println(Ui.ANSI_YELLOW + "Nem megfelelo koordinata" + Ui.ANSI_RESET);
            play(player, enemy);
        }
        sleep(1000);
    }

    /**
     * Nyertes
     */
    public void kudosToWinner() {
        String message = Ui.ANSI_GREEN + "Remek %s :) !" + Ui.ANSI_RED + " Legyozted %s :(" + Ui.ANSI_RESET;
        SelectApp sapp = new SelectApp();
        UpdateApp uapp = new UpdateApp();
        InsertApp iapp = new InsertApp();
        if (isPlayerStillLiving(player1)) {
            Ui.printf(message, player1.getName(), player2.getName());
            int points = sapp.getNametoPoints(player1.getName());
            if (points == 0)
            {
                iapp.insert(player1.getName(), 1);
            }
            else {
                uapp.update(player1.getName());
            }
        } else if (isPlayerStillLiving(player2)) {
            Ui.printf(message, player2.getName(), player1.getName());
            int points = sapp.getNametoPoints(player2.getName());
            if (points == 0)
            {
                iapp.insert(player2.getName(), 1);
            }
            else {
                uapp.update(player2.getName());
            }
        }
        System.out.println();
        sapp.selectSort();

        sleep(2500);
    }

    /**
     * Varakozas, h folytatodjon
     *
     * @param millis time
     */
    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    /**
     * SQL adatbazis letrehozas, illetve a tabla benne
     */
    public void createNewDatabase() {

        String url = "jdbc:sqlite:/home/bence/java-projects/Torpedo2/highScore.db";

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void createNewTable() {
        // SQLite connection string
        String url = "jdbc:sqlite:/home/bence/java-projects/Torpedo2/highScore.db";

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS hstable (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	name text NOT NULL,\n"
                + "	points integer\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
