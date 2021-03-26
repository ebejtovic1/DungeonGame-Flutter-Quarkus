import 'package:flutter/material.dart';
import './homePage.dart';

class Lost extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
          width: double.infinity,
          height: double.infinity,
          decoration: BoxDecoration(
            image: DecorationImage(
              image: AssetImage("assets/images/gameOver.jpg"),
              fit: BoxFit.cover,
            ),
          ),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Container(
                margin: EdgeInsets.fromLTRB(0, 350, 0, 0),
                padding: EdgeInsets.all(15),
                decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(15),
                    color: Color.fromRGBO(255, 255, 255, 0.009)),

                //height: 40.0,
                child: Text(
                  'You lose.',
                  style: TextStyle(
                      fontSize: 40, color: Colors.white, fontFamily: 'Great'),
                ),
              ),
            ],
          )),

      floatingActionButton: RaisedButton(
        onPressed: () {
          Navigator.of(context).pushReplacement(
            MaterialPageRoute(
              builder: (c) {
                return MyHomePage(title: 'Flutter Home Page');
              },
            ),
          );
        },
        color: Color.fromRGBO(255, 255, 255, 0.2),
        child: const Text(
          'Play again',
          style: TextStyle(fontFamily: 'Emotion', fontSize: 17),
        ),
        textColor: Colors.white,
      ), // This trailing comma makes auto-formatting nicer for build methods.
    );
  }
}
