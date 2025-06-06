package DragonMCSoftwares;

import OutSideAPIs.bStats.Metrics;
import DragonUtils.banning;
import DragonUtils.banning.banlisttype;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import DragonUtils.logging;

import java.util.*;
import java.util.logging.Level;

public final class GodKillerAnticheat extends JavaPlugin implements Listener
{
    String chatprefix="&k&6|&r&a[&r&l&6诛仙&r&b&n&o反作弊系统&r&a]&r&k&6| &r&6&l";
    String banprefix="&6&k|&a&k[&r&l&6诛仙&r&b&n&o反作弊系统&r&a&k]&6&k|&r";
    public void loging(Level level,String message)
    {
        logging.log(level,chatprefix,message);
    }
    List<banlisttype> banlist=new ArrayList<>(); // 封禁列表每组数据1号为玩家名,2号为IP,3号为时间,4号为原因
    @Override
    public void onEnable()
    {
        //banlisttype HelloWorldCoderTest;
        //HelloWorldCoderTest=new banlisttype();
        //HelloWorldCoderTest.name="HelloWorldCoder";
        //HelloWorldCoderTest.ip="/127.0.0.1";
        //HelloWorldCoderTest.time=114514;
        //HelloWorldCoderTest.reason="&6666&n&b爱开欧?&r";
        //HelloWorldCoderTest.duration=114514;
        //banlist.add(HelloWorldCoderTest);
        // Plugin startup logic
        loging(Level.INFO,"插件起动中...");
        loging(Level.INFO,"启动事件监听...");
        getServer().getPluginManager().registerEvents(this, this);
        loging(Level.INFO,"监听启动完成");
        loging(Level.INFO,"加载配置文件...");
        // 加载配置文件
        loging(Level.INFO,"插件配置加载完成");
        loging(Level.INFO,"正在加载bStats,这不会收集你的个人数据,请放心使用...");
        Metrics metrics = new Metrics(this,26100);
        loging(Level.INFO,"正在注册命令...");
        loging(Level.INFO,"插件启动完成");
    }

    @EventHandler
    public void onAsyncLogin(AsyncPlayerPreLoginEvent event) //封禁处理 2025/5/27 13:38 Commit No.1
    {
        loging(Level.INFO,"玩家["+event.getName()+"]("+event.getAddress()+")尝试登录服务器,正在审查流量.....");
        loging(Level.INFO,"玩家["+event.getName()+"]("+event.getAddress()+")登陆审查:检查封禁.....");
        banning.banreturntype baninfo=banning.isbanned(banlist,event.getName(),event.getAddress().toString());
        if(baninfo.banned)
        {
            // 封禁中
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED,logging.ChangeColorcode(banprefix+"\n&b诛仙!你被封印了!"+ "\n&6&k|&r&6&l剩余封印时间&r&6&k| &r&a&n"+baninfo.time+"&r"+"\n&c&l理由: &r&e&n"+baninfo.reason)); // 目前使用踢出，占用较大，且有漏洞风险，不知道大佬们有没有类似TempBan那种解决方案可以用
            // 封禁信息提示至log
            loging(Level.INFO,"被封印玩家"+event.getName()+"被踢出了服务器,理由: "+baninfo.reason+"剩余封印时间: "+baninfo.time);
            if(event.getName()!=baninfo.name)
            {
                boolean flag=true;
                for(banlisttype list:banlist) if(list.name.equalsIgnoreCase(event.getName()))
                {
                    flag=false;
                    break;
                }
                if(flag) banning.addban(banlist,event.getName(),baninfo.ip,baninfo.time,baninfo.reason,baninfo.duration);
                loging(Level.WARNING,"玩家["+event.getName()+"]("+event.getAddress()+")登陆审查: 玩家尝试了进行用户名绕过!已更新数据库!");
            }
            if(event.getAddress().toString()!=baninfo.ip)
            {
                boolean flag=true;
                for(banlisttype list:banlist) if(list.ip.equalsIgnoreCase(event.getAddress().toString()))
                {
                    flag=false;
                    break;
                }
                banlisttype thisplayer=banlist.get(baninfo.pointer);
                thisplayer.ip=event.getAddress().toString();
                thisplayer.duration=baninfo.duration;
                thisplayer.time=baninfo.time;
                if(flag) banning.addban(banlist,thisplayer.name,thisplayer.ip,thisplayer.time,thisplayer.reason,thisplayer.duration);
                loging(Level.WARNING,"玩家["+event.getName()+"]("+event.getAddress()+")登陆审查: 玩家尝试了进行IP绕过!已更新数据库!");
            }
        }
        else loging(Level.INFO,"玩家["+event.getName()+"]("+event.getAddress()+")登陆审查通过: 无封禁");
        if(!baninfo.banned && baninfo.duration!=0 && baninfo.name!="") loging(Level.WARNING,"玩家["+event.getName()+"]("+event.getAddress()+")登陆审查: 玩家有已解封但是还在考察期的封禁记录!");
    }

    // 命令处理
    @Override
    public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args)
    {
        if(cmd.getName().equalsIgnoreCase("ban") || cmd.getName().equalsIgnoreCase("totalban") || cmd.getName().equalsIgnoreCase("tempban") || cmd.getName().equalsIgnoreCase("ban-ip"))
        {
            if(sender.hasPermission("godkilleracmc.bancontrol.ban"))
            {
                loging(Level.WARNING,"占位代码已被执行,请检查源代码");
            }
        }
        return false;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        loging(Level.INFO,"插件关闭中...");
        // 插件关闭
        loging(Level.INFO,"插件关闭完成");
    }
}
