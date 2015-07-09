#!/bin/bash

javac src/*java
java -cp src/ TweetWordCount tweet_input/tweets.txt tweet_output/ft1.txt
java -cp src/ RunningMedian tweet_input/tweets.txt tweet_output/ft2.txt
