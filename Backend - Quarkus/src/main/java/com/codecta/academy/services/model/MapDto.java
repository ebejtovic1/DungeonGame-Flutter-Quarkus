package com.codecta.academy.services.model;

import java.util.ArrayList;
import java.util.List;

public class MapDto {
    private Integer id;
    private List<DungeonDto> dungeons = new ArrayList<>();
    private Integer currentDungeon;
    private String name;
    private Integer playerId;

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<DungeonDto> getDungeons() {
        return dungeons;
    }

    public void setDungeons(List<DungeonDto> dungeons) {
        this.dungeons = dungeons;
    }

    public Integer getCurrentDungeon() {
        return currentDungeon;
    }

    public void setCurrentDungeon(Integer currentDungeon) {
        this.currentDungeon = currentDungeon;
    }
}
