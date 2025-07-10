package DragonMCSoftwares;

import DragonUtils.logging;
import DragonUtils.utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Collections;
import java.util.List;
import static DragonMCSoftwares.GodKillerAnticheat.banlist;
import static org.bukkit.Bukkit.getOnlinePlayers;

public class commands
{
    public static boolean commandinit()
    {
        GodKillerAnticheat plugin = GodKillerAnticheat.getPlugin(GodKillerAnticheat.class);
        plugin.getCommand("ban").setExecutor(new bancommandexcute());
        plugin.getCommand("ban").setTabCompleter(new bancommandcomplete());
        return true;
    }
    public static class bancommandexcute implements CommandExecutor
    {
        public boolean onCommand(CommandSender sender, Command command, String lable, String[] args)
        {
            if(command.getName().equalsIgnoreCase("ban"))
            {
                if (sender.hasPermission("godkilleracmc.bancontrol.ban"))
                {
                    try
                    {
                        Player banedplayer = Bukkit.getPlayer(args[0]);
                        long dut = 0;
                        if (args.length >= 3) dut = Long.parseLong(args[2]) * 1000;
                        banning.ban(banlist,args[0],utils.getplayerip(banedplayer),0,dut,args[1],false,System.currentTimeMillis());
                        return true;
                    }
                    catch (Exception e)
                    {
                        sender.sendMessage(logging.ChangeColorcode(GodKillerAnticheat.banprefix + "&4&l请输入正确的参数,故障提示: "+e+"&r"));
                        return false;
                    }
                }
                sender.sendMessage(logging.ChangeColorcode("&k&6|&r&a[&r&l&6诛仙&r&b&n&o反作弊系统&r&a]&r&k&6| &r&6&l诛啥仙啊,你看你配吗??"));
            }
            return false;
        }
    }
    public static class bancommandcomplete implements TabCompleter
    {
        public List<String> onTabComplete(CommandSender sender, Command command, String lable, String[] args)
        {
            if(command.getName().equalsIgnoreCase("ban"))
            {
                if (sender.hasPermission("godkilleracmc.bancontrol.ban"))
                {
                    if(args.length==0) return getOnlinePlayers().stream().map(Player::getName).toList();
                    if(args.length==1) return Collections.singletonList("请输入封禁理由");
                    if(args.length==2) return Collections.singletonList("请输入封禁时间(单位: 秒, 0永封, 不填默认0)");
                    return null;
                }
                else return Collections.singletonList("你没有权限执行这个指令");
            }
            return null;
        }
    }
}
