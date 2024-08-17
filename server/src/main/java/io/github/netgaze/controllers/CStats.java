package io.github.netgaze.controllers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CStats {
    private Connection connection;
    private List<GazeStat> gazeStats = new ArrayList<>();
}
