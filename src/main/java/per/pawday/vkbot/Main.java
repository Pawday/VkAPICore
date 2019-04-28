package per.pawday.vkbot;


import per.pawday.vkbot.console.ConsoleColors;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;


class Main
{
    public static void main(String[] args) throws IOException
    {
        if (args.length == 1 && args[0].equals("init"))
        {
            Configs.removeFiles();
            Configs.init();
            System.out.println(ConsoleColors.GREEN + "Выполните настройку конфигурационных файлов." + ConsoleColors.RESET);
            System.exit(0);
        }

        Configs.init();


        Calendar calendar = Calendar.getInstance();

        System.out.println(calendar.getFirstDayOfWeek());




    }
}
