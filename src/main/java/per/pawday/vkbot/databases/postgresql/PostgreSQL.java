package per.pawday.vkbot.databases.postgresql;

import per.pawday.vkbot.configs.Advanced;
import per.pawday.vkbot.configs.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreSQL
{
    public Connection connection;
    public boolean status = false;

    private String host;
    private String user;
    private String password;
    private String database;
    private String port;

    public PostgreSQL()
    {
        Database dbConfigs = new Database();
        Advanced advancedConfigs = new Advanced();

        this.host = dbConfigs.PostgreSQL.connection.host;
        this.user = dbConfigs.PostgreSQL.connection.user;
        this.password = dbConfigs.PostgreSQL.connection.password;
        this.database = dbConfigs.PostgreSQL.connection.database;
        this.port = advancedConfigs.database.postgresql.port;





    }

    public void сonnect()
    {
        if (!this.status)
        {
            try
            {
                Class.forName("org.postgresql.Driver");
                this.connection = DriverManager.getConnection("jdbc:postgresql://" + this.host + ":" + this.port + "/" + this.database, this.user, this.password);
                this.status = true;
            }
            catch (ClassNotFoundException e)
            {
                System.out.println("Не найден драйвер PostgreSQL");
                System.exit(-1);

            }
            catch (SQLException e)
            {
                System.out.println("Не удалось подключиться к базе данных PostgreSQL");
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
