package com.codecta.academy.services.impl;

import com.codecta.academy.repository.*;
import com.codecta.academy.repository.entity.*;
import com.codecta.academy.services.QoQService;
import com.codecta.academy.services.model.*;
import org.modelmapper.ModelMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
@Transactional
public class QoQServiceImpl implements QoQService {

    @Inject
    MapRepository mapRepository;
    @Inject
    ItemRepository itemRepository;
    @Inject
    DungeonRepository dungeonRepository;
    @Inject
    MonsterRepository monsterRepository;
    @Inject
    PlayerRepository playerRepository;



    @Override
    public List<MonsterDto> findAllMonsters(){
        List<Monster> monsters = monsterRepository.findAll();
        if(monsters== null || monsters.isEmpty()) {
            return  null;
        }
        List<MonsterDto> monsterDtoList = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        for (Monster monster :
                monsters) {
            MonsterDto monsterDto = modelMapper.map(monster, MonsterDto.class);
            monsterDtoList.add(monsterDto);
        }
        return monsterDtoList;
    }

    @Override
    public List<MapDto> findAllMaps() {
        List<Map> maps = mapRepository.findAll();
        if(maps == null || maps.isEmpty()) {
            return  null;
        }
        List<MapDto> mapDtoList = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        for (Map map :
                maps) {
            MapDto mapDto = modelMapper.map(map, MapDto.class);
            mapDtoList.add(mapDto);
        }
        return mapDtoList;
    }

    @Override
    public List<ItemDto> findAllItems() {
        List<Item> items = itemRepository.findAll();
        if(items == null || items.isEmpty()) {
            return  null;
        }
        List<ItemDto> itemDtoList = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        for (Item item :
                items) {
            //ItemDto itemDto = modelMapper.map(item, ItemDto.class);
            itemDtoList.add(modelMapper.map(item, ItemDto.class));
        }
        return itemDtoList;
    }

    private List<ModelObject> createMonstersAndItems() {

        List<ModelObject> models = new ArrayList<>();
        List<Item> items = new ArrayList<>();
        Item item1 = new Item();
        item1.setName("Health increase");
        models.add(item1);
        items.add(item1);

        Item item2 = new Item();
        item2.setName("Damage increase");
        models.add(item2);
        items.add(item2);

        Item item3 = new Item();
        item3.setName("Healing potion");
        models.add(item3);
        items.add(item3);
        models.add(item3);
        items.add(item3);
        models.add(item3);
        items.add(item3);

        for(int i=0; i<((int)(Math.random()*(6)+5));i++){
            Monster monster = new Monster();
            monster.setName("Monster_" + i);
            monster.setHealth((int)(Math.random()*(21)+50));
            monster.setDamage((int)(Math.random()*(5)+1));

            //monster.setItem(items.get((int)(Math.random()*3)));
            monster.setItem(null);
            models.add(monster);
        }
        Collections.shuffle(models);
        return models;
    }


