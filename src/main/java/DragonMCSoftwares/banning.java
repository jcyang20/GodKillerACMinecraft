// Created by HelloWorldCoder on 2025/5/27 13:38
// MODIFICATION IS NOT ALLOWED
// A Part Of DragonUtils

package DragonMCSoftwares;

import DragonUtils.logging;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.logging.Level;

import static DragonUtils.utils.formattimeprd;

/**
 * 封禁系统核心类
 * 提供玩家封禁、解封和封禁信息查询等功能
 * 时间使用long类型防止溢出
 */
public class banning {
    /*
      静态初始化块，在类加载时执行
      记录模块加载信息到日志
     */
    static {
        logging.log(Level.INFO, "&6[DragonUtils] &r", "&aSuccessfully Loaded &bModule [banning] &r&ain &r&6&_DragonUtils by HelloWorldCoder-China&r&a !&r");
    }

    /**
     * 封禁信息存储类
     * 用于存储玩家的封禁信息
     */
    public static class banlisttype // 封禁信息储存
    {
        public String name;      // 玩家名称
        public String ip;        // 玩家IP
        public long time;        // 解封时间（时间戳） 
        public String reason;    // 封禁原因
        public long duration;    // 封禁持续时间（毫秒）
        public int banid;        // ban id
    }

    /**
     * 封禁信息返回类
     * 用于返回玩家的封禁状态和详细信息
     */
    public static class banreturntype // 封禁信息返回
    {
        public boolean banned;  // 是否被封禁
        public String name;     // 玩家名称
        public String ip;       // 玩家IP地址
        public long time;       // 解封时间（时间戳）
        public String reason;   // 封禁原因
        public long duration;   // 封禁持续时间（毫秒）
        public int pointer;     // 在封禁列表中的索引
        public int banid;       // ban id
    }

    /**
     * 向封禁列表添加一个封禁记录
     * 
     * @param banlist  封禁列表
     * @param name     玩家名称
     * @param ip       玩家IP地址
     * @param time     解封时间（时间戳）
     * @param reason   封禁原因
     * @param duration 封禁持续时间（毫秒）
     * @return 添加成功返回true
     */
    public static boolean addban(List<banlisttype> banlist,String name,String ip,long time,String reason,long duration,int banid)  // 封禁信息添加
    {
        banlisttype banlistthing=new banlisttype();
        banlistthing.name=name;
        banlistthing.ip=ip;
        banlistthing.time=time;
        banlistthing.reason=reason;
        banlistthing.duration=duration;
        banlistthing.banid=banid;
        banlist.add(banlistthing);
        return true;
    }
    /**
     * 封禁玩家
     * 
     * @param banlist    封禁列表
     * @param name       玩家名称
     * @param ip         玩家IP地址
     * @param timemup    封禁时间倍数
     * @param duration   封禁持续时间（毫秒）
     * @param reason     封禁原因
     * @param mercy      控制是否运行宽恕
     * @param timebefore 封禁历史宽恕时间戳
     * @return 如果执行了解封波，返回解封的数量；否则返回-1
     */
    public static int ban(List<banlisttype> banlist,String name,String ip,int timemup,long duration,String reason,boolean mercy,long timebefore)
    {
        int count = -1;
        // 如果启用了宽恕，执行解封操作
        if(mercy) {
          count = mercywave(banlist,timebefore);
        }
        reason = logging.ChangeColorcode(reason);
        int banid = banlist.size();
        for(banlisttype banlistthing : banlist)
        {
            if(banlistthing.banid == banid)
            {
                Player banedplayer = Bukkit.getPlayer(name);
                if(banedplayer.isOnline()) banedplayer.kickPlayer(logging.ChangeColorcode(GodKillerAnticheat.banprefix+"\n&b诛仙!你被封印了!"+ "\n&6&k|&r&6&l剩余封印时间&r&6&k| &r&a&n"+ formattimeprd(duration,logging.ChangeColorcode("&byyyy&4年 &bMM&c月 &bdd&e天 | &bHH&2小时 &bmm&a分钟 &bss&9秒"))+"&r"+"\n&c&l理由: &r&e&n"+reason));
                return count;
            }
            if(banlistthing.name.equals(name) || banlistthing.ip.equals(ip))
            {
                if(banlistthing.ip.equals(ip))
                {
                    addban(banlist, name, ip, banlistthing.duration * timemup + duration + System.currentTimeMillis(), reason, banlistthing.duration * timemup + duration,banid);
                    addban(banlist,banlistthing.name,banlistthing.ip,banlistthing.duration * timemup + duration + System.currentTimeMillis(), reason, banlistthing.duration * timemup + duration,banid);
                    banlist.remove(banlistthing);
                }
                Player banedplayer= Bukkit.getPlayer(name);
                if(banedplayer.isOnline()) banedplayer.kickPlayer(logging.ChangeColorcode(GodKillerAnticheat.banprefix+"\n&b诛仙!你被封印了!"+ "\n&6&k|&r&6&l剩余封印时间&r&6&k| &r&a&n"+ formattimeprd(duration,logging.ChangeColorcode("&byyyy&4年 &bMM&c月 &bdd&e天 | &bHH&2小时 &bmm&a分钟 &bss&9秒"))+"&r"+"\n&c&l理由: &r&e&n"+reason));
                if(banlistthing.name.equals(name))
                {
                    banlist.remove(banlistthing);
                    addban(banlist,name,ip,banlistthing.duration*timemup+duration+System.currentTimeMillis(),reason,banlistthing.duration*timemup+duration,banid);  // 封禁时间追加
                }
            }
        }

        addban(banlist,name,ip,duration+System.currentTimeMillis(),reason,duration,banid);
        Player banedplayer= Bukkit.getPlayer(name);
        if(banedplayer.isOnline()) banedplayer.kickPlayer(logging.ChangeColorcode(GodKillerAnticheat.banprefix+"\n&b诛仙!你被封印了!"+ "\n&6&k|&r&6&l剩余封印时间&r&6&k| &r&a&n"+ formattimeprd(duration,logging.ChangeColorcode("&byyyy&4年 &bMM&c月 &bdd&e天 | &bHH&2小时 &bmm&a分钟 &bss&9秒"))+"&r"+"\n&c&l理由: &r&e&n"+reason));
        return count;
    }

