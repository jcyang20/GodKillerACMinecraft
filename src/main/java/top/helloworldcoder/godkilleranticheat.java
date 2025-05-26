package top.helloworldcoder;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class godkilleranticheat extends JavaPlugin implements Listener
{

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

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().log(Level.INFO,"插件关闭中...");
        // 插件关闭
        getLogger().log(Level.INFO,"插件关闭完成");
    }
}
