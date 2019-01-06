package per.pawday.vkbot.main;

import per.pawday.vkbot.configs.Advanced;
import per.pawday.vkbot.configs.Token;
import per.pawday.vkbot.json.Functions;


public class Main
{
    public static void main(String[] args)
    {
        Token token = new Token();
        Functions jsonFuns = new Functions();
        Advanced advancedConf = new Advanced();


        System.out.println(advancedConf.heroku.usage);


    }
}