    @Override
    public MapDto startGame() {


        Map map = new Map();
        Player player = new Player();
        player.setHealth(100);
        player.setName("New player");
        player.setDamage(10);
        player.setHealingPoting(15);
        player.setMap(map);
        playerRepository.add(player);
        map.setName("Map 1");
        map.setPlayer(player);
        map.setCurrentDungeon(0);

        List<MonsterDto> monsterDtoList = new ArrayList<>();
        List<ItemDto> itemDtoList = new ArrayList<>();
        List<ModelObject> objects = createMonstersAndItems();
        Collections.shuffle(objects);
        List<DungeonDto> dungeonDtoList = new ArrayList();
        for(int i=0; i<8; i++){

            if(objects.get(i) instanceof Monster){
                Monster monster = (Monster) objects.get(i);
                monsterRepository.add(monster);
                ModelMapper mapper = new ModelMapper();
                Dungeon dungeon = new Dungeon();
                dungeon.setMap(map);
                monsterDtoList.add(mapper.map(monster, MonsterDto.class));

                dungeon.setMonster(monsterRepository.findById(monster.getId()));
                dungeon = dungeonRepository.add(dungeon);
                if(itemDtoList.size()>0) {
                    Item item = itemRepository.findById((int)(Math.random()*(itemDtoList.size())));
                    if(item!=null) {
                        item.getMonsters().add(monster);
                        item = itemRepository.save(item);
                        monster.setItem(item);
                    }
                    else
                        monster.setItem(null);
                }
                monster = monsterRepository.findById(monster.getId());
                monster.setDungeon(dungeon);
                monsterRepository.save(monster);
                dungeonDtoList.add(mapper.map(dungeon, DungeonDto.class));
            }
            if(objects.get(i) instanceof Item){
                Item item = (Item) objects.get(i);
                itemRepository.add(item);
                ModelMapper mapper = new ModelMapper();
                Dungeon dungeon = new Dungeon();
                dungeon.setMap(map);
                itemDtoList.add(mapper.map(item, ItemDto.class));

                dungeon.setItem(itemRepository.findById(item.getId()));
                dungeon = dungeonRepository.add(dungeon);
                item = itemRepository.findById(item.getId());
                item.setDungeon(dungeon);
                itemRepository.save(item);
                dungeonDtoList.add(mapper.map(dungeon, DungeonDto.class));
            }
        }
        Monster finalMonster = new Monster();
        finalMonster.setName("Final boss");
        finalMonster.setHealth((int)(Math.random()*(101)+100));
        finalMonster.setDamage((int)(Math.random()*(3)+3));
        Item item = new Item();
        item.setName("Orb of Quarkus");

        item.getMonsters().add(finalMonster);
        itemRepository.save(item);
        finalMonster.setItem(item);
        monsterRepository.add(finalMonster);
        ModelMapper mapper = new ModelMapper();
        monsterDtoList.add(mapper.map(finalMonster, MonsterDto.class));

        Dungeon dungeon = new Dungeon();
        dungeon.setMap(map);
        monsterDtoList.add(mapper.map(finalMonster, MonsterDto.class));

        dungeon.setMonster(monsterRepository.findById(finalMonster.getId()));
        dungeon = dungeonRepository.add(dungeon);
        finalMonster = monsterRepository.findById(finalMonster.getId());
        finalMonster.setDungeon(dungeon);
        monsterRepository.save(finalMonster);
        dungeonDtoList.add(mapper.map(dungeon, DungeonDto.class));



        for (DungeonDto dungeon1:
                dungeonDtoList) {
            Dungeon dungeon2 = dungeonRepository.findById(dungeon1.getId());
            map.getDungeons().add(dungeon2);
        }
        map = mapRepository.add(map);
        return mapper.map(map, MapDto.class);
    }

    @Override
    public MapDto getMapById(Integer id) {
        Map map = mapRepository.findById(id);
        ModelMapper mapper = new ModelMapper();
        if (map != null)
            return mapper.map(map, MapDto.class);
        return null;
    }

    @Override
    public DungeonDto getCurrentDungeon(MapDto mapDto) {

        if(mapDto==null)return null;
        Map map = mapRepository.findById(mapDto.getId());
        map.setCurrentDungeon(map.getCurrentDungeon()+1);
        mapRepository.save(map);
        Dungeon dungeon = dungeonRepository.findById(map.getCurrentDungeon());
        if(dungeon==null)return null;
        ModelMapper mapper = new ModelMapper();
        return mapper.map(dungeon, DungeonDto.class);

    }

    @Override
    public ItemDto getItemNameById(Integer id) {
        if(id==null)return null;
        Item item = itemRepository.findById(id);
        if(item==null)return null;
        ModelMapper mapper = new ModelMapper();
        return mapper.map(item, ItemDto.class);
    }

    @Override
    public PlayerDto getPlayerById(Integer id) {
        Player player = playerRepository.findById(id);
        ModelMapper mapper = new ModelMapper();
        if (player != null)
            return mapper.map(player, PlayerDto.class);
        return null;
    }

    @Override
    public PlayerDto healPlayer(PlayerDto playerDto) {

        Player player = playerRepository.findById(playerDto.getId());
        if(player==null || player.getHealingPoting()==null)return null;
        player.setHealth(player.getHealth()+player.getHealingPoting());
        player.setHealingPoting(0);
        player = playerRepository.save(player);
        ModelMapper mapper = new ModelMapper();
        return mapper.map(player, PlayerDto.class);
    }

    @Override
    public DungeonDto flee(PlayerDto playerDto) {
        Player player = playerRepository.findById(playerDto.getId());
        if(player==null)return null;
        player.setHealingPoting(0);
        player.setHealth(player.getHealth()-(int)(0.2*player.getHealth()));
        player.setDamage(player.getDamage()-1);
        player = playerRepository.save(player);
        ModelMapper mapper = new ModelMapper();
        playerDto = mapper.map(player, PlayerDto.class);
        MapDto mapDto = getMapById(playerDto.getMapId());
        return getCurrentDungeon(mapDto);
    }

