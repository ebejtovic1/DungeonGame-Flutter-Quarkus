package com.codecta.academy.services;

import com.codecta.academy.repository.entity.*;
import com.codecta.academy.services.model.*;

import java.util.List;

public interface QoQService {

    DungeonDto addDungeon(DungeonDto dungeon1);
    List<DungeonDto> findAllDungeons();
    MonsterDto addMonster(MonsterDto monster);
    List<MonsterDto> findAllMonsters();
    List<MapDto> findAllMaps();
    List<ItemDto> findAllItems();
    MapDto startGame();

    MapDto getMapById(Integer id);

    DungeonDto getCurrentDungeon(MapDto mapDto);

    ItemDto getItemNameById(Integer id);

    PlayerDto getPlayerById(Integer id);

    PlayerDto healPlayer(PlayerDto playerDto);

    DungeonDto flee(PlayerDto playerDto);

    DungeonDto getDungeonById(Integer currentDungeon);

    PlayerDto findPlayerById(Integer id);

    MonsterDto fightWithMonster(PlayerDto playerDto);

    PlayerDto collectItem(PlayerDto playerDto, ItemDto itemDto);

    MonsterDto getMonsterById(Integer monsterId);

}
