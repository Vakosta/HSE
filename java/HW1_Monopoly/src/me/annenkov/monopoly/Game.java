package me.annenkov.monopoly;

import me.annenkov.monopoly.model.Action;
import me.annenkov.monopoly.model.Board;
import me.annenkov.monopoly.model.Coordinates;
import me.annenkov.monopoly.model.cell.Cell;
import me.annenkov.monopoly.model.player.Bot;
import me.annenkov.monopoly.model.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Класс игрового процесса.
 *
 * @author <a href="mailto:vaannenkov@edu.hse.ru">Vladislav Annenkov</a>
 */
public class Game {
    private final Board board;
    private final List<Player> players;

    private boolean isGameOver;

    /**
     * Конструктор для инициализации объекта Game. Определяется язык игры,
     * заполняются игроки, генерируется игровая доска.
     *
     * @param width  Ширина игрового поля.
     * @param height Высота игрового поля.
     * @param money  Количество денег у игроков.
     */
    public Game(int width, int height, int money) {
        ConsoleHelper.clearConsole();
        ConsoleHelper.writeStringLine("1 — Русский | 2 — English.");
        ConsoleHelper.writeString("1 or 2: ");
        int answer;
        while (true) {
            answer = ConsoleHelper.readNumber();
            if (answer == 1 || answer == 2)
                break;

            ConsoleHelper.writeStringLine("1 or 2: ");
        }
        if (answer == 1)
            ConsoleHelper.setLocale(new Locale("ru"));
        else
            ConsoleHelper.setLocale(new Locale("en"));
        ConsoleHelper.clearConsole();

        this.players = new ArrayList<>();
        this.players.add(new Player(money));
        this.players.add(new Bot(money));
        Collections.shuffle(this.players);
        askPlayerNames();
        printPlayersString();

        this.board = new Board(width, height);

        this.isGameOver = false;
    }

    /**
     * Метод спрашивает имена игроков.
     */
    private void askPlayerNames() {
        ConsoleHelper.writeStringLine(ConsoleHelper.getMessage("game_start"));

        int counter = 0;
        for (Player player : players.stream().filter(p -> !p.isBot()).collect(Collectors.toList())) {
            counter++;

            while (true) {
                ConsoleHelper.writeFormattedString(ConsoleHelper.getMessage("introduce_player") + ' ', counter);
                String name = ConsoleHelper.readString();
                if (name.equals("")) continue;

                player.setName(name);
                ConsoleHelper.writeStringLine("");
                break;
            }
        }
    }

    /**
     * Метод отображает очерёдность ходов игроков.
     */
    private void printPlayersString() {
        ConsoleHelper.writeStringLine(ConsoleHelper.getMessage("order_of_moves"));

        for (int i = 0; i < players.size(); i++) {
            ConsoleHelper.writeFormattedString("%s. %s\n", Integer.toString(i + 1), players.get(i).toString());
        }

        ConsoleHelper.readString();
    }

    /**
     * Метод для действия определённого игрока.
     *
     * @param player Игрок, который ходит.
     */
    private void move(Player player) {
        Cell cell = board.getCellByIndex(player.getPosition());
        ConsoleHelper.writeStringLine(cell.toString());

        Action action = cell.getAction(player);
        if (!player.applyAction(action, board.getLength())) {
            ConsoleHelper.writeFormattedString(ConsoleHelper.getMessage("lost_money") + "\n", player.getName());
            isGameOver = true;
            return;
        }

        if (action.getType() == Action.ActionType.MOVE) {
            player.addPosition((int) action.getValue(), board.getLength());
            move(player);
            return;
        }

        ConsoleHelper.readString();
    }

    /**
     * Печать в консоль в начале хода.
     *
     * @param player Игрок, который ходит.
     */
    private void beforeMove(Player player) {
        ConsoleHelper.clearConsole();
        ConsoleHelper.writeStringLine(board.toString(player));
        ConsoleHelper.writeFormattedString(ConsoleHelper.getMessage("player_start") + '\n',
                player.getName());
        int cubesSum = player.throwCubes();
        player.addPosition(cubesSum, board.getLength());
        ConsoleHelper.readString();
    }

    /**
     * Печать в консоль в конце хода.
     *
     * @param player         Игрок, который ходит.
     * @param oldPosition    Старая позиция игрока.
     * @param oldCoordinates Старые координаты игрока.
     * @param oldMoney       Старое количество денег игрока.
     */
    private void afterMove(Player player, int oldPosition, Coordinates oldCoordinates, int oldMoney) {
        String header = String.format(ConsoleHelper.getMessage("player_end"), player.getName());
        ConsoleHelper.writeStringLine(header);
        ConsoleHelper.writeFormattedString(
                "=== %s: %" + (header.length() - 17) + "s ===\n",
                ConsoleHelper.getMessage("position"),
                oldPosition + " -> " + (player.getPosition() + 1)
        );
        ConsoleHelper.writeFormattedString(
                "=== %s: %" + (header.length() - 20) + "s ===\n",
                ConsoleHelper.getMessage("coordinates"),
                oldCoordinates + " -> " + player.getCoordinates(board.getWidth(), board.getHeight())
        );
        ConsoleHelper.writeFormattedString(
                "=== %s: %" + (header.length() - 26) + "s ===\n",
                ConsoleHelper.getMessage("money_count"),
                oldMoney + " -> " + player.getMoney()
        );
        ConsoleHelper.readString();
    }

    /**
     * Метод процесса игры.
     */
    public void run() {
        ConsoleHelper.readString();
        ConsoleHelper.clearConsole();

        ConsoleHelper.writeStringLine(ConsoleHelper.getMessage("generated_board"));
        ConsoleHelper.writeStringLine(board.toString(players.get(0)));
        ConsoleHelper.readString();

        int moveNumber = 0;
        while (!isGameOver) {
            Player player = players.get(moveNumber);
            int oldPosition = player.getPosition() + 1;
            Coordinates oldCoordinates = player.getCoordinates(board.getWidth(), board.getHeight());
            int oldMoney = player.getMoney();

            // Начало хода.
            beforeMove(player);

            // Действие игрока.
            move(player);

            // Конец хода.
            if (!isGameOver)
                afterMove(player, oldPosition, oldCoordinates, oldMoney);

            moveNumber++;
            moveNumber %= 2;
        }
    }
}