    /**
     * 解除玩家封禁
     * 
     * @param banlist 封禁列表
     * @param name    玩家名称
     * @param ip      玩家IP地址
     * @return 如果找到并解封了玩家，返回true；否则返回false
     */
    public static boolean unban(List<banlisttype> banlist, String name, String ip) {
        boolean sec = false;
        for (banlisttype banlistthing : banlist) {
            if (banlistthing.name.equals(name) || banlistthing.ip.equals(ip)) {
                // 将解封时间设置为当前时间之前，表示已解封
                banlistthing.time = System.currentTimeMillis() - 1;
                sec = true;
            }
        }
        return sec;
    }

    /**
     * 执行解封波，清理过期的封禁记录
     * 
     * @param banlist    封禁列表
     * @param timebefore 时间阈值，小于此时间的封禁记录将被移除
     * @return 移除的封禁记录数量
     */
    public static int mercywave(List<banlisttype> banlist, long timebefore) {
        int count = 0;
        // 注意：在遍历集合的同时修改集合可能会导致ConcurrentModificationException
        // 应该使用迭代器或创建一个要移除的元素列表
        List<banlisttype> toRemove = new java.util.ArrayList<>();
        
        for (banlisttype banlistthing : banlist) {
            if (banlistthing.time <= timebefore + System.currentTimeMillis()) {
                toRemove.add(banlistthing);
                count++;
            }
        }
        
        // 移除所有过期的封禁记录
        banlist.removeAll(toRemove);
        return count;
    }

    /**
     * 检查封禁记录是否有效
     * 
     * @param banlist 封禁列表
     * @param list    要检查的封禁记录
     * @return 封禁信息返回对象，包含封禁状态和详细信息
     */
    static banreturntype bantimecheck(List<banlisttype> banlist,banlisttype list)
    {
        if(list.time>System.currentTimeMillis() || list.duration==0)
        {
            banreturntype banreturntype=new banreturntype();
            banreturntype.banned=true;
            banreturntype.name=list.name;
            banreturntype.ip=list.ip;
            banreturntype.time=list.time;
            banreturntype.reason=list.reason;
            banreturntype.duration=list.duration;
            banreturntype.pointer=banlist.indexOf(list);
            banreturntype.banid=list.banid;
            return banreturntype;
        }
        banreturntype banreturntype=new banreturntype();
        banreturntype.banned=false;
        banreturntype.name=list.name;
        banreturntype.ip=list.ip;
        banreturntype.time=list.time;
        banreturntype.reason=list.reason;
        banreturntype.duration=list.duration;
        banreturntype.pointer=banlist.indexOf(list);
        banreturntype.banid=list.banid;
        return banreturntype;
    }
    public static banreturntype isbanned(List<banlisttype> banlist,String name,String ip)
    {
        for(banlisttype list:banlist)
        {   // 判断玩家是否被封禁，目前遍历，不知道大佬们有没有更好的解决方法
            if(list.name.equalsIgnoreCase(name) || list.ip.equalsIgnoreCase(ip))
            {
                // 解封时间判定
                return bantimecheck(banlist,list);
            }
        }
        banreturntype banreturntype=new banreturntype();
        banreturntype.banned=false;
        banreturntype.name="";
        banreturntype.ip="";
        banreturntype.time=0;
        banreturntype.reason="";
        banreturntype.duration=0;
        banreturntype.pointer=-1;
        banreturntype.banid=-1;
        return banreturntype;
    }
}