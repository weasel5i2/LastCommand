package net.weasel.LastCommand;

import org.bukkit.event.Event.Type;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

public class CommandListener extends PlayerListener
{
	public static LastCommand plugin = null;
	
	public CommandListener( LastCommand instance )
	{
		plugin = instance;
	}
	
	@Override
	public void onPlayerQuit( PlayerQuitEvent e )
	{
		if( e.getType() == Type.PLAYER_QUIT )
		{
			LastCommand.removePlayer( e.getPlayer() );				
		}
	}
	
	@Override
	public void onPlayerCommandPreprocess( PlayerCommandPreprocessEvent e ) 
	{
		if( !e.getMessage().equalsIgnoreCase("/l") && !e.getMessage().equalsIgnoreCase("/last") 
		&& !e.getMessage().equals("/!") && !e.getMessage().equalsIgnoreCase("/.") 
		&& !e.getMessage().equalsIgnoreCase("//") )
		{
			LastCommand.setLastCommand( e.getPlayer(), e.getMessage() );
		}
	}

}
