package scala.akka.first.app.mapreduce.actors

/**
  * Created by dsilv on 07/05/2016.
  */

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props
import akka.first.app.mapreduce._
import akka.routing.RoundRobinPool

import scala.akka.first.app.mapreduce.{MapData, ReduceData, Result}

class MasterActor extends Actor {

  val mapActor = context.actorOf(Props[MapActor].withRouter(
    RoundRobinPool(nrOfInstances = 5)), name = "map")
  val reduceActor = context.actorOf(Props[ReduceActor].withRouter(
    RoundRobinPool(nrOfInstances = 5)), name = "reduce")
  val aggregateActor = context.actorOf(Props[AggregateActor],
    name = "aggregate")

  def receive: Receive = {
    case line: String =>
      mapActor ! line
    case mapData: MapData =>
      reduceActor ! mapData
    case reduceData: ReduceData =>
      aggregateActor ! reduceData
    case Result =>
      aggregateActor forward Result
  }
}
