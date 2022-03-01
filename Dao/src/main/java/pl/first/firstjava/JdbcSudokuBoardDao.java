package pl.first.firstjava;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdbcSudokuBoardDao implements Dao<SudokuBoard> {

    private final ResourceBundle bundle = ResourceBundle.getBundle("Dao");
    private static final Logger logger =
            LoggerFactory.getLogger(JdbcSudokuBoardDao.class.getName());
    private final PopoutWindow popoutWindow = new PopoutWindow();

    private static final String dbURL = "jdbc:derby://localhost:1527/Sudoku;create=true";
    private static final String tableName = "SudokuBoards";
    private static final String tableFieldsName = "SudokuFields";
    public static Connection connection;
    private String boardName;


    public JdbcSudokuBoardDao(String boardName) {
        this.boardName = boardName;
        try {
            connection = DriverManager.getConnection(dbURL);
            logger.debug(bundle.getString("connection_success"));
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(bundle.getString("connection_fail"));
            // throw new JdbcDaoException(bundle.getString("connectionError"),e);
        }
    }

    public String showAll() throws SQLException {
        connection.setAutoCommit(false);
        StringBuilder string = new StringBuilder();
        string.append(bundle.getString("input_dialog"));
        try (Statement statement = connection.createStatement();
             ResultSet results = statement.executeQuery("select * from " + tableName)) {

            while (results.next()) {
                string.append(System.lineSeparator());
                string.append("boardName: " + results.getString("boardName"));
            }
        } catch (SQLException e) {
            connection.rollback();
            logger.error(bundle.getString("sql_error_read"));
        } finally {
            close();
        }
        return string.toString();
    }

    @Override
    public SudokuBoard read() throws SQLException {
        connection.setAutoCommit(false);
        try (Statement statement = connection.createStatement();
             ResultSet results = statement.executeQuery("select f.col, f.row, f.value "
                     + "from " + tableFieldsName + " f join " + tableName
                     + " b on f.id = b.id where b.boardName = '" + boardName + "'")) {
            SudokuBoard board = new SudokuBoard(9);
            if (!results.next()) {
                logger.error(bundle.getString("sql_error_read"));
                popoutWindow.messageBox(bundle.getString("message_title"),
                        bundle.getString("message_info_read"), Alert.AlertType.WARNING);
                return null;
            }

            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    board.setPosition(results.getInt(1), results.getInt(2), results.getInt(3));
                    results.next();
                }
            }
            return board;
        } catch (SQLException e) {
                connection.rollback();
                logger.error(bundle.getString("sql_error_read"));
                return null;
        } finally {
                close();
            }
    }

    @Override
    public void write(SudokuBoard board) throws SQLException {
        connection.setAutoCommit(false);
        try (Statement statement = connection.createStatement();
        Statement statement2 = connection.createStatement()) {
            statement.execute("create table " + tableName
                    + " (id integer not null generated always as identity, "
                    + "boardName varchar(30) not null unique, primary key (id))");
            statement2.execute("create table " + tableFieldsName
        + " (id int not null, col int not null, row int not null, value int not null, "
                    + "foreign key (id) references " + tableName + "(id))");
            logger.debug(bundle.getString("sql_create_tab"));
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            logger.warn(bundle.getString("sql_error_create_tab"));
        }
        try (Statement statement = connection.createStatement()) {
            statement.execute("insert into " + tableName
                    + "(boardName) values " + "('" + boardName + "')");
        } catch (SQLException e) {
            connection.rollback();
            logger.error(bundle.getString("sql_error_insert"));
            popoutWindow.messageBox(bundle.getString("message_title"),
                    bundle.getString("message_info"), Alert.AlertType.WARNING);
            return;
        }

        try (Statement statement2 = connection.createStatement();
             Statement statement3 = connection.createStatement();
             ResultSet results = statement2.executeQuery("select * from "
                     + tableName + " where boardName like '" + boardName + "'")) {
            StringBuilder boardToWrite = new StringBuilder();
            String id = null;
            while (results.next()) {
                id = String.valueOf(results.getInt("id"));
            }
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    boardToWrite.append("(").append(i).append(",").append(j).append(",")
                            .append(board.getPosition(i, j)).append(",")
                            .append(id).append(")");
                    if (i != 8 || j != 8) {
                        boardToWrite.append(",\n");
                    }
                }
            }
            statement3.execute("insert into " + tableFieldsName
                    + "(col, row, value, id) values " + boardToWrite);
            connection.commit();
            connection.setAutoCommit(true);
            logger.info(bundle.getString("sql_insert"));
        } catch (SQLException e) {
            connection.rollback();
            logger.error(bundle.getString("sql_error_insert"));
        } finally {
            close();
        }
    }

    @Override
    public void close() {
        try {
            if (connection != null) {
                connection.close();
                logger.debug(bundle.getString("sql_connection_close"));
            }
        } catch (SQLException e) {
            logger.error(bundle.getString("sql_error_close"));
        }
    }
}