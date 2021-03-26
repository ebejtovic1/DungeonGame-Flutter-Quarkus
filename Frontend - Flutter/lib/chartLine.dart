import 'package:flutter/material.dart';

class ChartLine extends StatelessWidget {
  const ChartLine({
    Key key,
    @required this.rate,
    @required this.title,
    this.name,
    this.number,
  })  : assert(title != null),
        assert(rate != null),
        assert(rate > 0),
        assert(rate <= 1),
        super(key: key);

  final double rate;
  final String title;
  final int number;
  final String name;

  @override
  Widget build(BuildContext context) {
    return LayoutBuilder(builder: (context, constraints) {
      final lineWidget = 400 * rate;
      return Padding(
        padding: const EdgeInsets.only(bottom: 10.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Container(
              constraints: BoxConstraints(minWidth: lineWidget),
              child: IntrinsicWidth(
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Text(
                      '',
                      style: TextStyle(
                        fontSize: 15,
                        color: Colors.white,
                      ),
                    ),
                    if (number != null)
                      Text(
                        number.toString(),
                        style: TextStyle(
                          fontSize: 15,
                          fontWeight: FontWeight.bold,
                          color: Colors.white,
                        ),
                      ),
                  ],
                ),
              ),
            ),
            Container(
              child: Container(
                  margin: EdgeInsets.fromLTRB(5, 4, 0, 0),
                  child: Text(name ?? 'Player',
                      style: TextStyle(
                          fontFamily: 'Great',
                          fontSize: 18,
                          fontWeight: FontWeight.bold,
                          color: Colors.black))),
              margin: EdgeInsets.only(top: 5.0),
              decoration: BoxDecoration(
                borderRadius: BorderRadius.circular(20),
                color: Colors.greenAccent,
                boxShadow: [
                  BoxShadow(color: Colors.greenAccent, spreadRadius: 3),
                ],
              ),
              height: 25,
              width: lineWidget,
            ),
          ],
        ),
      );
    });
  }
}
