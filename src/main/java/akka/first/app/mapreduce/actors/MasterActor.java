package akka.first.app.mapreduce.actors;

import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.first.app.mapreduce.messages.MapData;
import akka.first.app.mapreduce.messages.ReduceData;
import akka.first.app.mapreduce.messages.Result;
import akka.japi.Creator;
import akka.routing.RoundRobinPool;
import scala.akka.first.app.mapreduce.actors.ReduceActor;

/**
 * Created by 98379720172 on 06/05/2016.
 */
public class MasterActor extends UntypedActor{

    private static final Creator mapActorCreator = new Creator<Actor>() {
        @Override
        public Actor create() throws Exception {
            return new scala.akka.first.app.mapreduce.actors.MapActor();
        }
    };

    private static final Creator reduceActorCreator = new Creator<Actor>() {
        @Override
        public Actor create() throws Exception {
            return new ReduceActor();
        }
    };

    private static final Creator aggregateActorCreator = new Creator<Actor>() {
        @Override
        public Actor create() throws Exception {
            return new AggregateActor();
        }
    };

    ActorRef mapActor = getContext().actorOf(Props.create(mapActorCreator).withRouter(new RoundRobinPool(5)), "map");
    ActorRef reduceActor = getContext().actorOf(Props.create(reduceActorCreator).withRouter(new RoundRobinPool(5)),"reduce");
    ActorRef aggregateActor = getContext().actorOf(Props.create(aggregateActorCreator), "aggregate");

    public MasterActor() {
    }

    @Override
    public void onReceive(Object message) throws Exception {

        if(message instanceof String){
            mapActor.tell(message, getSelf());
        }else if (message instanceof MapData){
            reduceActor.tell(message, getSelf());
        }else if (message instanceof ReduceData){
            aggregateActor.tell(message, ActorRef.noSender());
        }else if(message instanceof Result){
            aggregateActor.forward(message, getContext());
        }else
            unhandled(message);
    }
}
