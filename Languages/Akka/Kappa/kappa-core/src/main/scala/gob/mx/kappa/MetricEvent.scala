package gob.mx.kappa

import org.joda.time.DateTime
/**
  * Created by levi on 6/8/16.
  */
object MetricEvent {
  import Metric._

  /** Base marker trait. */
  @SerialVersionUID(1L)
  sealed trait MetricEvent extends Serializable

  sealed trait LifeCycleEvent extends MetricEvent
  case object OutputStreamInitialized extends LifeCycleEvent
  case object NodeInitialized extends LifeCycleEvent
}
