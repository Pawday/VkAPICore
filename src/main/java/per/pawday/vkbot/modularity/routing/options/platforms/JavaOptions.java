package per.pawday.vkbot.modularity.routing.options.platforms;

import org.json.JSONArray;
import org.json.JSONObject;

public class JavaOptions
{
	public JavaOptions(JSONObject object)
	{
		JSONArray array = object.getJSONArray("vmOptions");
		this.javaOptions = new String[array.length()];
		for (int i = 0; i < javaOptions.length; i++)
		{
			this.javaOptions[i] = array.getString(i);
		}
	}
	public String[] javaOptions;
}
