package is.hi.hbv501g.bookself.utilities;

import java.util.concurrent.atomic.AtomicInteger;

public class UsageStatistics {
    private static UsageStatistics INSTANCE;

    private int totalUsers = 0;

    private UsageStatistics(){}

    public static UsageStatistics getInstance(){
        if(INSTANCE == null){
            INSTANCE = new UsageStatistics();
        }
        return INSTANCE;
    }

    public void incrementTotalUsers(){
        totalUsers++;
    }

    public void decrementTotalUsers(){
        if(totalUsers == 0){
            return;
        }
        totalUsers--;
    }

    public int getTotalUsers(){
        return totalUsers;
    }
}
