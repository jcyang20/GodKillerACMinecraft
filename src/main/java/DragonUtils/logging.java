package DragonUtils;

import java.util.logging.Level;
import static org.bukkit.Bukkit.getLogger;

public final class logging
{
    public static String ANSIDataBase(char colorcode)
    {
        if(colorcode=='0') return "\u001B[30;22m";
        if(colorcode=='1') return "\u001B[34;22m";
        if(colorcode=='2') return "\u001B[32;22m";
        if(colorcode=='3') return "\u001B[36;22m";
        if(colorcode=='4') return "\u001B[31;22m";
        if(colorcode=='5') return "\u001B[35;22m";
        if(colorcode=='6') return "\u001B[33;22m";
        if(colorcode=='7') return "\u001B[37;22m";
        if(colorcode=='8') return "\u001B[30;1m";
        if(colorcode=='9') return "\u001B[34;1m";
        if(colorcode=='a') return "\u001B[32;1m";
        if(colorcode=='b') return "\u001B[36;1m";
        if(colorcode=='c') return "\u001B[31;1m";
        if(colorcode=='d') return "\u001B[35;1m";
        if(colorcode=='e') return "\u001B[33;1m";
        if(colorcode=='f') return "\u001B[37;1m";
        if(colorcode=='k') return "\u001B[5m";
        if(colorcode=='l') return "\u001B[21m";
        if(colorcode=='m') return "\u001B[9m";
        if(colorcode=='n') return "\u001B[4m";
        if(colorcode=='o') return "\u001B[3m";
        if(colorcode=='r') return "\u001B[0;39m";
        return "\u001B[31;22m\u001B[21m[ERROR]\u001B[0;39m\u001B[31;1m Color Code NOT FOUND: "+colorcode+"!";
    }
    public static boolean log(Level level,String prefix,String message)
    {
        getLogger().log(level,prefix+message);
        return true;
    }
}