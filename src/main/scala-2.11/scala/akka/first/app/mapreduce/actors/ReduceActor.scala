package scala.akka.first.app.mapreduce.actors

/**
  * Created by dsilv on 07/05/2016.
  */
import akka.actor.Actor

import scala.akka.first.app.mapreduce.{MapData, ReduceData, WordCount}
import scala.collection.immutable.Map


class ReduceActor extends Actor {

  def receive: Receive = {
    case MapData(dataList) =>
      sender ! reduce(dataList)
  }

  def reduce(words: IndexedSeq[WordCount]): ReduceData = ReduceData {
    words.foldLeft(Map.empty[String, Int]) { (index, words) =>
      if (index contains words.word)
        index + (words.word -> (index.get(words.word).get + 1))
      else
        index + (words.word -> 1)
    }
  }
}
