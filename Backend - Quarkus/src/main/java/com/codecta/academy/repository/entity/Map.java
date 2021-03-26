package com.codecta.academy.repository.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema="codecta", name = "MAP")
public class Map extends ModelObject{

    @SequenceGenerator(
            name = "mapSeq",
            sequenceName = "MAP_SEQ",
            schema = "codecta",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mapSeq")
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @OneToMany(mappedBy = "map", fetch = FetchType.LAZY)
    private List<Dungeon> dungeons = new ArrayList<>();

    private Integer currentDungeon;
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(schema = "codecta", name = "map_player",
            joinColumns = { @JoinColumn(name = "map_id") },
            inverseJoinColumns = { @JoinColumn(name = "player_id") })
    private Player player;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
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

    public List<Dungeon> getDungeons() {
        return dungeons;
    }

    public void setDungeons(List<Dungeon> dungeons) {
        this.dungeons = dungeons;
    }

    public Integer getCurrentDungeon() {
        return currentDungeon;
    }

    public void setCurrentDungeon(Integer currentDungeon) {
        this.currentDungeon = currentDungeon;
    }

    @Override
    public Integer getId() {
        return this.id;
    }



}
