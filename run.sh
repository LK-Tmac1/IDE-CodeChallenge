#!/bin/bash

javac src/*java
java -cp src/ TweetWordCount tweet_input/tweets.txt tweet_output/ft1.txt
java -cp src/ RunningMedian tweet_input/tweets.txt tweet_output/ft2.txt

#To try the "distributed" mode, adding a -d|D flag to the end:
#java -cp src/TweetWordCount tweet_input/tweets tweet_output/ft1 -d
