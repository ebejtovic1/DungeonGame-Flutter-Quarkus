package com.codecta.academy;

import com.codecta.academy.repository.entity.Dungeon;
import com.codecta.academy.repository.entity.Player;
import com.codecta.academy.services.QoQService;
import com.codecta.academy.services.model.*;
import com.google.gson.*;

import javax.inject.Inject;
import javax.json.Json;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.http.HttpClient;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Path("/game")
public class QoQResource {

    public class Error {
        public String code;
        public String description;

        public Error(String code, String description) {
            this.code = code;
            this.description = description;
        }
    }

    @Inject
    QoQService qoQService;

    @GET
    @Path("/monsters")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMonsters()
    {
        List<MonsterDto> monsterList = qoQService.findAllMonsters();
        if(monsterList == null || monsterList.isEmpty()) {
            return Response.noContent().build();
        }

        return Response.ok(monsterList).build();


    }

    @GET
    @Path("/player/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlayerById(@PathParam("id") Integer id)
    {
        PlayerDto player = qoQService.findPlayerById(id);
        if(player == null) {
            return Response.status(Response.Status.NOT_FOUND).build();

        }
        return Response.ok(player).build();
    }

    @GET
    @Path("/monster/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMonsterById(@PathParam("id") Integer id)
    {
        MonsterDto player = qoQService.getMonsterById(id);
        if(player == null) {
            return Response.status(Response.Status.NOT_FOUND).build();

        }
        return Response.ok(player).build();
    }


    @GET
    @Path("/maps")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMaps()
    {
        List<MapDto> mapList = qoQService.findAllMaps();
        if(mapList == null || mapList.isEmpty()) {
            return Response.noContent().build();
        }
        return Response.ok(mapList).build();
    }
    @GET
    @Path("/items")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllItems()
    {
        List<ItemDto> itemList = qoQService.findAllItems();
        if(itemList == null || itemList.isEmpty()) {
            return Response.noContent().build();
        }
        return Response.ok(itemList).build();
    }


    @POST
    @Path("/{id}/move")
    @Produces({MediaType.APPLICATION_JSON})
    public Response move(@PathParam("id") Integer id){
        PlayerDto playerDto = qoQService.getPlayerById(id);
        if(playerDto == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        MapDto mapDto = qoQService.getMapById(playerDto.getMapId());
        if(mapDto == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        if(playerDto.getHealth()<=0 || mapDto.getCurrentDungeon()>9 )return Response.status(202).entity(new Object[]{playerDto,new ResponseMessage("Game is over")}).build();

        if(mapDto.getCurrentDungeon()>0) {
            DungeonDto dungeonDto = qoQService.getDungeonById(mapDto.getCurrentDungeon());
            if (dungeonDto == null) return Response.status(Response.Status.NOT_FOUND).build();
            if (dungeonDto.getMonsterId() != null){

                MonsterDto monsterDto = qoQService.getMonsterById(dungeonDto.getMonsterId());
                if(monsterDto.getHealth()>0) return Response.status(200).entity(new ResponseMessage("You can't go any further in the dungeon with the monster.")).build();
            }
        }
        DungeonDto dungeonDto = qoQService.getCurrentDungeon(mapDto);

        if(dungeonDto == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        else if(dungeonDto.getMonsterId() != null) {
            return Response.status(200).entity(new ResponseMessage(dungeonDto.getMonsterId().toString())).build();
        }
        if(dungeonDto.getItemId() != null) {
            ItemDto item = qoQService.getItemNameById(dungeonDto.getItemId());
            if(item==null)return Response.status(Response.Status.BAD_REQUEST).build();
            playerDto = qoQService.collectItem(playerDto, item);
            return Response.status(200).entity(new Object[]{playerDto, new ResponseMessage("You have won an item "+ item.getName()+ ". You can move on.")}).build();
        }
        return Response.status(200).entity(dungeonDto).build();
    }

    @POST
    @Path("{id}/heal")
    @Produces({MediaType.APPLICATION_JSON})
    public Response healPlayer(@PathParam("id") Integer id){
        PlayerDto playerDto = qoQService.getPlayerById(id);
        if(playerDto == null) return Response.status(404).entity(new ResponseMessage("Player with id: "+ id +" not found.")).build();
        MapDto mapDto = qoQService.getMapById(playerDto.getMapId());
        if(mapDto == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        if(playerDto.getHealth()<=0 || mapDto.getCurrentDungeon()>9 )return Response.status(202).entity(new Object[]{playerDto,new ResponseMessage("Game is over")}).build();
        if(playerDto.getHealingPoting() == 0) return Response.status(202).entity(new ResponseMessage("This player has no healing poison.")).build();
        playerDto = qoQService.healPlayer(playerDto);
        if(playerDto == null) return Response.status(Response.Status.BAD_REQUEST).build();
        return Response.ok(playerDto).build();
    }

    @POST
    @Path("{id}/flee")
    @Produces({MediaType.APPLICATION_JSON})
    public Response flee(@PathParam("id") Integer id){
        PlayerDto playerDto = qoQService.getPlayerById(id);
        if(playerDto == null) return Response.status(404).entity(new ResponseMessage("Player with id: "+ id +" not found.")).build();
        MapDto mapDto = qoQService.getMapById(playerDto.getMapId());
        if(mapDto == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        if(playerDto.getHealth()<=0 || mapDto.getCurrentDungeon()>9 )return Response.status(202).entity(new Object[]{playerDto,new ResponseMessage("Game is over")}).build();

        DungeonDto dungeon = qoQService.getDungeonById(mapDto.getCurrentDungeon());
        if(dungeon==null)return Response.status(Response.Status.BAD_REQUEST).build();
        if(dungeon.getMonsterId()==null)return Response.status(202).entity(new ResponseMessage("You are not in a dungeon with a monster. You can't flee")).build();
        if(dungeon.getId()==mapDto.getDungeons().size())return Response.status(202).entity(new ResponseMessage("You can't flee from the final boss.")).build();

        DungeonDto dungeonDto = qoQService.flee(playerDto);
        if(dungeonDto == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        else if(dungeonDto.getMonsterId() != null) {
            return Response.status(200).entity(new ResponseMessage(dungeonDto.getMonsterId().toString())).build();
        }
        if(dungeonDto.getItemId() != null) {

            ItemDto item = qoQService.getItemNameById(dungeonDto.getItemId());
            if(item==null)return Response.status(Response.Status.BAD_REQUEST).build();
            playerDto = qoQService.collectItem(playerDto, item);
            return Response.status(200).entity(new Object[]{playerDto, new ResponseMessage("You have won an item "+ item.getName()+ ". You can move on.")}).build();
        }
        return Response.ok(dungeonDto).build();

    }


    @POST
    @Path("{id}/fight")
    @Produces({MediaType.APPLICATION_JSON})
    public Response fightWithMonster(@PathParam("id") Integer id){
        PlayerDto playerDto = qoQService.getPlayerById(id);
        if(playerDto == null) return Response.status(404).entity(new ResponseMessage("Player with id: "+ id +" not found.")).build();
        MapDto mapDto = qoQService.getMapById(playerDto.getMapId());
        if(mapDto == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        if(playerDto.getHealth()<=0 || mapDto.getCurrentDungeon()>9 )return Response.status(202).entity(new Object[]{playerDto,new ResponseMessage("Game is over")}).build();
        DungeonDto dungeon = qoQService.getDungeonById(mapDto.getCurrentDungeon());
        if(dungeon==null)return Response.status(Response.Status.BAD_REQUEST).build();
        if(dungeon.getMonsterId()==null) return Response.status(202).entity(new ResponseMessage("You are not in a dungeon with a monster. You can't fight")).build();


        MonsterDto monsterDto = qoQService.fightWithMonster(playerDto);
        if(monsterDto==null)return Response.status(Response.Status.BAD_REQUEST).build();
        if(monsterDto.getHealth()<=0){
            if(monsterDto.getItemId()!=null) {
                ItemDto itemDto = qoQService.getItemNameById(monsterDto.getItemId());
                if(itemDto!=null){
                    if(itemDto.getName().equals("Orb of Quarkus"))return Response.status(200).entity(new Object[]{playerDto,monsterDto,new ResponseMessage("You won Orb of Quarkus. Game is over")}).build();
                    else {
                        playerDto = qoQService.collectItem(playerDto, itemDto);
                    }
                    return Response.status(200).entity(new Object[]{playerDto,monsterDto,new ResponseMessage("You defeated the monster and won the item: "+ itemDto.getName()+". You can move on.")}).build();
                }

            }
            return Response.status(200).entity(new Object[]{playerDto,monsterDto,new ResponseMessage("You defeated the monster. You can move on.")}).build();

        }
        else{
            return Response.status(202).entity(new Object[]{playerDto, monsterDto, new ResponseMessage("Game is over. You lost.")}).build();
        }

    }

    @POST
    @Path("/")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createGame()
    {
        MapDto mapa = qoQService.startGame();
        if(mapa != null)
            return Response.ok(new Object[]{mapa, new ResponseMessage("Welcome to Quest for the Orb of Quarkus")}).build();
        return Response.noContent().build();

    }


}