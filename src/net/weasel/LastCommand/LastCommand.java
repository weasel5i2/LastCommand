package net.weasel.LastCommand;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class LastCommand extends JavaPlugin 
{
	private static List<String> playerList = new ArrayList<String>();
	private static List<String> commandList = new ArrayList<String>();
	public static String appName = null;
	public static boolean isEnabled = false;
	public static long lastLogChange = 0;
	public static boolean debugging = false;
	
	private final PlayerEvents playerListener = new PlayerEvents();

	@Override
	public void onDisable() 
	{
		isEnabled = false;
		logOutput("LastCommand disabled.");
	}

	@Override
	public void onEnable() 
	{
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Type.PLAYER_COMMAND_PREPROCESS, playerListener, Priority.Monitor, this);
		pm.registerEvent(Type.PLAYER_QUIT, playerListener, Priority.Monitor, this);

		PluginDescriptionFile pdfFile = this.getDescription();
		appName = pdfFile.getName();
		isEnabled = true;
		logOutput( appName + " v" + pdfFile.getVersion() + " loaded.");
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) 
	{
        String commandName = command.getName().toLowerCase();
    	
    	if(commandName.equalsIgnoreCase("last") || commandName.equals("!") 
    	|| commandName.equals("..") || commandName.equalsIgnoreCase("ll" ) )
    	{
            if( sender instanceof Player )
            {
	            String lastCommand = getLastCommand((Player)sender);
	    		
	    		if( lastCommand != null )
	    		{
    				if( debugging ) logOutput( "execute: " + sender.toString() + "," + lastCommand );
	    				
    				if( getServer().dispatchCommand(sender, lastCommand.substring(1) ) == true )
    					if( debugging ) logOutput( "Success." );
    				else
    					if( debugging ) logOutput( "Failed to send command." );
	    		}
	        	else
	        		sender.sendMessage( "I don't know what your last command was." );
	    		
				return true;
            }
            else
            {
            	sender.sendMessage( "LastCommand:" );
            	sender.sendMessage( "=================================" );
            	
            	if( playerList.size() > 0 )
            	{
            		for( int X = 0; X < playerList.size(); X++ )
            		{
            			sender.sendMessage( playerList.get(X) + ": " + commandList.get(X) );
            		}
            		
            		return true;
            	}
            	else
            	{
            		sender.sendMessage( "No player commands are saved." );
            		return true;
            	}
            }
		}

    	return false;
	}
	
	public static String getLastCommand( Player player )
	{
		if( debugging ) logOutput( "getLastCommand(" + player.getName() + ")" );
		
		String retVal = null;
		String who = null;
		
		for( int x = 0; x < playerList.size(); x++ )
		{
			who = playerList.get(x).toString();
			
			if( who == player.getName() )
			{
				retVal = commandList.get(x);
				break;
			}
		}

		return retVal;
	}
	
	public static void setLastCommand( Player player, String pCommand )
	{
		if( debugging ) logOutput( "setLastCommand(" + player.getName() + "," + pCommand + ")" );
		
		String who = null;
		boolean found = false;
		
		for( int x = 0; x < playerList.size(); x++ )
		{
			who = playerList.get(x).toString();
			
			if( who == player.getName() )
			{
				found = true;
				commandList.set(x, pCommand );
				break;
			}
		}
		
		if( !found )
		{
			playerList.add(player.getName());
			commandList.add( pCommand );
		}
	}

	public static void removePlayer( Player player )
	{
		if( debugging ) logOutput( "removePlayer(" + player.getName() + ")" );
		
		int found = -1;
		String who = null;
		
		for( int x = 0; x < playerList.size(); x++ )
		{
			who = playerList.get(x).toString();
			
			if( who == player.getName() )
			{
				found = x;
				break;
			}
		}
		
		if( found != -1 )
		{
			commandList.remove( found );
			playerList.remove( found );
		}
	}

	public static void logOutput(String output)
	{
		System.out.println( "[LastCommand] " + output );
	}

	public static String arrayToString(String[] a, String separator) 
    {
        String result = "";
        
        if (a.length > 0) 
        {
            result = a[0];    // start with the first element
            for (int i=1; i<a.length; i++) {
                result = result + separator + a[i];
            }
        }
        return result;
    }

	private class PlayerEvents extends PlayerListener 
	{
		@Override
		public void onPlayerQuit( PlayerQuitEvent e )
		{
			if( e.getType() == Type.PLAYER_QUIT )
			{
				removePlayer( e.getPlayer() );				
			}
		}
		
		@Override
		public void onPlayerCommandPreprocess( PlayerCommandPreprocessEvent e ) 
		{
			if( debugging ) logOutput( "onPlayerCommandPreprocess(" + e.toString() + "): " + e.getMessage() );
			
			if( !e.getMessage().equalsIgnoreCase("/ll") && !e.getMessage().equalsIgnoreCase("/last") 
			&& !e.getMessage().equals("/!") && !e.getMessage().equalsIgnoreCase("/..")  )
			{
				setLastCommand( e.getPlayer(), e.getMessage() );
			}
		}
	}
}
