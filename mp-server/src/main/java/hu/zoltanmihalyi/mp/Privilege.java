package hu.zoltanmihalyi.mp;

public class Privilege {
    private Membership membership;

    public Membership getMembership() {
        return membership;
    }

    void setMembership(Membership membership) {
        this.membership = membership;
    }

    public boolean hasMembership() {
        return membership != null;
    }
}