    @Override
    public MonsterDto fightWithMonster(PlayerDto playerDto) {
        Player player = playerRepository.findById(playerDto.getId());
        if(player==null)return null;
        //mapa u kojoj je player
        ModelMapper mapper = new ModelMapper();
        playerDto = mapper.map(player, PlayerDto.class);
        MapDto mapDto = getMapById(playerDto.getMapId());

        //dungeon u kojem je igrač trenutno
        Dungeon dungeon = dungeonRepository.findById(mapDto.getCurrentDungeon());
        if(dungeon==null || dungeon.getMonster()==null)return null;
        Monster monster = monsterRepository.findById(dungeon.getMonster().getId());

        //fight
        while (monster.getHealth()>0 && player.getHealth()>0){
            monster.setHealth(monster.getHealth() - player.getDamage() * ((int)(Math.random()*(6)+1))/5);
            player.setHealth(player.getHealth() - monster.getDamage() * ((int)(Math.random()*(6)+1))/5);
        }
        player = playerRepository.save(player);
        monster = monsterRepository.save(monster);
        return mapper.map(monster, MonsterDto.class);

    }

    @Override
    public PlayerDto collectItem(PlayerDto playerDto, ItemDto itemDto) {
        Player player = playerRepository.findById(playerDto.getId());
        if(player==null)return null;
        if(itemDto.getName().equals("Health increase"))player.setHealth(player.getHealth()+ ((int)(Math.random()*(11)+10)));
        else if(itemDto.getName().equals("Damage increase"))player.setDamage(player.getDamage()+((int)(Math.random()*(11)+5)));
        else player.setHealingPoting(((int)(Math.random()*(11)+10)));
        player = playerRepository.save(player);
        ModelMapper mapper = new ModelMapper();
        return mapper.map(player, PlayerDto.class);
    }

    @Override
    public MonsterDto getMonsterById(Integer monsterId) {
        Monster monster = monsterRepository.findById(monsterId);
        ModelMapper mapper = new ModelMapper();
        if (monster != null)
            return mapper.map(monster, MonsterDto.class);
        return null;
    }

    @Override
    public DungeonDto getDungeonById(Integer currentDungeon) {
        Dungeon dungeon = dungeonRepository.findById(currentDungeon);
        ModelMapper mapper = new ModelMapper();
        if (dungeon != null)
            return mapper.map(dungeon, DungeonDto.class);
        return null;
    }

    @Override
    public PlayerDto findPlayerById(Integer id) {
        Player player = playerRepository.findById(id);
        ModelMapper mapper = new ModelMapper();
        if (player != null)
            return mapper.map(player, PlayerDto.class);
        return null;
    }




    @Override
    public List<DungeonDto> findAllDungeons() {
        List<Dungeon> dungeonsList = dungeonRepository.findAll();
        if(dungeonsList == null || dungeonsList.isEmpty()) {
            return null;
        }
        List<DungeonDto> dungeonDtoList = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        for(Dungeon character : dungeonsList) {
            dungeonDtoList.add(modelMapper.map(character, DungeonDto.class));
        }
        return dungeonDtoList;
    }

    @Override
    public MonsterDto addMonster(MonsterDto monster) {
        if(monster.getItemId() == null) { //check if can without this
            return null;
        }
        Item item = itemRepository.findById(monster.getItemId());
        if(item == null) {
            return null;
        }
        ModelMapper modelMapper = new ModelMapper();
        Monster newMonster = modelMapper.map(monster, Monster.class);
        newMonster.setItem(item);
        newMonster = monsterRepository.add(newMonster);
        return modelMapper.map(newMonster, MonsterDto.class);
    }



    @Override
    public DungeonDto addDungeon(DungeonDto dungeonDto) {

        ModelMapper modelMapper = new ModelMapper();
        Dungeon dungeon = modelMapper.map(dungeonDto, Dungeon.class);

        Map map = new Map();
        if(dungeonDto.getMapId()!=null) {
            map = mapRepository.findById(dungeonDto.getMapId());
        }

        if (map != null) {
            dungeon.setMap(map);
        }


        Monster monster = new Monster();
        if(dungeonDto.getMonsterId()!=null) {
            monster = monsterRepository.findById(dungeonDto.getMonsterId());
        }

        if (monster != null) {
            dungeon.setMonster(monster);
        }

        Item item = new Item();
        if(dungeonDto.getItemId()!=null) {
            item = itemRepository.findById(dungeonDto.getItemId());
        }

        if (item != null) {
            dungeon.setItem(item);
        }

        dungeon = dungeonRepository.add(dungeon);
        return modelMapper.map(dungeon, DungeonDto.class);
    }


}
