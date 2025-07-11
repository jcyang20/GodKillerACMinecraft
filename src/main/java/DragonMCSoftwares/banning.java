// Created by HelloWorldCoder on 2025/5/27 13:38 Last Edited 2025/6/1 9:37
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
     * IP快速查找内部类
     * 用于存储IP地址的各个部分，加速IP查找过程
     */
    static class fastlookup4 {
        public static boolean[] ip1 = new boolean[257]; // 第一段IP地址
        public static boolean[] ip2 = new boolean[257]; // 第二段IP地址
        public static boolean[] ip3 = new boolean[257]; // 第三段IP地址
        public static int[] ip4 = new int[257];         // 第四段IP地址，存储对应的封禁列表索引
    }

//    fastlookup4 fastlookupv4 = new fastlookup4();

    /**
     * 初始化IP快速查找表
     * 将封禁列表中的IP地址解析并存储到快速查找表中
     * 
     * @param banlist 封禁列表
     */
    public static void fastipinit(List<banlisttype> banlist) {
        for (banlisttype banlistthing : banlist) {
            if (banlistthing.ip.contains(".")) {
                String[] ip = banlistthing.ip.split("\\.");
                fastlookup4.ip1[Integer.parseInt(ip[0])] = true;
                fastlookup4.ip2[Integer.parseInt(ip[1])] = true;
                fastlookup4.ip3[Integer.parseInt(ip[2])] = true;
                fastlookup4.ip4[Integer.parseInt(ip[3])] = banlist.indexOf(banlistthing);
            }
        }
    }

    /**
     * 快速检查IP是否在封禁列表中
     * 
     * @param ipin 要检查的IP地址
     * @return 如果IP被封禁，返回封禁列表中的索引；否则返回0
     */
    public static int checkipfast(String ipin) {
        if (ipin.contains(".")) {
            String[] ip = ipin.split("\\.");
            if (fastlookup4.ip1[Integer.parseInt(ip[0])] && 
                fastlookup4.ip2[Integer.parseInt(ip[1])] && 
                fastlookup4.ip3[Integer.parseInt(ip[2])] && 
                fastlookup4.ip4[Integer.parseInt(ip[3])] != 0) {
                return fastlookup4.ip4[Integer.parseInt(ip[3])];
            }
        }
        return 0;
    }

    /**
     * 封禁信息存储类
     * 用于存储玩家的封禁信息
     */
    public static class banlisttype {
        public String name;     // 玩家名称
        public String ip;       // 玩家IP地址
        public long time;       // 解封时间（时间戳）
        public String reason;   // 封禁原因
        public long duration;   // 封禁持续时间（毫秒）
    }

    /**
     * 封禁信息返回类
     * 用于返回玩家的封禁状态和详细信息
     */
    public static class banreturntype {
        public boolean banned;  // 是否被封禁
        public String name;     // 玩家名称
        public String ip;       // 玩家IP地址
        public long time;       // 解封时间（时间戳）
        public String reason;   // 封禁原因
        public long duration;   // 封禁持续时间（毫秒）
        public int pointer;     // 在封禁列表中的索引
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
    public static boolean addban(List<banlisttype> banlist, String name, String ip, long time, String reason, long duration) {
        banlisttype banlistthing = new banlisttype();
        banlistthing.name = name;
        banlistthing.ip = ip;
        banlistthing.time = time;
        banlistthing.reason = reason;
        banlistthing.duration = duration;
        banlist.add(banlistthing);
        return true;
    }

    /**
     * 封禁玩家
     * 
     * @param banlist    封禁列表
     * @param name       玩家名称
     * @param ip         玩家IP地址
     * @param timemup    时间倍数（用于累计封禁）
     * @param duration   封禁持续时间（毫秒）
     * @param reason     封禁原因
     * @param mercy      是否执行解封波（在封禁前清理过期的封禁记录）
     * @param timebefore 解封波的时间阈值
     * @return 如果执行了解封波，返回解封的数量；否则返回-1
     */
    public static int ban(List<banlisttype> banlist, String name, String ip, int timemup, long duration, String reason, boolean mercy, long timebefore) {
        int count = -1;
        // 如果启用了解封波，执行解封操作
        if (mercy) {
            count = mercywave(banlist, timebefore);
        }
        
        // 转换颜色代码
        reason = logging.ChangeColorcode(reason);
        
        // 检查玩家是否已被封禁，如果是，则累加封禁时间
        for (banlisttype banlistthing : banlist) {
            if (banlistthing.name.equals(name) || banlistthing.ip.equals(ip)) {
                addban(banlist, name, ip, 
                       banlistthing.duration * timemup + duration + System.currentTimeMillis(), 
                       reason, 
                       banlistthing.duration * timemup + duration);
                return count;
            }
        }
        
        // 如果玩家未被封禁，添加新的封禁记录
        addban(banlist, name, ip, duration + System.currentTimeMillis(), reason, duration);
        
        // 如果玩家在线，踢出玩家
        Player banedplayer = Bukkit.getPlayer(name);
        if (banedplayer != null && banedplayer.isOnline()) {
            banedplayer.kickPlayer(logging.ChangeColorcode(
                GodKillerAnticheat.banprefix + 
                "\n&b诛仙!你被封印了!" + 
                "\n&6&k|&r&6&l剩余封印时间&r&6&k| &r&a&n" + 
                formattimeprd(duration, logging.ChangeColorcode("&byyyy&4年 &bMM&c月 &bdd&e天 | &bHH&2小时 &bmm&a分钟 &bss&9秒")) + 
                "&r" + 
                "\n&c&l理由: &r&e&n" + reason));
        }
        
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
    static banreturntype bantimecheck(List<banlisttype> banlist, banlisttype list) {
        banreturntype banreturntype = new banreturntype();
        
        // 填充返回对象的基本信息
        banreturntype.name = list.name;
        banreturntype.ip = list.ip;
        banreturntype.time = list.time;
        banreturntype.reason = list.reason;
        banreturntype.duration = list.duration;
        banreturntype.pointer = banlist.indexOf(list);
        
        // 检查封禁是否有效（时间大于当前时间或为永久封禁）
        banreturntype.banned = list.time > System.currentTimeMillis() || list.time == 0;
        
        return banreturntype;
    }
    
    /**
     * 检查玩家是否被封禁
     * 
     * @param banlist 封禁列表
     * @param name    玩家名称
     * @param ip      玩家IP地址
     * @return 封禁信息返回对象，包含封禁状态和详细信息
     */
    public static banreturntype isbanned(List<banlisttype> banlist, String name, String ip) {
        // 首先使用快速IP查找
        if (checkipfast(ip) != 0) {
            return bantimecheck(banlist, banlist.get(checkipfast(ip)));
        }
        
        // 如果快速查找未找到，遍历封禁列表
        for (banlisttype list : banlist) {
            // 判断玩家是否被封禁（不区分大小写）
            if (list.name.equalsIgnoreCase(name) || list.ip.equalsIgnoreCase(ip)) {
                // 检查封禁是否有效
                return bantimecheck(banlist, list);
            }
        }
        
        // 如果未找到封禁记录，返回默认的未封禁对象
        banreturntype banreturntype = new banreturntype();
        banreturntype.banned = false;
        banreturntype.name = "";
        banreturntype.ip = "";
        banreturntype.time = 0;
        banreturntype.reason = "";
        banreturntype.duration = 0;
        banreturntype.pointer = -1;
        return banreturntype;
    }
}