package hu.zoltanmihalyi.mp;

import java.util.HashMap;
import java.util.Map;

import hu.zoltanmihalyi.mp.exception.PrivilegeAlreadyGrantedException;
import hu.zoltanmihalyi.mp.exception.PrivilegeNotFoundException;
import hu.zoltanmihalyi.mp.exception.PrivilegeReuseException;
import hu.zoltanmihalyi.mp.replication.Replicator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class Membership {
    private Map<Class<?>, Privilege> privileges = new HashMap<>();
    @Getter
    private final User user;
    @Getter
    private final Room room;
    @Getter
    @Setter
    private Replicator replicator; //todo should be final?

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

    public void revokePrivilege(Class<?> privilegeType) {
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
        return (T) getPrivilegeAsPrivilege(privilegeType);
    }

    public Privilege getPrivilegeAsPrivilege(Class<?> privilegeType) {
        if (!hasPrivilege(privilegeType)) {
            throw new PrivilegeNotFoundException();
        }
        return privileges.get(privilegeType);
    }

    public void revoke() {
        room.removeUser(user);
    }

    public void update() {
        Object data = replicator.getData();
        if (data != null) {
            user.update(this, data);
        }
    }
}
