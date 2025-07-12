// Created by HelloWorldCoder on 2025/5/27 13:38
// MODIFICATION IS NOT ALLOWED
// A Part Of DragonUtils

package DragonMCSoftwares;

import DragonUtils.logging;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.logging.Level;

import static DragonUtils.utils.formatTimeprd;

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
    public static class BanListType // 封禁信息储存
    {
        public String name;      // 玩家名称
        public String ip;        // 玩家IP
        public long time;        // 解封时间（时间戳） 
        public String reason;    // 封禁原因
        public long duration;    // 封禁持续时间（毫秒）
        public int banId;        // ban id
    }

    /**
     * 封禁信息返回类
     * 用于返回玩家的封禁状态和详细信息
     */
    public static class BanReturnType // 封禁信息返回
    {
        public boolean banned;  // 是否被封禁
        public String name;     // 玩家名称
        public String ip;       // 玩家IP地址
        public long time;       // 解封时间（时间戳）
        public String reason;   // 封禁原因
        public long duration;   // 封禁持续时间（毫秒）
        public int pointer;     // 在封禁列表中的索引
        public int banId;       // ban id
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
    public static boolean addban(List<BanListType> banlist, String name, String ip, long time, String reason, long duration, int banid)  // 封禁信息添加
    {
        BanListType banListThing = new BanListType();
        banListThing.name = name;
        banListThing.ip = ip;
        banListThing.time = time;
        banListThing.reason = reason;
        banListThing.duration = duration;
        banListThing.banId = banid;
        banlist.add(banListThing);
        return true;
    }
    /**
     * 封禁玩家
     * 
     * @param banlist    封禁列表
     * @param name       玩家名称
     * @param ip         玩家IP地址
     * @param timeUp    封禁时间倍数
     * @param duration   封禁持续时间（毫秒）
     * @param reason     封禁原因
     * @param mercy      控制是否运行宽恕
     * @param timeBefore 封禁历史宽恕时间戳
     * @return 如果执行了解封波，返回解封的数量；否则返回-1
     */
    public static int ban(List<BanListType> banlist, String name, String ip, int timeUp, long duration, String reason, boolean mercy, long timeBefore)
    {
        int count = -1;
        // 如果启用了宽恕，执行解封操作
        if(mercy) {
          count = mercyWave(banlist,timeBefore);
        }
        reason = logging.ChangeColorCode(reason);
        int banId = banlist.size();
        for(BanListType banListThing : banlist)
        {
            if(banListThing.banId == banId)
            {
                Player banedplayer = Bukkit.getPlayer(name);
                if (banedplayer != null && banedplayer.isOnline())
                    banedplayer.kickPlayer(logging.ChangeColorCode(GodKillerAntiCheat.banPrefix + "\n&b诛仙!你被封印了!" + "\n&6&k|&r&6&l剩余封印时间&r&6&k| &r&a&n" + formatTimeprd(duration, logging.ChangeColorCode("&byyyy&4年 &bMM&c月 &bdd&e天 | &bHH&2小时 &bmm&a分钟 &bss&9秒")) + "&r" + "\n&c&l理由: &r&e&n" + reason));
                return count;
            }
            if(banListThing.name.equals(name) || banListThing.ip.equals(ip))
            {
                if(banListThing.ip.equals(ip))
                {
                    addban(banlist, name, ip, banListThing.duration * timeUp + duration + System.currentTimeMillis(), reason, banListThing.duration * timeUp + duration,banId);
                    addban(banlist,banListThing.name,banListThing.ip,banListThing.duration * timeUp + duration + System.currentTimeMillis(), reason, banListThing.duration * timeUp + duration,banId);
                    banlist.remove(banListThing);
                }
                Player banedplayer= Bukkit.getPlayer(name);
                if (banedplayer != null && banedplayer.isOnline())
                    banedplayer.kickPlayer(logging.ChangeColorCode(GodKillerAntiCheat.banPrefix + "\n&b诛仙!你被封印了!" + "\n&6&k|&r&6&l剩余封印时间&r&6&k| &r&a&n" + formatTimeprd(duration, logging.ChangeColorCode("&byyyy&4年 &bMM&c月 &bdd&e天 | &bHH&2小时 &bmm&a分钟 &bss&9秒")) + "&r" + "\n&c&l理由: &r&e&n" + reason));
                if(banListThing.name.equals(name))
                {
                    banlist.remove(banListThing);
                    addban(banlist,name,ip,banListThing.duration* timeUp +duration+System.currentTimeMillis(),reason,banListThing.duration* timeUp +duration,banId);  // 封禁时间追加
                }
            }
        }

        addban(banlist,name,ip,duration+System.currentTimeMillis(),reason,duration,banId);
        Player banedplayer= Bukkit.getPlayer(name);
        if (banedplayer != null && banedplayer.isOnline())
            banedplayer.kickPlayer(logging.ChangeColorCode(GodKillerAntiCheat.banPrefix + "\n&b诛仙!你被封印了!" + "\n&6&k|&r&6&l剩余封印时间&r&6&k| &r&a&n" + formatTimeprd(duration, logging.ChangeColorCode("&byyyy&4年 &bMM&c月 &bdd&e天 | &bHH&2小时 &bmm&a分钟 &bss&9秒")) + "&r" + "\n&c&l理由: &r&e&n" + reason));
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
    public static boolean unban(List<BanListType> banlist, String name, String ip) {
        boolean sec = false;
        for (BanListType banListThing : banlist) {
            if (banListThing.name.equals(name) || banListThing.ip.equals(ip)) {
                // 将解封时间设置为当前时间之前，表示已解封
                banListThing.time = System.currentTimeMillis() - 1;
                sec = true;
            }
        }
        return sec;
    }

    /**
     * 执行解封波，清理过期的封禁记录
     * 
     * @param banlist    封禁列表
     * @param timeBefore 时间阈值，小于此时间的封禁记录将被移除
     * @return 移除的封禁记录数量
     */
    public static int mercyWave(List<BanListType> banlist, long timeBefore) {
        int count = 0;
        // 注意：在遍历集合的同时修改集合可能会导致ConcurrentModificationException
        // 应该使用迭代器或创建一个要移除的元素列表
        List<BanListType> toRemove = new java.util.ArrayList<>();
        
        for (BanListType banListThing : banlist) {
            if (banListThing.time <= timeBefore + System.currentTimeMillis()) {
                toRemove.add(banListThing);
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
    static BanReturnType banTimeCheck(List<BanListType> banlist, BanListType list)
    {
        if(list.time>System.currentTimeMillis() || list.duration==0)
        {
            BanReturnType banreturntype=new BanReturnType();
            banreturntype.banned=true;
            banreturntype.name=list.name;
            banreturntype.ip=list.ip;
            banreturntype.time=list.time;
            banreturntype.reason=list.reason;
            banreturntype.duration=list.duration;
            banreturntype.pointer=banlist.indexOf(list);
            banreturntype.banId =list.banId;
            return banreturntype;
        }
        BanReturnType banreturntype=new BanReturnType();
        banreturntype.banned=false;
        banreturntype.name=list.name;
        banreturntype.ip=list.ip;
        banreturntype.time=list.time;
        banreturntype.reason=list.reason;
        banreturntype.duration=list.duration;
        banreturntype.pointer=banlist.indexOf(list);
        banreturntype.banId =list.banId;
        return banreturntype;
    }
    public static BanReturnType isBanned(List<BanListType> banlist, String name, String ip)
    {
        for(BanListType list:banlist)
        {   // 判断玩家是否被封禁，目前遍历，不知道大佬们有没有更好的解决方法
            if(list.name.equalsIgnoreCase(name) || list.ip.equalsIgnoreCase(ip))
            {
                // 解封时间判定
                return banTimeCheck(banlist,list);
            }
        }
        BanReturnType banreturntype=new BanReturnType();
        banreturntype.banned=false;
        banreturntype.name="";
        banreturntype.ip="";
        banreturntype.time=0;
        banreturntype.reason="";
        banreturntype.duration=0;
        banreturntype.pointer=-1;
        banreturntype.banId =-1;
        return banreturntype;
    }
}