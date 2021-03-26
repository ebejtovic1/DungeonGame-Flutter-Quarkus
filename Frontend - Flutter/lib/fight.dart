import './lost.dart';
import './win.dart';
import 'package:flutter/material.dart';
import './chartLine.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import './move.dart';

class Fight extends StatefulWidget {
  int health;
  int healingPotion;
  int damage;
  String playerName;
  String monsterName;
  int damageMonster;
  int healthMonster;
  int playerId;
  int mapId;

  Fight(
      {this.health,
      this.healingPotion,
      this.damage,
      this.playerName,
      this.monsterName,
      this.damageMonster,
      this.healthMonster,
      this.playerId,
      this.mapId});

  @override
  _FightState createState() => _FightState();
}

class _FightState extends State<Fight> {
  void healing() {
    setState(() {
      String url = 'http://10.0.2.2:8080/game/${widget.playerId}/heal';
      widget.health += widget.healingPotion;
      widget.healingPotion = 0;
      http.post(url).then((response) {});
    });
  }

  void fight(BuildContext ctx) {
    String url = 'http://10.0.2.2:8080/game/${widget.playerId}/fight';
    http.post(url).then((response) {
      String contains = json.decode(response.body)[2]['message'];
      if (contains.contains('lost')) {
        Navigator.of(ctx).pushReplacement(
          MaterialPageRoute(
            builder: (c) {
              return Lost();
            },
          ),
        );
      } else if (contains.contains('Quarkus')) {
        Navigator.of(ctx).pushReplacement(
          MaterialPageRoute(
            builder: (c) {
              return Win();
            },
          ),
        );
      } else {
        String url1 = 'http://10.0.2.2:8080/game/player/${widget.playerId}';

        http.get(url1).then((response1) {
          Navigator.of(ctx).pushReplacement(
            MaterialPageRoute(
              builder: (c) {
                return Move(
                  healingPotion: json.decode(response1.body)['healingPoting'],
                  health: json.decode(response1.body)['health'],
                  playerName: widget.playerName,
                  damage: json.decode(response1.body)['damage'],
                  playerId: json.decode(response1.body)['id'],
                  mapId: json.decode(response1.body)['mapId'],
                  title: json.decode(response.body)[2]['message'],
                );
              },
            ),
          );
        });
      }
    });
  }

