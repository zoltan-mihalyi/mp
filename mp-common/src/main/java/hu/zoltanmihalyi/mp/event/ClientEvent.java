package hu.zoltanmihalyi.mp.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class ClientEvent {
    @Getter
    private int roomId;
}
