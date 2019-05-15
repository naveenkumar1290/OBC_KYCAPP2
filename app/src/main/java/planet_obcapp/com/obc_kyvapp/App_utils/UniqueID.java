package planet_obcapp.com.obc_kyvapp.App_utils;

public class UniqueID {
    static long current= System.currentTimeMillis();
    static public synchronized long get(){
        return current++;
    }
}
