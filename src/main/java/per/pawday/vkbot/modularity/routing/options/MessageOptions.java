package per.pawday.vkbot.modularity.routing.options;

import org.json.JSONArray;
import org.json.JSONObject;

public class MessageOptions
{
	public MessageOptions(JSONObject object)
	{
		this.separator = object.getString("separator").charAt(0);
		JSONArray array = object.getJSONArray("message");
		this.message = new String[array.length()];
		for (int i = 0; i < this.message.length; i++)
		{
			this.message[i] = array.getString(i);
		}

		this.callSignOptions = new CallSignOptions(object.getJSONObject("callSignOptions"));

	}
	public CallSignOptions callSignOptions;
	public String[] message;
	public char separator;
}