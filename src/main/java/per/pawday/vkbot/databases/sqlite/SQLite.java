package per.pawday.vkbot.databases.sqlite;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLite
{

    public Connection connection;
    public boolean status = false;

    private File dir = new File("databases");
    private File file = new File("databases/database.db");

    public SQLite()
    {
        if ( ! dir.exists())
        {
            dir.mkdir();
        }

        this.connect();
        this.close();
    }

    public void connect()
    {
        if (!this.status)
        {
            try
            {
                Class.forName("org.sqlite.JDBC");
                this.connection = DriverManager.getConnection("jdbc:sqlite:".concat(file.getPath()));
                this.status = true;
            }
            catch (ClassNotFoundException e)
            {}
            catch (SQLException e)
            {}
        } else
        {
            this.close();
            this.connect();
        }
    }


    public void close()
    {
        if (this.status)
        {
            try
            {
                this.connection.close();
            }
            catch (SQLException e)
            {}

            this.status = false;
        }
    }
}
