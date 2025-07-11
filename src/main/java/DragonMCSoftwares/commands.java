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

/**
 * 命令处理类
 * 负责注册和处理插件的命令
 */
public class commands {
    
    /**
     * 初始化命令
     * 注册命令执行器和命令补全器
     * 
     * @return 初始化成功返回true
     */
    public static boolean commandinit() {
        GodKillerAnticheat plugin = GodKillerAnticheat.getPlugin(GodKillerAnticheat.class);
        plugin.getCommand("ban").setExecutor(new bancommandexcute());
        plugin.getCommand("ban").setTabCompleter(new bancommandcomplete());
        return true;
    }
    
    /**
     * ban命令执行器
     * 处理玩家封禁命令的执行
     */
    public static class bancommandexcute implements CommandExecutor {
        
        /**
         * 处理命令执行
         * 
         * @param sender  命令发送者
         * @param command 执行的命令
         * @param lable   命令标签
         * @param args    命令参数
         * @return 命令执行成功返回true，否则返回false
         */
        public boolean onCommand(CommandSender sender, Command command, String lable, String[] args) {
            if (command.getName().equalsIgnoreCase("ban")) {
                // 检查发送者是否有权限执行ban命令
                if (sender.hasPermission("godkilleracmc.bancontrol.ban")) {
                    try {
                        // 获取要封禁的玩家
                        Player banedplayer = Bukkit.getPlayer(args[0]);
                        // 计算封禁时长（秒转毫秒）
                        long dut = 0;
                        if (args.length >= 3) {
                            dut = Long.parseLong(args[2]) * 1000;
                        }
                        // 执行封禁
                        banning.ban(banlist, args[0], utils.getplayerip(banedplayer), 0, dut, args[1], false, System.currentTimeMillis());
                        return true;
                    } catch (Exception e) {
                        // 参数错误或其他异常
                        sender.sendMessage(logging.ChangeColorcode(GodKillerAnticheat.banprefix + "&4&l请输入正确的参数,故障提示: " + e + "&r"));
                        return false;
                    }
                }
                // 无权限提示
                sender.sendMessage(logging.ChangeColorcode("&k&6|&r&a[&r&l&6诛仙&r&b&n&o反作弊系统&r&a]&r&k&6| &r&6&l诛啥仙啊,你看你配吗??"));
            }
            return false;
        }
    }
    
    /**
     * ban命令补全器
     * 提供命令参数的自动补全功能
     */
    public static class bancommandcomplete implements TabCompleter {
        
        /**
         * 处理命令补全
         * 
         * @param sender  命令发送者
         * @param command 执行的命令
         * @param lable   命令标签
         * @param args    当前已输入的参数
         * @return 补全建议列表，如果没有建议则返回null
         */
        public List<String> onTabComplete(CommandSender sender, Command command, String lable, String[] args) {
            if (command.getName().equalsIgnoreCase("ban")) {
                // 检查发送者是否有权限执行ban命令
                if (sender.hasPermission("godkilleracmc.bancontrol.ban")) {
                    // 根据已输入的参数数量提供不同的补全建议
                    if (args.length == 0) {
                        // 第一个参数：在线玩家列表
                        return getOnlinePlayers().stream().map(Player::getName).toList();
                    }
                    if (args.length == 1) {
                        // 第二个参数：封禁理由提示
                        return Collections.singletonList("请输入封禁理由");
                    }
                    if (args.length == 2) {
                        // 第三个参数：封禁时间提示
                        return Collections.singletonList("请输入封禁时间(单位: 秒, 0永封, 不填默认0)");
                    }
                    return null;
                } else {
                    // 无权限提示
                    return Collections.singletonList("你没有权限执行这个指令");
                }
            }
            return null;
        }
    }
}
