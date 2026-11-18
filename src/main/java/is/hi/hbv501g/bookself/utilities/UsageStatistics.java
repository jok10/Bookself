package is.hi.hbv501g.bookself.utilities;

/**
 * Singleton used for tracking number of total user accounts
 */
public class UsageStatistics {
    private static UsageStatistics INSTANCE;

    private int totalUsers = 0;

    private UsageStatistics(){}

    /**
     * Retrieves the only instance of UsageStatistics.
     *
     * @return the UsageStatistics instance
     */
    public static UsageStatistics getInstance(){
        if(INSTANCE == null){
            INSTANCE = new UsageStatistics();
        }
        return INSTANCE;
    }

    /**
     * Safely increments the total user count by one.
     */
    public void incrementTotalUsers(){
        totalUsers++;
    }

    /**
     * Safely decrements the total user count by one.
     */
    public void decrementTotalUsers(){
        if(totalUsers == 0){
            return;
        }
        totalUsers--;
    }

    /**
     * Retrieves the current total user count.
     *
     * @return the total user count
     */
    public int getTotalUsers(){
        return totalUsers;
    }
}
