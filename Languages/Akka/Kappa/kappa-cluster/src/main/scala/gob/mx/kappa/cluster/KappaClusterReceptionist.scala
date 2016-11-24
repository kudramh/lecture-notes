package gob.mx.kappa.cluster

import akka.actor.{Actor, ActorLogging, ActorRef, Address, Deploy, Props, ReceiveTimeout, SupervisorStrategy, Terminated}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent
import scala.concurrent.duration._
import akka.remote.RemoteScope
import scala.util.Random


object KappaClusterReceptionist {
  private case class Job(client: ActorRef, url: String)
  case class Get(url: String)
  case class Result(url: String, links: Set[String])
  case class Failed(url: String, reason: String)
}

class KappaClusterReceptionist extends Actor {
  import KappaClusterReceptionist._

  def controllerProps: Props = Props[KappaClusterController]

  override def supervisorStrategy = SupervisorStrategy.stoppingStrategy

  var reqNo = 0

  def receive = waiting

  val waiting: Receive = {
    case Get(url) =>
      context.become(runNext(Vector(Job(sender, url))))
  }

  def running(queue: Vector[Job]): Receive = {
    case KappaClusterController.Result(links) =>
      val job = queue.head
      job.client ! Result(job.url, links)
      context.stop(context.unwatch(sender))
      context.become(runNext(queue.tail))
    case Terminated(_) =>
      val job = queue.head
      job.client ! Failed(job.url, "controller failed unexpectedly")
      context.become(runNext(queue.tail))
    case Get(url) =>
      context.become(enqueueJob(queue, Job(sender, url)))
  }

  def runNext(queue: Vector[Job]): Receive = {
    reqNo += 1
    if (queue.isEmpty) waiting
    else {
      val controller = context.actorOf(controllerProps, s"c$reqNo")
      context.watch(controller)
      controller ! KappaClusterController.Check(queue.head.url, 2)
      running(queue)
    }
  }

  def enqueueJob(queue: Vector[Job], job: Job): Receive = {
    if (queue.size > 3) {
      sender ! Failed(job.url, "queue overflow")
      running(queue)
    } else running(queue :+ job)
  }

}



class ClusterReceptionist  extends Actor with ActorLogging {

  import KappaClusterReceptionist._
  import ClusterEvent.{ MemberUp, MemberRemoved }

  val cluster = Cluster(context.system)
  cluster.subscribe(self, classOf[MemberUp])
  cluster.subscribe(self, classOf[MemberRemoved])

  override def postStop(): Unit = {
    cluster.unsubscribe(self)
  }

  val randomGen = new Random
  def pick[A](coll: IndexedSeq[A]): A = coll(randomGen.nextInt(coll.size))

  def receive = awaitingMembers

  val awaitingMembers: Receive = {
    case Get(url) => sender ! Failed(url, "no nodes available")
    case current: ClusterEvent.CurrentClusterState =>
      val notMe = current.members.toVector map (_.address) filter (_ != cluster.selfAddress)
      if (notMe.nonEmpty) context.become(active(notMe))
    case MemberUp(member) if member.address != cluster.selfAddress =>
      context.become(active(Vector(member.address)))
  }

  def active(addresses: Vector[Address]): Receive = {
    case Get(url) if context.children.size < addresses.size =>
      val client = sender
      val address = pick(addresses)
      context.actorOf(Props(new Customer(client, url, address)))
    case Get(url) =>
      sender ! Failed(url, "too many parallel queries")
    case MemberUp(member) if member.address != cluster.selfAddress =>
      context.become(active(addresses :+ member.address))
    case MemberRemoved(member, _) =>
      val next = addresses filterNot (_ == member.address)
      if (next.isEmpty) context.become(awaitingMembers)
      else context.become(active(next))
  }

}

  class Customer(client: ActorRef, url: String, node: Address) extends Actor {
    implicit val s = context.parent

    override val supervisorStrategy = SupervisorStrategy.stoppingStrategy
    val props = Props[KappaClusterController].withDeploy(Deploy(scope = RemoteScope(node)))
    val controller = context.actorOf(props, "controller")
    context.watch(controller)

    context.setReceiveTimeout(5.seconds)
    controller ! KappaClusterController.Check(url, 2)

    def receive = ({
      case ReceiveTimeout =>
        context.unwatch(controller)
        client ! KappaClusterReceptionist.Failed(url, "controller timed out")
      case Terminated(_) =>
        client ! KappaClusterReceptionist.Failed(url, "controller died")
      case KappaClusterController.Result(links) =>
        context.unwatch(controller)
        client ! KappaClusterReceptionist.Result(url, links)
    }: Receive) andThen (_ => context.stop(self))
  }

