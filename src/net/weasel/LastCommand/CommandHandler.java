package net.weasel.LastCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor 
{
	public LastCommand plugin;
	
	public CommandHandler( LastCommand instance )
	{
		plugin = instance;
	}
	
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) 
	{
        if( arg0 instanceof Player )
    	{
            String lastCommand = LastCommand.getLastCommand( (Player)arg0 );
    		
    		if( lastCommand != null )
    		{
    			LastCommand.logOutput( ((Player) arg0).getName() + " last cmd: " + lastCommand );
    			try
    			{
					if( plugin.getServer().dispatchCommand(arg0, lastCommand.substring(1) ) == false )
						arg0.sendMessage( "There was a problem sending your last command." );
    			}
    			catch( Exception e )
    			{
    				LastCommand.logOutput( "Exception caught: " + e.getCause().getMessage() );
    			}
    		}
        	else
        		arg0.sendMessage( "I don't know what your last command was." );
    		
			return true;
        }
        else
        {
        	arg0.sendMessage( "LastCommand:" );
        	arg0.sendMessage( "=================================" );
        	
        	if( LastCommand.playerList.size() > 0 )
        	{
        		for( int X = 0; X < LastCommand.playerList.size(); X++ )
        		{
        			arg0.sendMessage( LastCommand.playerList.get(X) + ": " 
        			+ LastCommand.commandList.get(X) );
        		}
        		
        		return true;
        	}
        	else
        	{
        		arg0.sendMessage( "No player commands are saved." );
        		return true;
        	}
        }
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
}
