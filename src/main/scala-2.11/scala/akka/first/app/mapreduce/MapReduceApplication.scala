package scala.akka.first.app.mapreduce

import java.util.concurrent.TimeUnit

import akka.actor.{ActorSystem, Props}
import akka.util.Timeout
import akka.pattern.ask

import scala.akka.first.app.mapreduce.actors.MasterActor
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Await

/**
  * Created by dsilv on 07/05/2016.
  */
sealed trait MapReduceMessage

case class WordCount(word: String, count: Int) extends MapReduceMessage

case class MapData(dataList: ArrayBuffer[WordCount]) extends MapReduceMessage

case class ReduceData(reduceDataMap: Map[String, Int]) extends MapReduceMessage

case class Result() extends MapReduceMessage


object MapReduceApplication extends App {
  val _system = ActorSystem("MapReduceApp")

  val master = _system.actorOf(Props[MasterActor], name = "master")

  implicit val timeout = Timeout(5, TimeUnit.SECONDS)

  master ! "The quick brown fox tried to jump over the lazy dog and fell on the dog"
  master ! "Dog is man's best friend"
  master ! "Dog and Fox belong to the same family"

  Thread.sleep(500)

  val future = (master ? Result).mapTo[String]
  val result = Await.result(future, timeout.duration)
  println(result)

  _system.terminate()
}