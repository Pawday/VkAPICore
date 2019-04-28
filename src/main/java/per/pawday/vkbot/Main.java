package per.pawday.vkbot;


import per.pawday.vkbot.console.ConsoleColors;
import per.pawday.vkbot.eventHandler.EventHandler;
import per.pawday.vkbot.vk.VkRequester;

import java.io.IOException;


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


        //confirm groups tokens
        {
            String[] groupsTokens = Configs.configs.tokens.getGroupsTokens();

            for (int i = 0; i < groupsTokens.length; i++)
            {
                VkRequester requester = new VkRequester("5.95",groupsTokens[i]);
                requester.post("groups.getTokenPermissions",null);
            }
        }

        //Thread handler = new Thread(new EventHandler());

    }
}