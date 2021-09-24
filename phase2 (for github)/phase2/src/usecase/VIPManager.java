package usecase;

import entity.VIPData;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class VIPManager implements Serializable, Savable {

    private List<VIPData> vips;

    /**
     * Constructs an VIPManager with its VIPs predetermined.
     */
    public VIPManager() { this.vips = new ArrayList<VIPData>(); }

    /**
     * Constructs an VIPManager with its vips predetermined.
     * @param vips The list of all existing VIPs
     */
    public VIPManager(List<VIPData> vips) { this.vips = vips; }

    /**
     * Verify if the VIP exists in VIPManager.
     * @param username The VIP's displayname
     * @return True iff the VIP exists in the list of VIPs
     */
    public boolean checkVIPExists(String username) {
        for (VIPData vip : this.vips) {
            if (vip.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verify if the attendee exists in VIPManager.
     * @param vip The VIP
     * @return True iff the VIP exists in the list of VIPs
     */
    public boolean checkAttendeeExists(VIPData vip) { return this.vips.contains(vip); }

    /**
     * Returns an VIP's data given the VIP's username.
     * @param username The VIP's username
     * @return Returns the VIP's data iff the VIP exists in the list of VIPs
     */
    public VIPData getVIPByID(String username) {
        for (VIPData vip : this.vips) {
            if (vip.getUsername().equals(username)) {
                return vip;
            }
        }
        return null;
    }

    /* TODO: This needs modification for phase 2 */
    /**
     * Add a new VIP to VIPs.
     * @param vip The new VIP
     * @return Returns true iff the new VIP has been added to VIPs
     */
    public boolean addVIP(VIPData vip) {
        // Check if the VIP is already in the list or not
        if (checkAttendeeExists(vip)) {
            return false;
        }
        vips.add(vip);
        return true;
    }

    /**
     * Return a list of usernames of all existing VIPs.
     * @return The list of usernames of all existing VIPs.
     */
    public List<String> getVIPsUsernames() {
        List<String> vipNames = new ArrayList<>();

        for (VIPData attendee : vips) {
            vipNames.add(attendee.getUsername());
        }

        return vipNames;
    }

    public List<VIPData> getAttendees() {
        return vips;
    }
}
