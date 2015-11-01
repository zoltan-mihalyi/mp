package hu.zoltanmihalyi.mp;

import java.util.HashMap;
import java.util.Map;

import hu.zoltanmihalyi.mp.privilege.PrivilegeAlreadyGrantedException;
import hu.zoltanmihalyi.mp.privilege.PrivilegeNotFoundException;
import hu.zoltanmihalyi.mp.privilege.PrivilegeReuseException;

public class Membership {
    private Map<Class<?>, Object> privileges = new HashMap<>();
    private User user;

    public Membership(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public <T extends Privilege> void grant(Class<? super T> privilegeType, T privilege) {
        if (hasPrivilege(privilegeType)) {
            throw new PrivilegeAlreadyGrantedException();
        }
        if (privilege.hasMembership()) {
            throw new PrivilegeReuseException();
        }
        privilege.setMembership(this);
        privileges.put(privilegeType, privilege);
    }

    public void revoke(Class<?> privilegeType) {
        if (!hasPrivilege(privilegeType)) {
            throw new PrivilegeNotFoundException();
        }
        privileges.remove(privilegeType);
    }

    public boolean hasPrivilege(Class<?> privilegeType) {
        return privileges.containsKey(privilegeType);
    }

    @SuppressWarnings("unchecked")
    public <T> T getPrivilege(Class<T> privilegeType) {
        if (!hasPrivilege(privilegeType)) {
            throw new PrivilegeNotFoundException();
        }
        return (T) privileges.get(privilegeType);
    }
}
