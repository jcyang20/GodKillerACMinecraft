// Created by HelloWorldCoder on 2025/5/27 13:38
// MODIFICATION IS NOT ALLOWED
// A Part Of DragonUtils

package DragonUtils;

import java.util.logging.Level;
import static org.bukkit.Bukkit.getLogger;

public final class logging
{
    static
    {
        logging.log(Level.INFO,"&6[DragonUtils] &r","&aSuccessfully Loaded &bModule [logging] &r&ain &r&6&_DragonUtils by HelloWorldCoder-China&r&a !&r");
    }
    public static String ANSIDataBase(char colorCode) // ANSI数据库,手打
    {
        if(colorCode=='0') return "\u001B[30;22m";
        if(colorCode=='1') return "\u001B[34;22m";
        if(colorCode=='2') return "\u001B[32;22m";
        if(colorCode=='3') return "\u001B[36;22m";
        if(colorCode=='4') return "\u001B[31;22m";
        if(colorCode=='5') return "\u001B[35;22m";
        if(colorCode=='6') return "\u001B[33;22m";
        if(colorCode=='7') return "\u001B[37;22m";
        if(colorCode=='8') return "\u001B[30;22m";
        if(colorCode=='9') return "\u001B[34;22m";
        if(colorCode=='a') return "\u001B[32;22m";
        if(colorCode=='b') return "\u001B[36;22m";
        if(colorCode=='c') return "\u001B[31;22m";
        if(colorCode=='d') return "\u001B[35;22m";
        if(colorCode=='e') return "\u001B[33;22m";
        if(colorCode=='f') return "\u001B[37;22m";
        if(colorCode=='k') return "\u001B[5m";
        if(colorCode=='l') return "\u001B[1m";
        if(colorCode=='m') return "\u001B[9m";
        if(colorCode=='n') return "\u001B[4m";
        if(colorCode=='o') return "\u001B[3m";
        if(colorCode=='r') return "\u001B[0m\u001B[0;39m\u001B[24m";
        if(colorCode=='_') return "\u001B[21m";
        if(colorCode=='-') return "&";
        return "\u001B[31;22m\u001B[21m[ERROR]\u001B[0;39m\u001B[31;22m Color Code NOT FOUND: "+colorCode+"!\u001B[0m\u001B[0;39m\u001B[24m";
    }
    public static String ChangeColorCode(String Source)
    {
        return Source.replaceAll("&","§"); // 初始化字符串
    }
    public static String ANSIChange(String Source) // ANSI转换器
    {
        Source= ChangeColorCode(Source);
        while(Source.contains("§")) // 查找
        {
            char colorCode=Source.charAt(Source.indexOf("§")+1);
            Source=Source.replaceAll("§"+colorCode,ANSIDataBase(colorCode)); // 替换
        }
        return Source;
    }
    public static boolean log(Level level,String prefix,String message) // 日志输出
    {
        getLogger().log(level,ANSIChange(prefix+message+"&r"));
        return true;
    }
}