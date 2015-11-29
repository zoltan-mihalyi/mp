package hu.zoltanmihalyi.mp.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
public class ServerEvent implements Serializable {
    @Getter
    private int roomId;
}
