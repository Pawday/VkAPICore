package per.pawday.vkbot;


import org.json.JSONArray;
import org.json.JSONObject;
import per.pawday.jsonFormatter.JsonFormatter;
import per.pawday.jsonFormatter.constants.IndentChars;
import per.pawday.jsonFormatter.constants.IndentsStyles;
import per.pawday.jsonFormatter.exceptions.JsonFormatterException;
import per.pawday.vkbot.console.ConsoleColors;
import per.pawday.vkbot.vk.VkRequester;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;


class Main
{
    private static JsonFormatter formatter;

    static
    {
        try
        {
            formatter = new JsonFormatter(IndentsStyles.ERIC_ALLMANS_STYLE, IndentChars.TAB,1);
        } catch (JsonFormatterException e)
        {
            // i used embedded constants
        }
    }

    public static void main(String[] args)
    {
        if (args.length == 1 && args[0].equals("init"))
        {
            try
            {
                Configs.init();
            }
            catch (IOException e)
            {
                System.out.println(ConsoleColors.RED + "Возникла проблемма при сздании файла configs" + File.separator + "token.json" + ConsoleColors.RESET);
            }
            System.out.println(ConsoleColors.GREEN + "Выполните настройку конфигурационных файлов." + ConsoleColors.RESET);
            System.exit(0);
        }

        Configs.inited = true;


        //confirm group token
        {
            String groupToken = Configs.tokens.getGroupToken();

            VkRequester requester = new VkRequester("5.95",groupToken);
            JSONObject res = null;
            try
            {
                res = requester.post("groups.getTokenPermissions",null);
            }
            catch (IOException e)
            {
                System.out.println(ConsoleColors.RED + "Проблемма с доступом к серверу VK" + ConsoleColors.RESET);
                System.exit(-1);
            }

            if (res.has("error"))
            {
                System.out.println(ConsoleColors.YELLOW + "Group tokens confirmation failed: " + ConsoleColors.RED + ((JSONObject) res.get("error")).get("error_msg") + ConsoleColors.RESET);
                System.exit(-1);
            }
            requester = null;

        }
        //confirm admin token
        {
            try
            {

                String groupToken = Configs.tokens.getGroupToken();
                String adminToken = Configs.tokens.getGroupToken();

                VkRequester requester = new VkRequester("5.95", groupToken);

                HashMap<String,String> params = new HashMap();


                params.put("group_itd", Configs.tokens.getGroupId());
                System.out.println(Configs.tokens.getGroupId());
                int groupId = 0;

                groupId = ((JSONObject) ((JSONArray) requester.post("groups.getById", params).get("response")).get(0)).getInt("id");
                System.out.println(groupId);
                params.clear();
                params.put("group_id", String.valueOf(groupId));
//                params.put("filter", "managers");
                params.put("fields", "name");

                JSONObject res = requester.post("groups.getMembers", params);
                System.out.println(res);
                if (!res.has("response"))
                {
                    System.out.println(ConsoleColors.RED + "Токен от группы не подходит к group_id (Файл configs" + File.separator + "token.json)" + ConsoleColors.RESET);
                    System.exit(-1);
                }
                res = res.getJSONObject("response");
                JSONArray array = res.getJSONArray("items");

                for (int i = 0; i < array.length(); i++)
                {
                    System.out.println(i + 1 + ". " + ((JSONObject) array.get(i)).getString("first_name"));
                }

            }
            catch (IOException exception)
            {
                System.out.println(ConsoleColors.RED + "Проблемма с доступом к серверу VK" + ConsoleColors.RESET);
                System.exit(-1);
            }
        }



    }
}