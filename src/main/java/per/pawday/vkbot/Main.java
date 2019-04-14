package per.pawday.vkbot;





import per.pawday.vkbot.console.ConsoleColors;
import java.io.IOException;


class Main
{
    public static void main(String[] args) throws IOException
    {
        if (args.length == 1 && args[0].equals("init"))
        {
            Configs.removeFiles();
            Configs.returnFiles();
            System.out.println(ConsoleColors.GREEN + "Выполните настройку конфигурационных файлов." + ConsoleColors.RESET);
            System.exit(0);
        }
        Configs.init();
        Configs.removeFiles();

        Configs.returnFiles();


    }
}
