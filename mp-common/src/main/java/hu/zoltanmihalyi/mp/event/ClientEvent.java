package hu.zoltanmihalyi.mp.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
public class ClientEvent implements Serializable {
    @Getter
    private int membershipId;
}
