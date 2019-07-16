package per.pawday.vkbot;


import org.json.JSONArray;
import org.json.JSONObject;
import per.pawday.jsonFormatter.JsonFormatter;
import per.pawday.jsonFormatter.constants.IndentChars;
import per.pawday.jsonFormatter.constants.IndentsStyles;
import per.pawday.jsonFormatter.exceptions.JsonFormatterException;
import per.pawday.vkbot.console.ConsoleColors;
import per.pawday.vkbot.handlers.UserLongPollHandler;
import per.pawday.vkbot.vk.VkRequester;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Main
{
    public static JsonFormatter formatter;
    public static final String getApiVersion()
    {
        return Configs.getApiVersion();
    }

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
                Configs.initFiles();
            }
            catch (IOException e)
            {
                System.out.println(ConsoleColors.RED + "Возникла проблемма при создании файла configs" + File.separator + "token.json" + ConsoleColors.RESET);
                e.printStackTrace();
                System.exit(-1);
            }
            System.out.println(ConsoleColors.GREEN + "Выполните настройку конфигурационных файлов." + ConsoleColors.RESET);
            System.exit(0);
        }

        Configs.init();



        //confirm group token
        {
            String groupToken = Configs.tokens.getGroupToken();

            VkRequester requester = new VkRequester(Configs.getApiVersion(),groupToken);
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
                VkRequester groupRequester;
                VkRequester adminRequester;

                {
                    String groupToken = Configs.tokens.getGroupToken();
                    String adminToken = Configs.tokens.getAdminToken();
                    groupRequester = new VkRequester(Configs.getApiVersion(), groupToken);
                    adminRequester = new VkRequester(Configs.getApiVersion(), adminToken);
                }

                int groupId;
                int[] groupAdminsIds;

                {
                    groupId = ((JSONObject) ((JSONArray) groupRequester.post("groups.getById", null).get("response")).get(0)).getInt("id");

                    HashMap<String,String> params = new HashMap();
                    params.clear();
                    params.put("group_id", String.valueOf(groupId));
                    params.put("filter", "managers");
                    params.put("fields", "name");

                    JSONObject res = groupRequester.post("groups.getMembers", params);

                    res = res.getJSONObject("response");
                    JSONArray adminsJsonArray = res.getJSONArray("items");

                    groupAdminsIds = new int[adminsJsonArray.length()];

                    for (int i = 0; i < adminsJsonArray.length(); i++)
                    {
                        if (((JSONObject) adminsJsonArray.get(i)).getString("role").equals("administrator"))
                            groupAdminsIds[i] = ((JSONObject) adminsJsonArray.get(i)).getInt("id");
                        else
                            groupAdminsIds[i] = 0;
                    }

                    int notAdminsCount = 0;
                    for (int i = 0; i < groupAdminsIds.length; i++)
                    {
                        if (groupAdminsIds[i] == 0)
                            notAdminsCount++;
                    }

                    int[] newGroupAdminsIds = new int[groupAdminsIds.length - notAdminsCount];
                    int iteratorNewArray = 0;
                    for (int i = 0; i < groupAdminsIds.length; i++)
                    {
                        if (groupAdminsIds[i] != 0)
                        {
                            newGroupAdminsIds[iteratorNewArray] = groupAdminsIds[i];
                            iteratorNewArray++;
                        }
                    }
                    groupAdminsIds = newGroupAdminsIds;
                }

                int adminId;
                JSONObject adminInfo = adminRequester.post("users.get",null);

                if (adminInfo.has("error"))
                {
                    System.out.println(ConsoleColors.RED + "admin_token: " + adminInfo.getJSONObject("error").getString("error_msg") + ConsoleColors.RESET);
                    System.exit(-1);
                }
                adminId = adminInfo.getJSONArray("response").getJSONObject(0).getInt("id");

                {
                    boolean hasThisAdminInGroup = false;

                    for (int i = 0; i < groupAdminsIds.length && !hasThisAdminInGroup; i++)
                        if (groupAdminsIds[i] == adminId) hasThisAdminInGroup = true;

                    JSONObject groupInfo = groupRequester.post("groups.getById", null).getJSONArray("response").getJSONObject(0);
                    if (!hasThisAdminInGroup)
                    {
                        System.out.println(ConsoleColors.RED + "Пользователь (id:" + adminId + ") " + adminInfo.getJSONArray("response").getJSONObject(0).getString("first_name") + " " + adminInfo.getJSONArray("response").getJSONObject(0).getString("last_name") + " " + "не является администратором группы (id: " + groupId + ")" + groupInfo.getString("name") + ConsoleColors.RESET);
                        System.exit(-1);
                    }

                    JSONObject res = adminRequester.post("account.getAppPermissions", null);

                    if (!((res.getInt("response") & (1 << 18)) == (1 << 18)))
                    {
                        Map<String, String> params = new HashMap<>();
                        params.put("name_case", "gen");
                        adminInfo = adminRequester.post("users.get", null);
                        System.out.println(ConsoleColors.RED + "У токена админа " + " (id:" + adminId + ") " + adminInfo.getJSONArray("response").getJSONObject(0).getString("first_name") + " " + adminInfo.getJSONArray("response").getJSONObject(0).getString("last_name") + " " + "нет доступа к группе (id: " + groupId + ")" + groupInfo.getString("name") + ConsoleColors.RESET);
                        System.exit(-1);
                    }
                }

                Map map = new HashMap();


            }
            catch (IOException exception)
            {
                System.out.println(ConsoleColors.RED + "Проблемма с доступом к серверу VK" + ConsoleColors.RESET);
                System.exit(-1);
            }
        }

        Thread usersLongPollThread = new Thread(new UserLongPollHandler(Configs.tokens.getGroupToken(),Configs.getApiVersion()));
        usersLongPollThread.setPriority(10);
        usersLongPollThread.start();


    }
}