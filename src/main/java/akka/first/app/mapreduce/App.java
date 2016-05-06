package akka.first.app.mapreduce;

import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.first.app.mapreduce.actors.MasterActor;
import akka.first.app.mapreduce.messages.Result;
import akka.japi.Creator;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * Created by 98379720172 on 06/05/2016.
 */
public class App {

    public static void main(String[] args) throws Exception{
        Timeout timeout = new Timeout(Duration.apply(5, TimeUnit.SECONDS));
        ActorSystem _system = ActorSystem.create("MapReduceApp");
        ActorRef master = _system.actorOf(Props.create(new Creator<Actor>() {
            @Override
            public Actor create() throws Exception {
                return new MasterActor();
            }
        }), "master");

        master.tell("The quick brown fox tried to jump over the lazy dog and fell on the dog", ActorRef.noSender());
        master.tell("Dog is man's best friend", ActorRef.noSender());
        master.tell("Dog and Fox belong to the same family", ActorRef.noSender());

        Thread.sleep(5000);

        Future<Object> future = Patterns.ask(master, new Result(), timeout);
        Object result = Await.result(future, (Duration) timeout.duration());

        System.out.println(result);
        _system.terminate();

    }
}
