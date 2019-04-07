package per.pawday.vkbot;





import per.pawday.vkbot.console.ConsoleColors;
import java.io.IOException;


class Main
{
    public static void main(String[] args) throws IOException
    {
        if (args.length == 1 && args[0].equals("init"))
        {
            Configs.returnFiles();
            System.out.println(ConsoleColors.GREEN + "Make a setting up config files now." + ConsoleColors.RESET);
            System.exit(0);
        }

    }
}
