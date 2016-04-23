package hu.zoltanmihalyi.mp;

import hu.zoltanmihalyi.mp.exception.UserAlreadyAddedException;
import hu.zoltanmihalyi.mp.replication.Replicator;
import hu.zoltanmihalyi.mp.exception.UserNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Room {
    private Map<User, Membership> users = new HashMap<>();

    public Membership addUser(User user) {
        if (contains(user)) {
            throw new UserAlreadyAddedException();
        }
        Membership membership = new Membership(user, this);
        users.put(user, membership);
        user.join(membership);
        onJoin(membership);
        return membership;
    }

    public boolean contains(User user) {
        return users.containsKey(user);
    }

    protected abstract void onJoin(Membership membership);

    public void removeUser(User user) {
        Membership membership = getMembershipOf(user);
        users.remove(user);
        user.leave(membership);
    }

    public List<User> getUsers() {
        return new ArrayList<>(users.keySet());
    }

    public int getUsersCount() {
        return users.size();
    }

    public Membership getMembershipOf(User user) {
        if (!users.containsKey(user)) {
            throw new UserNotFoundException();
        }
        return users.get(user);
    }

    public void update() {
        MultiMap<Replicator, Membership> replicators = new MultiMap<>();

        for (Membership membership : users.values()) {
            replicators.put(membership.getReplicator(), membership);
        }

        for (Map.Entry<Replicator, List<Membership>> replicatorMemberships : replicators.entrySet()) {
            Object data = replicatorMemberships.getKey().getData();
            if (data != null) {
                for (Membership membership : replicatorMemberships.getValue()) {
                    membership.update(data);
                }
            }
        }
    }
}
