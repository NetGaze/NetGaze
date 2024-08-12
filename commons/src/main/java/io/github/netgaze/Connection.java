package io.github.netgaze;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.Duration;

@Data
@NoArgsConstructor
public class Connection {

    @NonNull
    private String name;

    @NonNull
    private String description;

    @NonNull
    private Scheme scheme;

    @NonNull
    private String host;

    private int port;

    @NonNull
    private Duration pollInterval = Duration.ofSeconds(5);

    private Duration timeout = Duration.ofSeconds(10);
}
