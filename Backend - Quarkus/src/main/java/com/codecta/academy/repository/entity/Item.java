package com.codecta.academy.repository.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema="codecta", name = "ITEM")
public class Item extends ModelObject{
    @SequenceGenerator(
            name = "itemSeq",
            sequenceName = "ITEM_SEQ",
            schema = "codecta",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "itemSeq")
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    private String name;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private List<Monster> monsters = new ArrayList<>();

    @OneToOne
    private Dungeon dungeon;

    public Dungeon getDungeon() {
        return dungeon;
    }

    public void setDungeon(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    public List<Monster> getMonsters() {
        return monsters;
    }

    public void setMonsters(List<Monster> monsters) {
        this.monsters = monsters;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    @Override
    public Integer getId() {
        return this.id;
    }
}
