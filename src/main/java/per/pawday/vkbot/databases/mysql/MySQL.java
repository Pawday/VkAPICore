package per.pawday.vkbot.databases.mysql;

import per.pawday.vkbot.configs.Advanced;
import per.pawday.vkbot.configs.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL
{
    public Connection connection;
    public boolean status = false;

    private String host;
    private String user;
    private String password;
    private String database;
    private String port;

    public MySQL()
{
    Database dbConfigs = new Database();
    Advanced advancedConfigs = new Advanced();

    this.host = dbConfigs.MySQL.connection.host;
    this.user = dbConfigs.MySQL.connection.user;
    this.password = dbConfigs.MySQL.connection.password;
    this.database = dbConfigs.MySQL.connection.database;
    this.port = advancedConfigs.database.mysql.port;


}

    public void сonnect()
    {
        if (!this.status)
        {
            try
            {
                Class.forName("com.mysql.cj.jdbc.Driver");
                this.connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", this.user, this.password);

                this.status = true;
            }
            catch (ClassNotFoundException e)
            {
                System.out.println("Не найден драйвер MySQL");
                System.exit(-1);
            }
            catch (SQLException e)
            {
                System.out.println("Не удалось подключиться к базе данных MySQL");
                System.exit(-1);
            }
        } else
        {
            this.close();
            this.сonnect();
        }

    }

    public void close()
    {
        if (this.status)
        {
            try
            {
                this.connection.close();
                this.status = false;
            }
            catch (SQLException e)
            {}
        }
    }
}
