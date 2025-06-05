// Created by HelloWorldCoder on 2025/5/27 13:38 Last Edited 2025/6/1 9:37
// MODIFICATION IS NOT ALLOWED
// A Part Of DragonUtils

package DragonUtils;

import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.List;
import java.util.logging.Level;

// 时间使用long防止溢出
public class banning
{
    static
    {
        logging.log(Level.INFO,"&6[DragonUtils] &r","&aSuccessfully Loaded &bModule [banning] &r&ain &r&6&_DragonUtils by HelloWorldCoder-China&r&a !&r");
    }

    public static class banlisttype // 封禁信息储存
    {
        public String name;
        public String ip;
        public long time;
        public String reason;
        public int duration;
    }

    public static class banreturntype // 封禁信息返回
    {
        public boolean banned;
        public String name;
        public String ip;
        public long time;
        public String reason;
        public int duration;
        public int pointer;
    }

    public static boolean addban(List<banlisttype> banlist,String name,String ip,long time,String reason,int duration)
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
    public static int ban(List<banlisttype> banlist,String name,String ip,int timemup,int duration,String reason,boolean mercy,long timebefore)
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
    public static banreturntype isbanned(List<banlisttype> banlist,String name,String ip)
    {
        for(banlisttype list:banlist)
        {   // 判断玩家是否被封禁，目前遍历，不知道大佬们有没有更好的解决方法
            if(list.name.equalsIgnoreCase(name) || list.ip.equalsIgnoreCase(ip))
            {
                // 解封时间判定
                if(list.time>System.currentTimeMillis() || list.time==-1)
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