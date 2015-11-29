package hu.zoltanmihalyi.mp;

import lombok.Getter;
import lombok.Setter;

public class Privilege {
    private Membership membership;
    @Getter
    @Setter
    private boolean active = true;

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
