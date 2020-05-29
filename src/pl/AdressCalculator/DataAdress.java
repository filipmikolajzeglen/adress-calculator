package pl.AdressCalculator;

public class DataAdress {

    private String ipadress;
    private String networkadress;
    private String broadcastadress;
    private String classadress;
    private String fullmask;
    private String firsthost;
    private  String lasthost;
    private  String maxhosts;

    public DataAdress() {}

    public DataAdress(String ipadress, String networkadress,
                      String broadcastadress, String classadress,
                      String fullmask, String firsthost, String lasthost,
                      String maxhosts) {
        this.ipadress = ipadress;
        this.networkadress = networkadress;
        this.broadcastadress = broadcastadress;
        this.classadress = classadress;
        this.fullmask = fullmask;
        this.firsthost = firsthost;
        this.lasthost = lasthost;
        this.maxhosts = maxhosts;
    }

    public String getIpadress() {
        return ipadress;
    }

    public void setIpadress(String ipadress) {
        this.ipadress = ipadress;
    }

    public String getNetworkadress() {
        return networkadress;
    }

    public void setNetworkadress(String networkadress) {
        this.networkadress = networkadress;
    }

    public String getBroadcastadress() {
        return broadcastadress;
    }

    public void setBroadcastadress(String broadcastadress) {
        this.broadcastadress = broadcastadress;
    }

    public String getClassadress() {
        return classadress;
    }

    public void setClassadress(String classadress) {
        this.classadress = classadress;
    }

    public String getFullmask() {
        return fullmask;
    }

    public void setFullmask(String fullmask) {
        this.fullmask = fullmask;
    }

    public String getFirsthost() {
        return firsthost;
    }

    public void setFirsthost(String firsthost) {
        this.firsthost = firsthost;
    }

    public String getLasthost() {
        return lasthost;
    }

    public void setLasthost(String lasthost) {
        this.lasthost = lasthost;
    }

    public String getMaxhosts() {
        return maxhosts;
    }

    public void setMaxhosts(String maxhosts) {
        this.maxhosts = maxhosts;
    }
}
