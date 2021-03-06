# Quiz

Interested in answering multiple choice quiz questions? With a very unfair score function that
makes it almost impossible to have a positive score for a longer period of time? (Some would say it 
is as unfair as RailNL's scorefunction). If you answer "yes" to both of these questions, than this
is the Android App for you!

![alt text](https://github.com/DimitrivC/FinalProject/blob/master/FinalProjectProgrammerenLogIn.jpg "log in")
![alt text](https://github.com/DimitrivC/FinalProject/blob/master/FinalProjectProgrammerenQuiz.jpg "quiz")
![alt text](https://github.com/DimitrivC/FinalProject/blob/master/FinalProjectProgrammerenCreate.jpg "create")

### Prerequisites

To be able to run this program, you need at least the following:
* A Git account
* Android Studio 3.0

### Installing

To install the programm, clone this repository with this URL: 

```
https://github.com/DimitrivC/FinalProject.git
```
Open this in Android Studio, and run it, either on your phone or on an emulator.


## Built With

* [Android Studio 3.0](https://developer.android.com/studio/index.html)


## Authors

* **Dimitri van Capelleveen**

## Review

Two people have been kind enough to review my code:
* **Thom Oosterhuis**:
  * Remove buttons in MainActivity (now LogInActivity) that are meant for testing
  * code in onStart can go in onCreate
  * addListenerForSingleValueEvent contains if/else statement with very similar content
  * Actionbar Icon returns empty list
  * ScoreFragment is very chaotic
* **Lennart Klein**:
  * Refactor you activities and give them recognisable names
  * There are some anonymous functions left, see if you can remove the last ones
  * When retrieving data, such as in a jsonArray with wrong answers, see if you can give them better names
  * Use more smaller functions
