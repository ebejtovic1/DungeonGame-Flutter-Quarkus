import 'package:flutter/material.dart';
import 'package:flutter/painting.dart';
import './move.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);
  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  String _playerName = 'Player';
  final myController = TextEditingController();

  @override
  void dispose() {
    // Clean up the controller when the widget is disposed.
    myController.dispose();
    super.dispose();
  }

  void _incrementCounter(BuildContext ctx) {
    _playerName = myController.text;

    const url = 'http://10.0.2.2:8080/game';

    http.post(url).then((response) {
      int playerId = json.decode(response.body)[0]['playerId'];
      String player = playerId.toString();
      String url1 = 'http://10.0.2.2:8080/game/player/$player';

      http.get(url1).then((response1) {
        Navigator.of(ctx).pushReplacement(
          MaterialPageRoute(
            builder: (c) {
              return Move(
                healingPotion: json.decode(response1.body)['healingPoting'],
                health: json.decode(response1.body)['health'],
                playerName: _playerName,
                damage: json.decode(response1.body)['damage'],
                playerId: json.decode(response1.body)['id'],
                mapId: json.decode(response1.body)['mapId'],
                title: 'Welcome to Quest for the Orb of Quarkus',
              );
            },
          ),
        );
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
          width: double.infinity,
          height: double.infinity,
          decoration: BoxDecoration(
            image: DecorationImage(
              image: AssetImage("assets/images/BackG.jpg"),
              fit: BoxFit.cover,
            ),
          ),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Container(
                margin: EdgeInsets.fromLTRB(0, 0, 0, 30),
                padding: EdgeInsets.fromLTRB(20, 10, 20, 10),
                decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(15),
                    color: Color.fromRGBO(255, 255, 255, 0.7)),

                //height: 40.0,
                child: Text(
                  'Enter your name',
                  style: TextStyle(
                      fontSize: 35, color: Colors.black, fontFamily: 'Great'),
                ),
              ),
              Container(
                margin: EdgeInsets.fromLTRB(0, 0, 0, 300),
                color: Color.fromRGBO(0, 0, 0, 0.6),
                width: 200.0,
                child: TextFormField(
                  controller: myController,
                  cursorColor: Colors.white,
                  textAlign: TextAlign.center,
                  style: TextStyle(color: Colors.white, fontFamily: 'Great', fontSize: 20),
                  decoration: InputDecoration(
                      border: InputBorder.none,
                      hintText: 'Player',
                      hintStyle:
                          TextStyle(color: Colors.white60, fontFamily: 'Great', fontSize: 20)),
                ),
              ),
            ],
          )),

      floatingActionButton: FloatingActionButton(
        onPressed: () => _incrementCounter(context),
        backgroundColor: Colors.black,
        tooltip: 'Increment',
        child: Icon(Icons.play_arrow),
      ), // This trailing comma makes auto-formatting nicer for build methods.
    );
  }
}
