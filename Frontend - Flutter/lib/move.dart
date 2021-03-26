import 'package:QoQ/fight.dart';
import 'package:flutter/material.dart';
import './chartLine.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class Move extends StatefulWidget {
  int health;
  int healingPotion;
  int damage;
  String playerName;
  int playerId;
  int mapId;
  String title;
  Move(
      {this.health,
      this.healingPotion,
      @required this.playerName,
      this.damage,
      this.playerId,
      this.mapId,
      this.title});

  @override
  _MoveState createState() => _MoveState();
}

class _MoveState extends State<Move> {
  void healing() {
    setState(() {
      String url = 'http://10.0.2.2:8080/game/${widget.playerId}/heal';
      widget.health += widget.healingPotion;
      widget.healingPotion = 0;
      http.post(url).then((response) {});
    });
  }

  void fight(BuildContext ctx) {
    String url = 'http://10.0.2.2:8080/game/${widget.playerId}/move';
    http.post(url).then((response) {
      if (response.body.length > 20) {
        setState(() {
          widget.title = json.decode(response.body)[1]['message'];
          widget.damage = json.decode(response.body)[0]['damage'];
          widget.health = json.decode(response.body)[0]['health'];
          widget.playerId = json.decode(response.body)[0]['id'];
          widget.healingPotion = json.decode(response.body)[0]['healingPoting'];
        });
      } else {
        String monsterId = json.decode(response.body)['message'];
        String url2 = 'http://10.0.2.2:8080/game/monster/$monsterId';

        http.get(url2).then((response1) {
          int monsterHealth = json.decode(response1.body)['health'];
          if (monsterHealth < 0) monsterHealth += 50;
          Navigator.of(ctx).pushReplacement(
            MaterialPageRoute(
              builder: (c) {
                return Fight(
                    healingPotion: widget.healingPotion,
                    health: widget.health,
                    playerName: widget.playerName,
                    damage: widget.damage,
                    playerId: widget.playerId,
                    monsterName: json.decode(response1.body)['name'],
                    healthMonster: monsterHealth,
                    damageMonster: json.decode(response1.body)['damage']);
              },
            ),
          );
        });
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    double ratio = 1 - ((300 - 100) / 300);
    if (ratio <= 0 || ratio > 1) ratio = 0.5;

    return Scaffold(
        body: Container(
      decoration: BoxDecoration(
        image: DecorationImage(
          image: AssetImage("assets/images/galaxy.jpg"),
          fit: BoxFit.cover,
        ),
      ),
      child: Column(
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.start,
            children: [],
          ),
          Align(
            alignment: Alignment.topLeft,
            child: Container(
              width: 200,
              margin: EdgeInsets.fromLTRB(10, 40, 0, 0),
              decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(10),
                  color: Color.fromRGBO(0, 0, 0, 0.5)),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Container(
                      margin: EdgeInsets.fromLTRB(0, 10, 0, 0),
                      child: Column(
                        children: [
                          ChartLine(
                              title: 'Health',
                              number: widget.health,
                              rate: ratio,
                              name: widget.playerName)
                        ],
                      )),
                  Container(
                    margin: EdgeInsets.fromLTRB(0, 10, 0, 0),
                    child: Text(
                      'Healing potion: ${widget.healingPotion}',
                      style: TextStyle(
                          color: Colors.white,
                          fontSize: 16,
                          fontFamily: 'Emotion'),
                    ),
                  ),
                  Container(
                    margin: EdgeInsets.fromLTRB(0, 10, 0, 20),
                    child: Text(
                      'Damage: ${widget.damage}',
                      style: TextStyle(
                          color: Colors.white,
                          fontSize: 16,
                          fontFamily: 'Emotion'),
                    ),
                  ),
                ],
              ),
            ),
          ),
          Container(
            width: double.infinity,
            padding: EdgeInsets.all(11),
            decoration: BoxDecoration(color: Color.fromRGBO(0, 0, 0, 0.6)),
            margin: EdgeInsets.fromLTRB(0, 40, 0, 0),
            child: Center(
              child: Text(
                widget.title ?? '',
                style: TextStyle(
                    color: Colors.white, fontSize: 27, fontFamily: 'Great'),
              ),
            ),
          ),
          Container(
            margin: EdgeInsets.fromLTRB(0, 30, 0, 30),
            child: Image.network(
                'https://media.giphy.com/media/3o7aCPMbB9gjGQq3gA/giphy.gif'),
          ),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceAround,
            children: [
              SizedBox(
                width: 100.0,
                height: 50.0,
                child: RaisedButton(
                  shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(20)),
                  color: Color.fromRGBO(0, 0, 0, 0.2),
                  onPressed: () => fight(context),
                  child: Icon(Icons.directions_walk,
                      color: Colors.white70, size: 30.0),
                ),
              ),
              SizedBox(
                width: 100.0,
                height: 50.0,
                child: RaisedButton(
                  shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(20)),
                  color: Color.fromRGBO(0, 0, 0, 0.3),
                  onPressed: healing,
                  child: Icon(
                    Icons.healing,
                    color: Colors.white70,
                    size: 30.0,
                  ),
                ),
              ),
            ],
          ),
        ],
      ),
    ));
  }
}
