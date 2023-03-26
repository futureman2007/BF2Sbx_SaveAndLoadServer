package sbx.revive;

public class Group {

     /*
     * Uses the String (From URL Decoded to UTF-8) send from a bf2 sandbox server when a player uses @netsavegroup.
     * With this Data, it create a Filename in the format <USERNAME>_<PID>-<GROUPNAME>.bf2sbg. The dash is the delimiter between playeridentifiaction and groupidentification.
     * Fileformat: name=<username:STRING>&pid=<playerid:INT>&groupName=groupname<STRING>&data=<GROUP_DATA>
    */   

    /*
     * Uses the String (From URL Decoded to UTF-8) send from a bf2 sandbox server when a player uses @netsavegroup.
     * With this Data, extracts the actual GroupData. Every '!' Marks the end of an element describtion of a Group
     * GRUP_DATA: <Entity_Name>;<comma_separated_position_and_rotation_data>!<Entity_name>;....
     * example_ fueltankwagon_mp;0.0,0.0,0.0;-158.999984741,-0.0,169.999969482!fueltankwagon_mp;0.0,0.0,0.0;-158.999984741,-0.0,169.999969482!
    */

    private String username;
    private String pid;
    private String groupname;
    private String groupdata;

    public Group(String username, String pid, String groupname, String groupdata){

        this.username = username;
        this.pid = pid;
        this.groupname = groupname;
        this.groupdata = groupdata;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPid() {
        return this.pid;
    }


    @Override
    public String toString(){
        return "name=" + this.username + "&"
            + "pid=" + this.pid + "&"
            + "groupname=" + this.groupname + "&"
            + "data=" + this.groupdata;
    }
    
    public String toJSONString() {
        return "{" + 
            " name='" + getUsername() + "'" +  
            ", pid='" + getPid() + "'" + 
            ", groupname='" + getGroupname() + "'" + 
            ", data='" + getGroupdata() + "'" + 
            "}";
    }

    public String getGroupname() {
        return this.groupname;
    }

    public String getGroupdata() {
        return this.groupdata;
    }

    protected void setGroupdata(String data){
        this.groupdata = data;
    }
    
    
}
