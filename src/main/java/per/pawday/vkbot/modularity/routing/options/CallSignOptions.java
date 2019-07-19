package per.pawday.vkbot.modularity.routing.options;

import org.json.JSONArray;
import org.json.JSONObject;

public class CallSignOptions
{
	public CallSignOptions(JSONObject object)
	{
		JSONArray botNamesJson = object.getJSONArray("names");
		this.botNames = new String[botNamesJson.length()];
		for (int i = 0; i < this.botNames.length; i++)
		{
			this.botNames[i] = botNamesJson.getString(i);
		}
		JSONArray symbols = object.getJSONArray("firstSymbols");
		this.firstSymbols = new char[symbols.length()];
		for (int i = 0; i < this.firstSymbols.length; i++)
		{
			this.firstSymbols[i] = symbols.getString(i).charAt(0);
		}
	}
	public String[] botNames;
	public char[] firstSymbols;
}
