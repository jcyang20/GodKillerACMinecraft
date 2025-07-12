// 命令处理

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
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import static DragonMCSoftwares.GodKillerAnticheat.banlist;
import static org.bukkit.Bukkit.getOnlinePlayers;
import java.util.logging.Level;

/**
 * 命令处理类
 * 负责注册和处理插件的命令
 */
public class commands
{
    /**
     * 初始化命令
     * 注册命令执行器和命令补全器
     * 
     * @return 初始化成功返回true
     */
    public static boolean commandinit()
    {
        GodKillerAnticheat plugin = GodKillerAnticheat.getPlugin(GodKillerAnticheat.class);  // 获取插件类
        // 注册权限
        Permission banpermission = new Permission("godkilleracmc.bancontrol.ban","允许操作封禁");
        banpermission.setDefault(PermissionDefault.OP);
        plugin.getServer().getPluginManager().addPermission(banpermission);
        Permission unbanpermission = new Permission("godkilleracmc.bancontrol.unban","允许操作解封");
        unbanpermission.setDefault(PermissionDefault.OP);
        plugin.getServer().getPluginManager().addPermission(unbanpermission);
        // 注册命令
        try
        {
            plugin.getCommand("ban").setExecutor(new bancommandexcute());
            plugin.getCommand("ban").setTabCompleter(new bancommandcomplete());
            plugin.getCommand("ban").setUsage("/ban <玩家> <原因> <时间>(秒,永封0)");
            List<String> aliases = new java.util.ArrayList<>();
            aliases.add("totalban");
            aliases.add("tempban");
            aliases.add("ban-ip");
            plugin.getCommand("ban").setAliases(aliases);
        }
        catch(Exception e)
        {
            logging.log(Level.WARNING,GodKillerAnticheat.chatprefix,"命令注册故障,请向Dragon Minecraft Softwares反馈: "+e);
        }
        return true;
    }
    /**
     * ban命令执行器
     * 处理玩家封禁命令的执行
     */
    public static class bancommandexcute implements CommandExecutor   // 封禁执行
    {
        /**
         * 处理命令执行
         * 
         * @param sender  命令发送者
         * @param command 执行的命令
         * @param lable   命令标签
         * @param args    命令参数
         * @return 命令执行成功返回true，否则返回false
         */
        public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String lable, @NotNull String[] args)
        {
            if(command.getName().equalsIgnoreCase("ban"))
            {
                if (sender.hasPermission("godkilleracmc.bancontrol.ban"))  // 权限检查
                {
                    try
                    {
                        Player banedplayer=Bukkit.getPlayer(args[0]);
                        // 计算封禁时长（秒转毫秒）
                        long dut = 0;
                        if (args.length >= 3) dut = Long.parseLong(args[2]) * 1000;
                        if(banedplayer!=null) banning.ban(banlist,args[0],utils.getplayerip(banedplayer),0,dut,args[1],false,System.currentTimeMillis());
                        sender.sendMessage(logging.ChangeColorcode(GodKillerAnticheat.chatprefix + "&a&l成功将玩家 &e"+args[0]+" &a以&b "+args[1]+" &a为理由封印&9 "+args[2]+" &a秒&r"));
                        return true;
                    }
                    catch (Exception e)
                    {
                        sender.sendMessage(logging.ChangeColorcode(GodKillerAnticheat.chatprefix + "&4&l请输入正确的参数,故障提示: "+e+"&r"));
                        return false;
                    }
                }
                sender.sendMessage(logging.ChangeColorcode(GodKillerAnticheat.chatprefix+"&r&6&l诛啥仙啊,你看你配吗??"));
            }
            return false;
        }
    }
  
    /**
     * ban命令补全器
     * 提供命令参数的自动补全功能
     */
    public static class bancommandcomplete implements TabCompleter // 命令补全
    {
         /**
         * 处理命令补全
         * 
         * @param sender  命令发送者
         * @param command 执行的命令
         * @param lable   命令标签
         * @param args    当前已输入的参数
         * @return 补全建议列表，如果没有建议则返回null
         */
        public List<String> onTabComplete(@NotNull CommandSender sender, Command command, @NotNull String lable, @NotNull String[] args)
        {
            if(command.getName().equalsIgnoreCase("ban"))
            {
                if (sender.hasPermission("godkilleracmc.bancontrol.ban"))
                {
                    if(args.length==1) return getOnlinePlayers().stream().map(Player::getName).toList();
                    if(args.length==2) return Collections.singletonList("请输入封禁理由");
                    if(args.length==3) return Collections.singletonList("请输入封禁时间(单位: 秒, 0永封, 不填默认0)");
                    return Collections.singletonList("填充完成,请执行");
                }
                else return Collections.singletonList("你没有权限执行这个指令");
            }
            return null;
        }
    }
}
