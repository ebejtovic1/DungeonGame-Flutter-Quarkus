package com.codecta.academy.services.model;

import com.codecta.academy.repository.entity.Dungeon;
import com.codecta.academy.repository.entity.Monster;

import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

public class ItemDto {
    private Integer id;
    private String name;
    private List<MonsterDto> monsters = new ArrayList<>();
    private Integer dungeonId;

    public Integer getDungeonId() {
        return dungeonId;
    }

    public void setDungeonId(Integer dungeonId) {
        this.dungeonId = dungeonId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MonsterDto> getMonsters() {
        return monsters;
    }

    public void setMonsters(List<MonsterDto> monsters) {
        this.monsters = monsters;
    }
}