  void flee(BuildContext ctx) {
    if (widget.monsterName == 'Final boss') {
    } else {
      String url = 'http://10.0.2.2:8080/game/${widget.playerId}/flee';
      http.post(url).then((response) {
        if (response.body.length > 20) {
          String url1 = 'http://10.0.2.2:8080/game/player/${widget.playerId}';
          http.get(url1).then((response1) {
            Navigator.of(ctx).pushReplacement(
              MaterialPageRoute(
                builder: (c) {
                  return Move(
                    healingPotion: json.decode(response1.body)['healingPoting'],
                    health: json.decode(response1.body)['health'],
                    playerName: widget.playerName,
                    damage: json.decode(response1.body)['damage'],
                    playerId: json.decode(response1.body)['id'],
                    mapId: json.decode(response1.body)['mapId'],
                    title: json.decode(response.body)[1]['message'],
                  );
                },
              ),
            );
          });
        } else {
          String url1 = 'http://10.0.2.2:8080/game/player/${widget.playerId}';
          setState(() {
            http.get(url1).then((response1) {
              widget.health = json.decode(response1.body)['health'];
              widget.healingPotion =
                  json.decode(response1.body)['healingPoting'];
              widget.damage = json.decode(response1.body)['damage'];
              String monsterId = json.decode(response.body)['message'];
              String url2 = 'http://10.0.2.2:8080/game/monster/$monsterId';

              http.get(url2).then((response2) {
                Navigator.of(ctx).pushReplacement(
                  MaterialPageRoute(
                    builder: (c) {
                      return Fight(
                          healingPotion: widget.healingPotion,
                          health: widget.health,
                          playerName: widget.playerName,
                          damage: widget.damage,
                          playerId: widget.playerId,
                          monsterName: json.decode(response2.body)['name'],
                          healthMonster: json.decode(response2.body)['health'],
                          damageMonster: json.decode(response2.body)['damage']);
                    },
                  ),
                );
              });
            });
          });
        }
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    double ratio = 1 - ((300 - 100) / 300);
    double ratioMonster = 1 - ((300 - 70) / 300);

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
          Container(
            margin: EdgeInsets.fromLTRB(0, 40, 0, 0),
            decoration: BoxDecoration(
                borderRadius: BorderRadius.circular(20),
                color: Color.fromRGBO(0, 0, 0, 0.5)),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              children: [
                Column(
                  children: [
                    Container(
                        margin: EdgeInsets.fromLTRB(0, 10, 0, 0),
                        alignment: Alignment.topLeft,
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
                      alignment: Alignment.topLeft,
                      child: Text(
                        'Damage: ${widget.damage}',
                        style: TextStyle(
                            color: Colors.white,
                            fontSize: 16,
                            fontFamily: 'Emotion'),
                      ),
                    ),
                    Container(
                      margin: EdgeInsets.fromLTRB(0, 10, 0, 20),
                      alignment: Alignment.topLeft,
                      child: Text(
                        'Healing potion: ${widget.healingPotion}',
                        style: TextStyle(
                            color: Colors.white,
                            fontSize: 16,
                            fontFamily: 'Emotion'),
                      ),
                    ),
                  ],
                ),
                Column(
                  children: [
                    Container(
                        margin: EdgeInsets.fromLTRB(0, 10, 0, 0),
                        alignment: Alignment.topLeft,
                        child: Column(
                          children: [
                            ChartLine(
                                title: 'Health',
                                number: widget.healthMonster,
                                rate: ratioMonster,
                                name: widget.monsterName)
                          ],
                        )),
                    Container(
                      margin: EdgeInsets.fromLTRB(0, 10, 0, 0),
                      alignment: Alignment.topLeft,
                      child: Text(
                        'Damage: ${widget.damageMonster}',
                        style: TextStyle(
                            color: Colors.white,
                            fontSize: 16,
                            fontFamily: 'Emotion'),
                      ),
                    ),
                    Container(
                      margin: EdgeInsets.fromLTRB(0, 10, 0, 20),
                      alignment: Alignment.topLeft,
                      child: Text(
                        '',
                        style: TextStyle(color: Colors.white, fontSize: 15),
                      ),
                    ),
                  ],
                )
              ],
            ),
          ),
          Container(
            width: double.infinity,
            padding: EdgeInsets.all(15),
            decoration: BoxDecoration(color: Color.fromRGBO(0, 0, 0, 0.6)),
            margin: EdgeInsets.fromLTRB(0, 40, 0, 0),
            child: Center(
              child: Text(
                widget.monsterName == 'Final boss'
                    ? 'You came across a Final boss. You can\'t flee.'
                    : 'You came across a monster. You can fight or flee. Running away has consequences.',
                style: TextStyle(
                    color: Colors.white, fontSize: 27, fontFamily: 'Great'),
              ),
            ),
          ),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceAround,
            children: [
              Column(
                children: [
                  Container(
                    margin: EdgeInsets.fromLTRB(1, 40, 0, 30),
                    child: Image.network(
                      'https://media.giphy.com/media/l378okiN9iyQAsHRu/giphy.gif',
                      width: 195,
                    ),
                  ),
                  SizedBox(
                    width: 100.0,
                    height: 50.0,
                    child: RaisedButton(
                      shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(20)),
                      color: Color.fromRGBO(0, 0, 0, 0.5),
                      child: const Text(
                        'Fight',
                        style: TextStyle(
                            color: Colors.white70,
                            fontFamily: 'Emotion',
                            fontSize: 20),
                      ),
                      onPressed: () => fight(context),
                    ),
                  ),
                ],
              ),
              Column(
                children: [
                  Container(
                    margin: EdgeInsets.fromLTRB(0, 40, 0, 30),
                    child: Image.network(
                      'https://media.giphy.com/media/l378jtJ6f2ZOpHCBW/giphy.gif',
                      width: 195,
                    ),
                  ),
                  SizedBox(
                    width: 100.0,
                    height: 50.0,
                    child: RaisedButton(
                      shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(20)),
                      color: Color.fromRGBO(0, 0, 0, 0.5),
                      child: const Text('Flee',
                          style: TextStyle(
                              color: Colors.white70,
                              fontFamily: 'Emotion',
                              fontSize: 20)),
                      onPressed: () => flee(context),
                    ),
                  ),
                ],
              ),
            ],
          ),
          Align(
            alignment: Alignment.bottomRight,
            child: Container(
              margin: EdgeInsets.fromLTRB(0, 110, 10, 0),
              child: SizedBox(
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
            ),
          ),
        ],
      ),
    ));
  }
}
