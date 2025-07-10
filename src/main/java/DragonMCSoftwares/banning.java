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

// 时间使用long防止溢出
public class banning
{
    static
    {
        logging.log(Level.INFO,"&6[DragonUtils] &r","&aSuccessfully Loaded &bModule [banning] &r&ain &r&6&_DragonUtils by HelloWorldCoder-China&r&a !&r");
    }

    static class fastlookup4
    {
        public static boolean[] ip1=new boolean[257];
        public static boolean[] ip2=new boolean[257];
        public static boolean[] ip3=new boolean[257];
        public static int[] ip4=new int[257];
    }

    fastlookup4 fastlookupv4=new fastlookup4();

    public static void fastipinit(List<banlisttype> banlist)
    {
        for(banlisttype banlistthing:banlist)
        {
            if(banlistthing.ip.contains("."))
            {
                String[] ip=banlistthing.ip.split("\\.");
                fastlookup4.ip1[Integer.parseInt(ip[0])]=true;
                fastlookup4.ip2[Integer.parseInt(ip[1])]=true;
                fastlookup4.ip3[Integer.parseInt(ip[2])]=true;
                fastlookup4.ip4[Integer.parseInt(ip[3])]=banlist.indexOf(banlistthing);
            }
        }
    }

    public static int checkipfast(String ipin)
    {
        if(ipin.contains("."))
        {
            String[] ip=ipin.split("\\.");
            if(fastlookup4.ip1[Integer.parseInt(ip[0])] && fastlookup4.ip2[Integer.parseInt(ip[1])] && fastlookup4.ip3[Integer.parseInt(ip[2])] && fastlookup4.ip4[Integer.parseInt(ip[3])]!=0) return fastlookup4.ip4[Integer.parseInt(ip[3])];
        }
        return 0;
    }

    public static class banlisttype // 封禁信息储存
    {
        public String name;
        public String ip;
        public long time;
        public String reason;
        public long duration;
    }

    public static class banreturntype // 封禁信息返回
    {
        public boolean banned;
        public String name;
        public String ip;
        public long time;
        public String reason;
        public long duration;
        public int pointer;
    }

    public static boolean addban(List<banlisttype> banlist,String name,String ip,long time,String reason,long duration)
    {
        banlisttype banlistthing=new banlisttype();
        banlistthing.name=name;
        banlistthing.ip=ip;
        banlistthing.time=time;
        banlistthing.reason=reason;
        banlistthing.duration=duration;
        banlist.add(banlistthing);
        return true;
    }

    // 封禁函数
    public static int ban(List<banlisttype> banlist,String name,String ip,int timemup,long duration,String reason,boolean mercy,long timebefore)
    {
        int count=-1;
        if(mercy) count=mercywave(banlist,timebefore);
        reason=logging.ChangeColorcode(reason);
        for(banlisttype banlistthing:banlist)
        {
            if(banlistthing.name.equals(name) || banlistthing.ip.equals(ip))
            {
                addban(banlist,name,ip,banlistthing.duration*timemup+duration+System.currentTimeMillis(),reason,banlistthing.duration*timemup+duration);
                return count;
            }
        }
        addban(banlist,name,ip,duration+System.currentTimeMillis(),reason,duration);
        Player banedplayer= Bukkit.getPlayer(name);
        if(banedplayer.isOnline()) banedplayer.kickPlayer(logging.ChangeColorcode(GodKillerAnticheat.banprefix+"\n&b诛仙!你被封印了!"+ "\n&6&k|&r&6&l剩余封印时间&r&6&k| &r&a&n"+ formattimeprd(duration,logging.ChangeColorcode("&byyyy&4年 &bMM&c月 &bdd&e天 | &bHH&2小时 &bmm&a分钟 &bss&9秒"))+"&r"+"\n&c&l理由: &r&e&n"+reason));
        return count;
    }

    // 解封函数
    public static boolean unban(List<banlisttype> banlist,String name,String ip)
    {
        boolean sec=false;
        for(banlisttype banlistthing:banlist)
        {
            if(banlistthing.name.equals(name) || banlistthing.ip.equals(ip))
            {
                banlistthing.time=System.currentTimeMillis()-1;
                sec=true;
            }
        }
        return sec;
    }

    // 彻底解除封禁波
    public static int mercywave(List<banlisttype> banlist,long timebefore)
    {
        int count=0;
        for(banlisttype banlistthing:banlist)
        {
            if(banlistthing.time<=timebefore+System.currentTimeMillis())
            {
                banlist.remove(banlistthing);
                count++;
            }
        }
        return count;
    }

    // 封禁信息查询
    static banreturntype bantimecheck(List<banlisttype> banlist,banlisttype list)
    {
        if(list.time>System.currentTimeMillis() || list.time==0)
        {
            banreturntype banreturntype=new banreturntype();
            banreturntype.banned=true;
            banreturntype.name=list.name;
            banreturntype.ip=list.ip;
            banreturntype.time=list.time;
            banreturntype.reason=list.reason;
            banreturntype.duration=list.duration;
            banreturntype.pointer=banlist.indexOf(list);
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
        return banreturntype;
    }
    public static banreturntype isbanned(List<banlisttype> banlist,String name,String ip)
    {
        if(checkipfast(ip)!=0)
        {
            return bantimecheck(banlist,banlist.get(checkipfast(ip)));
        }
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
        return banreturntype;
    }
}