package top.helloworldcoder;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.logging.Level;

public final class godkilleranticheat extends JavaPlugin implements Listener
{
    String chatprefix="&k&6|&r&a[&r&l&6诛仙&r&b&n&o反作弊系统&r&a]&r&k&6| &r&6&l";
    List<List<String>> banlist=new ArrayList<>(); // 封禁列表每组数据1号为玩家名,2号为IP,3号为时间,4号为原因
    @Override
    public void onEnable()
    {
        // Plugin startup logic
        getLogger().log(Level.INFO,"插件起动中...");
        getLogger().log(Level.INFO,"启动事件监听...");
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().log(Level.INFO,"监听启动完成");
        getLogger().log(Level.INFO,"加载配置文件...");
        // 加载配置文件
        getLogger().log(Level.INFO,"插件配置加载完成");
        getLogger().log(Level.INFO,"插件启动完成");
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) //封禁处理 2025/5/27 13:38 Commit No.1
    {
        for(List<String> list:banlist)
        {   // 判断玩家是否被封禁，目前遍历，不知道大佬们有没有更好的解决方法
            if(list.get(0).equals(event.getPlayer().getName()) || list.get(1).equals(Objects.requireNonNull(event.getPlayer().getAddress()).getAddress().toString()))
            {
                // 到期判断，记得添加 到期判断，记得添加 到期判断，记得添加 到期判断，记得添加 到期判断，记得添加 到期判断，记得添加
                // 到期判断，记得添加 到期判断，记得添加 到期判断，记得添加 到期判断，记得添加 到期判断，记得添加 到期判断，记得添加
                // 到期判断，记得添加 到期判断，记得添加 到期判断，记得添加 到期判断，记得添加 到期判断，记得添加 到期判断，记得添加
                event.getPlayer().kickPlayer(chatprefix+"\n诛仙!\n你被封印了,理由: "+list.get(3)+"剩余封印时间: "+list.get(2)); // 目前使用踢出，占用较大，且有漏洞风险，不知道大佬们有没有类似TempBan那种解决方案可以用
                // 封禁信息提示至log
                getLogger().log(Level.INFO,"被封印玩家"+event.getPlayer().getName()+"被踢出了服务器,理由: "+list.get(3)+"剩余封印时间: "+list.get(2));
            }
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().log(Level.INFO,"插件关闭中...");
        // 插件关闭
        getLogger().log(Level.INFO,"插件关闭完成");
    }
}
