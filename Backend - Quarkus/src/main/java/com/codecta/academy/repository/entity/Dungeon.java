package com.codecta.academy.repository.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema="codecta", name = "DUNGEON")
public class Dungeon extends ModelObject{
    @SequenceGenerator(
            name = "dungeonSeq",
            sequenceName = "DUNGEON_SEQ",
            schema = "codecta",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dungeonSeq")
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(schema = "codecta", name = "dungeon_monster",
            joinColumns = { @JoinColumn(name = "dungeon_id") },
            inverseJoinColumns = { @JoinColumn(name = "monster_id") })
    private Monster monster;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(schema = "codecta", name = "dungeon_item",
            joinColumns =
                    { @JoinColumn(name = "dungeon_id") },
            inverseJoinColumns =
                    { @JoinColumn(name = "item_id") })
    private Item item;

    @ManyToOne
    private Map map;

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Monster getMonster() {
        return monster;
    }

    public void setMonster(Monster monster) {
        this.monster = monster;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

}
