package com.codecta.academy.services.model;

import com.codecta.academy.repository.entity.Item;
import com.codecta.academy.repository.entity.Map;
import com.codecta.academy.repository.entity.Monster;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class DungeonDto {

    private Integer id;
    private Integer itemId;
    private Integer monsterId;
    private Integer mapId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getMonsterId() {
        return monsterId;
    }

    public void setMonsterId(Integer monsterId) {
        this.monsterId = monsterId;
    }

    public Integer getMapId() {
        return mapId;
    }

    public void setMapId(Integer mapId) {
        this.mapId = mapId;
    }
}
