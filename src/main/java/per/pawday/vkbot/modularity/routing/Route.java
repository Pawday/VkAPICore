package per.pawday.vkbot.modularity.routing;

import org.json.JSONObject;
import per.pawday.vkbot.modularity.routing.options.MessageOptions;
import per.pawday.vkbot.modularity.routing.options.platforms.JavaOptions;

import java.io.File;


public class Route
{
	public String moduleAlias;
	public File moduleFile;
	public String moduleType;
	public JavaOptions javaOptions;

	public MessageOptions messageOption;

	public int userLongPollEvent;
	public String botsLongPollEvent;

	public int processLiveTime;


	public Route(JSONObject object) throws RouteException
	{
		this.userLongPollEvent = object.getInt("userLongPollEvent");
		this.botsLongPollEvent = object.getString("botsLongPollEvent");
		this.moduleAlias = object.getString("moduleAlias");
		if (object.has("moduleFile"))
		{
			this.moduleFile = new File(object.getString("moduleFile"));
			if (!this.moduleFile.isFile())
			{
				throw new RouteException("moduleFile parameter is not a file");
			}
			if (!this.moduleFile.isAbsolute())
			{
				throw new RouteException("moduleFile is not absolute");
			}

			this.moduleType = object.getString("moduleType");
			if (this.moduleType.equals("java"))
			{
				this.javaOptions = new JavaOptions(object.optJSONObject("javaOptions"));
			}
			else
			{
				throw new RouteException("Module type " + this.moduleType + "is not supported");
			}
		}
		if (!object.has("messageOption"))
		{
			this.messageOption = null;
		}
		else
		{
			this.messageOption = new MessageOptions(object.getJSONObject("callSignOptions"));
		}
	}
}


